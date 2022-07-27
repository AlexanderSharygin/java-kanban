package com.taskTracker.taskMmanager;

import com.taskTracker.historyManager.HistoryManager;
import com.taskTracker.model.Epic;
import com.taskTracker.model.SubTask;
import com.taskTracker.model.Task;
import com.taskTracker.utils.TaskStatus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;

import static com.taskTracker.model.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String fileName;

    public void setSourceFileLocked(boolean sourceFileLocked) {
        isSourceFileLocked = sourceFileLocked;
    }

    private boolean isSourceFileLocked;
    public static List<String> HEADER_COLUMNS = List.of("id", "type", "name", "status", "description", "epic");


    public FileBackedTasksManager(String filePath) throws IOException {
        super();
        this.fileName = filePath;
        isSourceFileLocked = false;
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }


    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void setHistoryManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    @Override
    public void removeAllTasks() {
        super.removeAllTasks();

        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();

        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();

        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);

        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);

        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);

        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);

        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    public void save() {
        if (!isSourceFileLocked) {
            try (Writer fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
                 BufferedWriter bw = new BufferedWriter(fileWriter)) {
                String header = String.join(",", HEADER_COLUMNS);
                bw.write(header);
                bw.newLine();
                for (var task : tasks.values()) {
                    bw.write(task.toString());
                    bw.newLine();
                }
                for (var epic : epics.values()) {
                    bw.write(epic.toString());
                    bw.newLine();
                }
                for (var subtask : subTasks.values()) {
                    bw.write(subtask.toString());
                    bw.newLine();
                }
                bw.newLine();
                bw.write(String.join(",", Managers.toString(historyManager)));
            } catch (IOException exception) {
                throw new ManagerSaveException("Something went wrong during saving the file");
            }
        }
    }

    public Task fromString(String source) {
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

