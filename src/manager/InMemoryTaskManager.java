package manager;

import exception.TaskIsIntersectingException;
import history.manager.HistoryManager;
import history.manager.Managers;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, SubTask> subTasks;
    protected final TreeSet<Task> sortedByTimeItems;
    private final HistoryManager historyManager;
    private int idCounter;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        this.idCounter = 0;
        this.sortedByTimeItems = new TreeSet<>((task1, task2) ->
                (int) Duration.between(task2.getUtcStartTime(), task1.getUtcStartTime()).toSeconds());
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
            checkIfTaskIntersect(task);
            if (task.getId() == null) {
                task.setId(++idCounter);
            } else {
                idCounter = Integer.max(++idCounter, task.getId());
            }
            tasks.put(task.getId(), task);
            sortedByTimeItems.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            if (epic.getId() == null) {
                epic.setId(++idCounter);
            } else {
                idCounter = Integer.max(++idCounter, epic.getId());
            }
            if (epic.getStatus() == null) {
                epic.setStatus(TaskStatus.NEW);
            }
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void addSubTask(SubTask subtask) {
        if (subtask != null) {
            Epic epicForSubTask = epics.get(subtask.getEpicId());
            checkIfEpicExist(epicForSubTask.getId());
            checkIfTaskIntersect(subtask);
            if (subtask.getId() == null) {
                subtask.setId(++idCounter);
            } else {
                idCounter = Integer.max(++idCounter, subtask.getId());
            }
            subTasks.put(subtask.getId(), subtask);
            epicForSubTask.addSubtask(subtask);
            TaskStatus epicStatus = calculateEpicStatus(subtask.getEpicId());
            epicForSubTask.setStatus(epicStatus);
            calculateEpicTime(subtask.getEpicId());
            sortedByTimeItems.add(subtask);
        }
    }

    @Override
    public void updateTask(Task task) {
        checkIfTaskExist(task.getId());
        checkIfTaskIntersect(task);
        Task existedTask = tasks.get(task.getId());
        existedTask.setName(task.getName());
        existedTask.setDescription(task.getDescription());
        existedTask.setStatus(task.getStatus());
        existedTask.setStartTime(task.getUtcStartTime());
        existedTask.setDuration(task.getDuration());
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
        checkIfTaskIntersect(subtask);
        checkIfEpicExist(subtask.getEpicId());
        SubTask existedSubTask = subTasks.get(subtask.getId());
        existedSubTask.setName(subtask.getName());
        existedSubTask.setDescription(subtask.getDescription());
        existedSubTask.setStatus(subtask.getStatus());
        existedSubTask.setStartTime(subtask.getUtcStartTime());
        existedSubTask.setDuration(subtask.getDuration());
        Epic epicForSubTask = epics.get(existedSubTask.getEpicId());
        epicForSubTask.setStatus(calculateEpicStatus(subtask.getEpicId()));
        calculateEpicTime(existedSubTask.getEpicId());
    }

    @Override
    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.keySet().forEach(historyManager::remove);
            tasks.clear();
        }
    }

    @Override
    public void removeAllEpics() {
        if (!epics.isEmpty()) {
            epics.keySet().forEach(historyManager::remove);
            subTasks.keySet().forEach(historyManager::remove);
            epics.clear();
            subTasks.clear();
        }
    }

    @Override
    public void removeAllSubTasks() {
        if (!subTasks.isEmpty()) {
            subTasks.keySet().forEach(historyManager::remove);
            epics.forEach((_, value) ->
            {
                value.clearSubtasksId();
                value.setDuration(Duration.ZERO);
                value.setStartTime(LocalDateTime.MAX);
                value.setEndTime(null);
            });
            subTasks.clear();
        }
    }

    @Override
    public void removeTaskById(Integer id) {
        checkIfTaskExist(id);
        sortedByTimeItems.remove(tasks.get(id));
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
        calculateEpicTime(epicId);
    }

    @Override
    public void removeEpicById(Integer id) {
        checkIfEpicExist(id);
        List<Integer> subtasksId = epics.get(id).getSubtasksId();
        subtasksId.forEach(i -> {
            subTasks.remove(i);
            historyManager.remove(i);
        });
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedByTimeItems);
    }

    protected boolean isTaskIntersect(Task task) {
        for (var item : sortedByTimeItems) {
            LocalDateTime newTaskStartTime = task.getUtcStartTime();
            LocalDateTime newTaskEndTime = task.getUtcStartTime().plus(task.getDuration());
            LocalDateTime oldTaskStartTime = item.getUtcStartTime();
            LocalDateTime oldTaskEndTime = item.getUtcStartTime().plus(item.getDuration());
            if (newTaskStartTime.isBefore(oldTaskStartTime) && newTaskEndTime.isBefore(oldTaskEndTime)) {
                return true;
            } else if (newTaskStartTime.isAfter(oldTaskStartTime) && newTaskEndTime.isAfter(oldTaskEndTime)) {
                return true;
            }
            if (oldTaskEndTime.isBefore(newTaskStartTime) && oldTaskEndTime.isBefore(newTaskEndTime)) {
                return true;
            } else if (oldTaskStartTime.isAfter(newTaskStartTime) && oldTaskEndTime.isAfter(newTaskEndTime)) {
                return true;
            } else if (isTaskInsideAnother(newTaskStartTime, newTaskEndTime, oldTaskStartTime, oldTaskEndTime) ||
                    isTaskInsideAnother(oldTaskStartTime, oldTaskEndTime, newTaskStartTime, newTaskEndTime)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTaskInsideAnother(LocalDateTime firstTaskStartTime,
                                        LocalDateTime firstTaskEndTime,
                                        LocalDateTime secondTaskStartTime,
                                        LocalDateTime secondTaskEndTime) {
        return (firstTaskStartTime.isAfter(secondTaskStartTime) || firstTaskStartTime.isEqual(secondTaskStartTime))
                && (firstTaskEndTime.isBefore(secondTaskEndTime) || firstTaskEndTime.isEqual(secondTaskEndTime));
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

    private void checkIfTaskIntersect(Task task) {
        if (isTaskIntersect(task)) {
            throw new TaskIsIntersectingException("Задача пересекается по времени с другими задачами/подзадачами");
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

    protected void calculateEpicTime(int epicId) {
        List<SubTask> subTasksList = new ArrayList<>(subTasks.values().stream()
                .filter(k -> k.getEpicId() == epicId).toList());
        subTasksList.sort((o1, o2) -> (int) Duration.between(o2.getUtcStartTime(), o1.getUtcStartTime()).toSeconds());
        if (!subTasksList.isEmpty()) {
            epics.get(epicId).setStartTime(subTasksList.getFirst().getUtcStartTime());
            epics.get(epicId).setEndTime(subTasksList.getLast().getUtcStartTime().plus(subTasksList.getLast()
                    .getDuration()));
            epics.get(epicId).setDuration(Duration.between(epics.get(epicId).getUtcStartTime(),
                    epics.get(epicId).getUtcEndTime()));
        }
    }
}