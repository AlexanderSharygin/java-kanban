package manager;

import history.manager.HistoryManager;
import history.manager.Managers;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    private int idCounter;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        this.idCounter = 0;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        checkIfEpicExist(epicId);
        List<Integer> subTasksId = epics.get(epicId).getSubtasksId();
        return subTasksId.stream().map(subTasks::get).toList();
    }

    @Override
    public Task getTaskById(int id) {
        checkIfTaskExist(id);
        Task task = tasks.get(id);
        historyManager.add(task);

        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        checkIfEpicExist(id);
        Epic epic = epics.get(id);
        historyManager.add(epic);

        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        checkIfSubTaskExist(id);
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);

        return subTask;
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            task.setId(++idCounter);
            tasks.put(idCounter, task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(++idCounter);
            epic.setStatus(TaskStatus.NEW);
            epics.put(idCounter, epic);
        }
    }

    @Override
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

    @Override
    public void updateTask(Task task) {
        checkIfTaskExist(task.getId());
        Task existedTask = tasks.get(task.getId());
        existedTask.setName(task.getName());
        existedTask.setDescription(task.getDescription());
        existedTask.setStatus(task.getStatus());
    }

    @Override
    public void updateEpic(Epic epic) {
        checkIfEpicExist(epic.getId());
        Epic existedEpic = epics.get(epic.getId());
        existedEpic.setName(epic.getName());
        existedEpic.setDescription(epic.getDescription());
    }

    @Override
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

    @Override
    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    @Override
    public void removeAllEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
            subTasks.clear();
        }
    }

    @Override
    public void removeAllSubTasks() {
        if (!subTasks.isEmpty()) {
            subTasks.clear();
            epics.values().forEach(k -> k.setStatus(TaskStatus.NEW));
        }
    }

    @Override
    public void removeTaskById(Integer id) {
        checkIfTaskExist(id);
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTaskById(Integer id) {
        checkIfSubTaskExist(id);
        int epicId = subTasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        epic.getSubtasksId().remove(id);
        subTasks.remove(id);
        historyManager.remove(id);
        epic.setStatus(calculateEpicStatus(epicId));
    }

    @Override
    public void removeEpicById(Integer id) {
        checkIfEpicExist(id);
        List<Integer> subtasksId = epics.get(id).getSubtasksId();
        for (Integer i : subtasksId) {
            subTasks.remove(i);
            historyManager.remove(i);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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