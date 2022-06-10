import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskManager {

    private int idCounter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.idCounter = 0;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

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

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void addTask(Task task) {
        Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
        newTask.setId(idCounter);
        tasks.put(idCounter, newTask);
        idCounter++;
    }

    public void addEpic(Epic epic) {
        Epic newEpic = new Epic(epic.getName(), epic.getDescription());
        newEpic.setId(idCounter);
        epics.put(idCounter, newEpic);
        epics.forEach((key,value)->updateEpicStatus(key));
        idCounter++;
    }

    public void addSubTask(SubTask subtask) {
        SubTask newSubTask = new SubTask(subtask.getName(), subtask.getDescription(),
                subtask.getStatus(), subtask.getEpicId());
        newSubTask.setId(idCounter);
        subTasks.put(idCounter, newSubTask);

        var subTaskEpic = epics.get(newSubTask.getEpicId());
        if (subTaskEpic != null) {
            subTaskEpic.addSubtask(newSubTask);
            updateEpicStatus(subtask.getEpicId());
        }
        idCounter++;
    }

    public void updateTask(Task task) {
        var taskFromList = tasks.get(task.getId());
        if (taskFromList != null) {
            taskFromList.update(task.getName(), task.getDescription(), task.getStatus());
        }
    }

    public void updateEpic(Epic epic) {
        var epicFromList = epics.get(epic.getId());
        if (epicFromList != null) {
            epicFromList.update(epic.getName(), epic.getDescription());
        }
    }

    public void updateSubtask(SubTask subtask) {
        var subTaskFromList = subTasks.get(subtask.getId());
        if (subTaskFromList != null) {
            subTaskFromList.update(subtask.getName(), subtask.getDescription(),
                    subtask.getStatus());
            var epicFromList = epics.get(subtask.getEpicId());
            epicFromList.addSubtask(subtask);
            updateEpicStatus(epicFromList.getId());
        }
    }


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


    public void removeTaskById(int id) {
        tasks.remove(id);
    }

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
}
