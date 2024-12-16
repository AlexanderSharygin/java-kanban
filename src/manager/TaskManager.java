package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int idCounter;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.idCounter = 0;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public List<SubTask> getSubTasksByEpicId(int epicId) {
        checkIfEpicExist(epicId);
        List<Integer> subTasksId = epics.get(epicId).getSubtasksId();
        return subTasksId.stream().map(subTasks::get).toList();
    }

    public Task getTaskById(int id) {
        checkIfTaskExist(id);
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        checkIfEpicExist(id);
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        checkIfSubTaskExist(id);
        return subTasks.get(id);
    }


    public void addTask(Task task) {
        if (task != null) {
            task.setId(++idCounter);
            tasks.put(idCounter, task);
        }
    }

    public void addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(++idCounter);
            epic.setStatus(TaskStatus.NEW);
            epics.put(idCounter, epic);
        }
    }

    public void addSubTask(SubTask subtask) {
        if (subtask != null) {
            subtask.setId(++idCounter);
            subTasks.put(idCounter, subtask);
            Epic epicForSubTask = epics.get(subtask.getEpicId());
            if (epicForSubTask != null) {
                epicForSubTask.addSubtask(subtask);
                TaskStatus epicStatus = calculateEpicStatus(subtask.getEpicId());
                epicForSubTask.setStatus(epicStatus);
            }
        }
    }

    public void updateTask(Task task) {
        checkIfTaskExist(task.getId());
        Task existedTask = tasks.get(task.getId());
        existedTask.setName(task.getName());
        existedTask.setDescription(task.getDescription());
        existedTask.setStatus(task.getStatus());
    }

    public void updateEpic(Epic epic) {
        checkIfEpicExist(epic.getId());
        Epic existedEpic = epics.get(epic.getId());
        existedEpic.setName(epic.getName());
        existedEpic.setDescription(epic.getDescription());
    }

    public void updateSubtask(SubTask subtask) {
        checkIfSubTaskExist(subtask.getId());
        checkIfEpicExist(subtask.getEpicId());
        SubTask existedSubTask = subTasks.get(subtask.getId());
        existedSubTask.setName(subtask.getName());
        existedSubTask.setDescription(subtask.getDescription());
        existedSubTask.setStatus(subtask.getStatus());
        Epic epicForSubTask = epics.get(existedSubTask.getEpicId());
        epicForSubTask.setStatus(calculateEpicStatus(subtask.getEpicId()));
    }

    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    public void removeAllEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
            subTasks.clear();
        }
    }

    public void removeAllSubTasks() {
        if (!subTasks.isEmpty()) {
            subTasks.clear();
            epics.values().forEach(k -> k.setStatus(TaskStatus.NEW));
        }
    }

    public void removeTaskById(Integer id) {
        checkIfTaskExist(id);
        tasks.remove(id);
    }

    public void removeSubTaskById(Integer id) {
        checkIfSubTaskExist(id);
        int epicId = subTasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        epic.getSubtasksId().remove(id);
        subTasks.remove(id);
        epic.setStatus(calculateEpicStatus(epicId));
    }

    public void removeEpicById(Integer id) {
        checkIfEpicExist(id);
        List<Integer> subtasksId = epics.get(id).getSubtasksId();
        subtasksId.forEach(subTasks::remove);
        epics.remove(id);
    }

    private void checkIfEpicExist(int epicId) {
        if (!epics.containsKey(epicId)) {
            throw new NoSuchElementException("Эпика с указанным id не существует");
        }
    }

    private void checkIfTaskExist(int taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new NoSuchElementException("Задачи с указанным id не существует");
        }
    }

    private void checkIfSubTaskExist(int subTaskId) {
        if (!subTasks.containsKey(subTaskId)) {
            throw new NoSuchElementException("Подзадачи с указанным id не существует");
        }
    }

    private TaskStatus calculateEpicStatus(int epicId) {
        List<SubTask> subTasksList = subTasks.values().stream()
                .filter(k -> k.getEpicId() == epicId).toList();
        if (subTasksList.isEmpty()) {
            return TaskStatus.NEW;
        }
        List<SubTask> newSubTasks = subTasksList.stream()
                .filter(k -> k.getStatus().equals(TaskStatus.NEW)).toList();
        List<SubTask> doneSubTasks = subTasksList.stream()
                .filter(k -> k.getStatus().equals(TaskStatus.DONE)).toList();
        if (subTasksList.size() == newSubTasks.size()) {
            return TaskStatus.NEW;
        }
        if (subTasksList.size() == doneSubTasks.size()) {
            return TaskStatus.DONE;
        }
        return TaskStatus.IN_PROGRESS;
    }
}