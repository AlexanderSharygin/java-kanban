import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManger {

    private int idCounter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;

    private final List<HistoryEntry> history;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.idCounter = 0;
        this.history = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }


    public void removeAllTasks() {
        tasks.clear();
    }


    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }
    
    public void removeAllSubTasks() {
        subTasks.clear();
        epics.forEach((key, value) -> {
            value.clearSubtasks();
        });

    }

    public List<HistoryEntry> getHistory() {
        return history;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task!=null) {
            addToHistory(task);
        }
        return task;

    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if(epic!=null) {
            addToHistory(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask!=null) {
            addToHistory(subTask);
        }
        return subTask;
    }

    @Override
    public void addTask(Task task) {
        task.setId(idCounter);
        tasks.put(idCounter, task);
        idCounter++;
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(idCounter);
        epics.put(idCounter, epic);
        updateEpicStatus(epic.getId());
        idCounter++;
    }

    @Override
    public void addSubTask(SubTask subtask) {
        subtask.setId(idCounter);
        subTasks.put(idCounter, subtask);
        var subTaskEpic = epics.get(subtask.getEpicId());
        if (subTaskEpic != null) {
            subTaskEpic.addSubtask(subtask);
            updateEpicStatus(subtask.getEpicId());
        }
        idCounter++;
    }

    @Override
    public void updateTask(Task task) {
        var taskFromList = tasks.get(task.getId());
        if (taskFromList != null) {
            taskFromList.setName(task.getName());
            taskFromList.setDescription(task.getDescription());
            taskFromList.setStatus(task.getStatus());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        var epicFromList = epics.get(epic.getId());
        if (epicFromList != null) {
            epicFromList.setName(epic.getName());
            epicFromList.setDescription(epic.getDescription());

        }
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        var subTaskFromList = subTasks.get(subtask.getId());
        if (subTaskFromList != null) {
            subTaskFromList.setName(subtask.getName());
            subTaskFromList.setDescription(subtask.getDescription());
            subTaskFromList.setStatus(subtask.getStatus());
            var epicFromList = epics.get(subtask.getEpicId());
            epicFromList.addSubtask(subtask);
            updateEpicStatus(epicFromList.getId());
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        for (var subtask : subTasks.values()) {
            if (subtask.getId() == id) {
                epics.forEach((key, value) -> {
                    if (value.getId() == subtask.getEpicId()) {
                        value.removeSubtask(subtask);
                        subTasks.remove(id);
                        updateEpicStatus(key);
                    }
                });
            }
        }
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        epics.remove(id);
        subTasks.forEach((key, value) -> {
            if (value.getEpicId() == id) {
                subTasks.remove(key);
            }

        });

    }

    public List<SubTask> getEpicSubtasks(int epicId) {
        List<SubTask> result = new ArrayList<>();
        subTasks.forEach((key, value) -> {
            if (value.getEpicId() == epicId) {
                result.add(value);
            }
        });
        return result;
    }

    private void updateEpicStatus(int epicId) {
        for (var id : epics.keySet()) {
            if (id == epicId) {
                List<SubTask> epicsSubtasks = subTasks.values().stream().filter(
                        k -> Objects.equals(k.getEpicId(), id)).collect(Collectors.toList());
                List<SubTask> newSubTasks = epicsSubtasks.stream().filter(k -> k.getStatus() == TaskStatus.NEW).collect(Collectors.toList());
                List<SubTask> doneSubtasks = epicsSubtasks.stream().filter(k -> k.getStatus() == TaskStatus.DONE).collect(Collectors.toList());
                if (epicsSubtasks.isEmpty() || newSubTasks.size() == epicsSubtasks.size()) {
                    epics.get(id).status = TaskStatus.NEW;
                } else if (doneSubtasks.size() == epicsSubtasks.size()) {
                    epics.get(id).status = TaskStatus.DONE;
                } else {
                    epics.get(id).status = TaskStatus.IN_PROGRESS;
                }
            }
        }
    }

    private void addToHistory(Task task)
    {
        HistoryEntry historyEntry = new HistoryEntry(task);
        if (history.size()==10) {
            history.remove(0);
        }
        history.add(historyEntry);

    }
}