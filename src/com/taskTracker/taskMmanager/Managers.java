package com.taskTracker.taskMmanager;

import com.taskTracker.historyManager.FileBackedHistoryManager;
import com.taskTracker.historyManager.HistoryManager;
import com.taskTracker.historyManager.InMemoryHistoryManager;
import com.taskTracker.model.Epic;
import com.taskTracker.model.SubTask;
import com.taskTracker.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.taskTracker.model.TaskType.*;

public class Managers {

    public static TaskManger getDefault() {
        return new InMemoryTaskManager();
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HistoryManager getFileBackedHistoryManager() {
        return new FileBackedHistoryManager();
    }

    public static String toString(HistoryManager manager) {
        List<String> history = manager.getTasks().stream().
                map(k -> k.getId().toString()).collect(Collectors.toList());
        return String.join(",", history);
    }



    public static FileBackedTasksManager loadFromFile(File file) throws IOException {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.getAbsolutePath());
        try (Reader fileReader = new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(fileReader)) {
            fileBackedTasksManager.setSourceFileLocked(true);
            while (br.ready()) {
                String historyRow = br.readLine();
                if (historyRow.isEmpty()) {
                    break;
                } else if (Character.isDigit(historyRow.charAt(0))) {
                    Task task = fileBackedTasksManager.fromString(historyRow);
                    if (task.getType().equals(TASK)) {
                        fileBackedTasksManager.addTask(task);
                    } else if (task.getType().equals(SUBTASK)) {
                        fileBackedTasksManager.addSubTask((SubTask) task);
                    } else if (task.getType().equals(EPIC)) {
                        fileBackedTasksManager.addEpic((Epic) task);
                    }
                }
            }
            fileBackedTasksManager.setSourceFileLocked(false);
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerReadException("Something went wrong during reading the file");
        }
    }
}