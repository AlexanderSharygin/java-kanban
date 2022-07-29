package com.task_tracker.task_manager;

import com.task_tracker.history_manager.FileBackedHistoryManager;
import com.task_tracker.history_manager.HistoryManager;
import com.task_tracker.model.Epic;
import com.task_tracker.model.SubTask;
import com.task_tracker.model.Task;
import com.task_tracker.utils.Converter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.task_tracker.model.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final List<String> HEADER_COLUMNS = List.of("id", "type", "name", "status", "description", "epic");
    private final String filePath;
    private boolean isSourceFileLocked;

    public FileBackedTasksManager(String filePath) throws IOException {
        super();
        this.filePath = filePath;
        isSourceFileLocked = false;
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }

    public void setIsSourceFileLocked(boolean sourceFileLocked) {
        isSourceFileLocked = sourceFileLocked;
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

    public void addTask(Task task, int id) {
        tasks.put(id, task);
        updateIdCounter();
        save();
    }

    public void addEpic(Epic epic, int id) {
        epics.put(id, epic);
        updateIdCounter();
        save();
    }

    public void addSubTask(SubTask subTask, int id) {
        subTasks.put(id, subTask);
        updateIdCounter();
        save();
    }

    private void updateIdCounter() {
        int maxTasksId = 0;
        if (!tasks.keySet().isEmpty()) {
            maxTasksId = Collections.max(tasks.keySet());
        }
        int maxEpicId = 0;
        if (!epics.keySet().isEmpty()) {
            maxEpicId = Collections.max(epics.keySet());
        }
        int maxSubtaskId = 0;
        if (!subTasks.keySet().isEmpty()) {
            maxSubtaskId = Collections.max(subTasks.keySet());
        }
        idCounter = Collections.max(List.of(maxTasksId, maxEpicId, maxSubtaskId)) + 1;
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
            try (Writer fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                String header = String.join(",", HEADER_COLUMNS);
                bufferedWriter.write(header);
                bufferedWriter.newLine();
                for (var task : tasks.values()) {
                    bufferedWriter.write(task.toString());
                    bufferedWriter.newLine();
                }
                for (var epic : epics.values()) {
                    bufferedWriter.write(epic.toString());
                    bufferedWriter.newLine();
                }
                for (var subtask : subTasks.values()) {
                    bufferedWriter.write(subtask.toString());
                    bufferedWriter.newLine();
                }
                bufferedWriter.newLine();
                bufferedWriter.write(String.join(",", Converter.historyToString(historyManager)));
            } catch (IOException exception) {
                throw new ManagerSaveException("Something went wrong during saving the file");
            }
        }
    }

    public void loadFromFile() {
        File file = new File(filePath);
        try (Reader fileReader = new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            isSourceFileLocked = true;
            while (bufferedReader.ready()) {
                String taskHistory = bufferedReader.readLine();
                if (taskHistory.isEmpty() && taskHistory != null) {
                    String historyRow = bufferedReader.readLine();
                    List<Integer> itemIds = Converter.historyFromString(historyRow);
                    FileBackedHistoryManager fileBackedHistoryManager = new FileBackedHistoryManager();
                    this.historyManager = fileBackedHistoryManager;
                    for (var itemId : itemIds) {
                        this.getTaskById(itemId);
                        this.getSubTaskById(itemId);
                        this.getEpicById(itemId);
                    }
                    break;
                } else if (Character.isDigit(taskHistory.charAt(0))) {
                    Task task = Converter.taskFromString(taskHistory);
                    if (task.getType().equals(TASK)) {
                        this.addTask(task, task.getId());
                    } else if (task.getType().equals(SUBTASK)) {
                        this.addSubTask((SubTask) task, task.getId());
                    } else if (task.getType().equals(EPIC)) {
                        this.addEpic((Epic) task, task.getId());
                    }
                }
            }
            isSourceFileLocked = false;
        } catch (IOException exception) {
            throw new ManagerReadException("Something went wrong during reading the file");
        }
    }
}