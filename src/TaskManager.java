import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskManager {

    private int idCounter;
    private final ArrayList<Task> tasks;
    private final ArrayList<Epic> epics;
    private final ArrayList<SubTask> subTasks;

    public TaskManager() {
        tasks = new ArrayList<>();
        epics = new ArrayList<>();
        subTasks = new ArrayList<>();
        idCounter = 0;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Epic> getEpics() {
        return epics;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeAllItems(TaskType taskType) {
        if (taskType.equals(TaskType.TASK)) {
            tasks.clear();
        }
        if (taskType.equals(TaskType.EPIC)) {
            epics.clear();
            subTasks.clear();
        }
        if (taskType.equals(TaskType.SUB_TASK)) {
            subTasks.clear();
            epics.forEach(Epic::clearSubtasks);
        }
    }

    public Task getTaskById(int id) {
        var task = tasks.stream().filter(k -> k.id == id).findFirst();
        return task.orElse(null);
    }

    public Epic getEpicById(int id) {
        var epic = epics.stream().filter(k -> k.id == id).findFirst();
        return epic.orElse(null);
    }

    public SubTask getSubTaskById(int id) {
        var subtask = subTasks.stream().filter(k -> k.id == id).findFirst();
        return subtask.orElse(null);
    }

    public void createNewTask(Task task) {
        Task newTask = new Task(task.getName(), task.getDescription(), idCounter, task.getStatus());
        tasks.add(newTask);
        idCounter++;
    }

    public void createNewEpic(Epic epic) {
        Epic newEpic = new Epic(epic.getName(), epic.getDescription(), idCounter);
        epics.add(newEpic);
        idCounter++;
    }

    public void createNewSubTask(SubTask subtask) {
        SubTask newSubTask = new SubTask(subtask.getName(), subtask.getDescription(), idCounter,
                subtask.getStatus(), subtask.getEpicId());
        subTasks.add(newSubTask);
        var subTaskEpic = epics.stream().filter(k -> k.id == subtask.getEpicId()).findFirst();
        subTaskEpic.ifPresent(
                epic -> {
                    epic.addSubtask(newSubTask);
                    updateEpicStatus(epic.getId());
                });
        idCounter++;
    }

    public void updateTask(Task task) {
        var taskFromList = tasks.stream().filter(k -> Objects.equals(k.id, task.getId())).findFirst();
        taskFromList.ifPresent(value -> value.update(task.getName(), task.getDescription(), task.getStatus()));
    }

    public void updateEpic(Epic epic) {
        var epicFromList = epics.stream().filter(k -> Objects.equals(k.id, epic.getId())).findFirst();
        epicFromList.ifPresent(value -> value.update(epic.getName(), epic.getDescription()));
    }

    public void updateSubtask(SubTask subtask) {
        var subTaskFromList = subTasks.stream().filter(k -> Objects.equals(k.id,
                subtask.getId())).findFirst();
        subTaskFromList.ifPresent(value -> value.update(subtask.getName(), subtask.getDescription(),
                subtask.getStatus()));
        var epicFromList = epics.stream().filter(k -> Objects.equals(k.id,
                subtask.getEpicId())).findFirst();
        epicFromList.ifPresent(value -> {
            value.addSubtask(subtask);
            updateEpicStatus(value.getId());
        });
    }

    public void removeItemById(int id, TaskType type) {
        if (type.equals(TaskType.TASK)) {
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getId() == id) {
                    tasks.remove(i);
                    return;
                }
            }
        } else if (type.equals(TaskType.EPIC)) {
            for (int i = 0; i < epics.size(); i++) {
                if (epics.get(i).getId() == id) {
                    epics.remove(i);
                    break;
                }
            }
            for (int i = 0; i < subTasks.size(); i++) {
                if (subTasks.get(i).getEpicId() == id) {
                    subTasks.remove(i);
                    i--;
                }

            }
        } else if (type.equals(TaskType.SUB_TASK)) {
            for (int i = 0; i < subTasks.size(); i++) {
                if (subTasks.get(i).getId() == id) {
                    for (var epic : epics) {
                        if (epic.getId() == subTasks.get(i).getEpicId()) {
                            epic.removeSubtask(subTasks.get(i));
                            subTasks.remove(i);
                            updateEpicStatus(epic.getId());
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public List<SubTask> getSubtasksByEpicId(int epicId) {
        return subTasks.stream().filter(k -> k.getEpicId() == epicId).collect(Collectors.toList());
    }

    private void updateEpicStatus(int epicId) {
        for (var epic : epics) {
            if (epic.getId() == epicId) {
                List<SubTask> epicsSubtasks = subTasks.stream().filter(k -> Objects.equals(k.getEpicId(), epic.getId())).collect(Collectors.toList());
                List<SubTask> newSubTasks = epicsSubtasks.stream().filter(k -> k.getStatus() == TaskStatus.NEW).collect(Collectors.toList());
                List<SubTask> doneSubtasks = epicsSubtasks.stream().filter(k -> k.getStatus() == TaskStatus.DONE).collect(Collectors.toList());
                if (epicsSubtasks.isEmpty() || newSubTasks.size() == epicsSubtasks.size()) {
                    epic.status = TaskStatus.NEW;
                } else if (doneSubtasks.size() == epicsSubtasks.size()) {
                    epic.status = TaskStatus.DONE;
                } else {
                    epic.status = TaskStatus.IN_PROGRESS;
                }
            }
        }
    }
}
