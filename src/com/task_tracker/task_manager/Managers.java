package com.task_tracker.task_manager;

import com.task_tracker.history_manager.FileBackedHistoryManager;
import com.task_tracker.history_manager.HistoryManager;
import com.task_tracker.history_manager.InMemoryHistoryManager;
import com.task_tracker.model.Epic;
import com.task_tracker.model.SubTask;
import com.task_tracker.model.Task;
import com.task_tracker.model.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

import static com.task_tracker.model.TaskType.*;

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
        List<String> history = manager.getTasks().stream()
                .map(k -> k.getId().toString())
                .collect(Collectors.toList());
        return String.join(",", history);
    }

    private static List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }


    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager;
        try {
            fileBackedTasksManager = new FileBackedTasksManager(file.getAbsolutePath());
        } catch (IOException e) {
            throw new ManagerReadException("Wrong file path");
        }
        try (Reader fileReader = new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            fileBackedTasksManager.setIsSourceFileLocked(true);
            while (bufferedReader.ready()) {
                String taskHistory = bufferedReader.readLine();
                if (taskHistory.isEmpty()) {
                    String historyRow = bufferedReader.readLine();
                    List<Integer> itemIds = historyFromString(historyRow);
                    FileBackedHistoryManager fileBackedHistoryManager = new FileBackedHistoryManager();
                    fileBackedTasksManager.setHistoryManager(fileBackedHistoryManager);
                    for (var itemId : itemIds) {
                        fileBackedTasksManager.getTaskById(itemId);
                        fileBackedTasksManager.getSubTaskById(itemId);
                        fileBackedTasksManager.getEpicById(itemId);
                    }
                    break;
                } else if (Character.isDigit(taskHistory.charAt(0))) {
                    Task task = taskFromString(taskHistory);
                    if (task.getType().equals(TASK)) {
                        fileBackedTasksManager.addTask(task, task.getId());
                    } else if (task.getType().equals(SUBTASK)) {
                        fileBackedTasksManager.addSubTask((SubTask) task, task.getId());
                    } else if (task.getType().equals(EPIC)) {
                        fileBackedTasksManager.addEpic((Epic) task, task.getId());
                    }
                }
            }
            fileBackedTasksManager.setIsSourceFileLocked(false);
            return fileBackedTasksManager;
        } catch (IOException exception) {
            throw new ManagerReadException("Something went wrong during reading the file");
        }
    }

    private static Task taskFromString(String source) {
        String[] data = source.split(",");
        String taskType = data[1];
        String taskName = data[2];
        String taskDescription = data[4];
        TaskStatus status = null;
        for (TaskStatus item : TaskStatus.values())
            if (item.toString().equals(data[3])) {
                status = item;
                break;
            }
        if (status == null) {
            throw new InputMismatchException("Invalid task status presents in the history file");
        }
        if (taskType.equals(TASK.toString())) {
            Task task = new Task(taskName, taskDescription, status, TASK);
            task.setId(Integer.parseInt(data[0]));
            return task;
        } else if (taskType.equals(SUBTASK.toString())) {
            int epicId = Integer.parseInt(data[5]);
            SubTask subTask = new SubTask(taskName, taskDescription, status, epicId);
            subTask.setId(Integer.parseInt(data[0]));
            return subTask;
        } else if (taskType.equals(EPIC.toString())) {
            Epic epic = new Epic(taskName, taskDescription, status);
            epic.setId(Integer.parseInt(data[0]));
            return epic;
        } else {
            throw new InputMismatchException("Invalid history file");
        }
    }
}