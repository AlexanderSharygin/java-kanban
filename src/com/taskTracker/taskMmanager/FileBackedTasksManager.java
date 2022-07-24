package com.taskTracker.taskMmanager;

import com.taskTracker.historyManager.HistoryManager;
import com.taskTracker.model.Epic;
import com.taskTracker.model.SubTask;
import com.taskTracker.model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String fileName;
    public static List<String> HEADER_COLUMNS = List.of("id", "type", "name", "status", "description", "epic");


    public FileBackedTasksManager(String fileName) throws IOException {
        super();
        this.fileName = fileName;
        Path path = Paths.get("resources/" + fileName);
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
        try (Writer fileWriter = new FileWriter("resources/" + fileName, StandardCharsets.UTF_8);
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
            bw.newLine();;
            bw.write(String.join(",", Managers.toString(historyManager)));

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

}

