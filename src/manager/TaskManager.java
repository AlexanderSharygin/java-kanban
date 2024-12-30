package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<SubTask> getSubTasksByEpicId(int epicId);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subtask);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeTaskById(Integer id);

    void removeSubTaskById(Integer id);

    void removeEpicById(Integer id);

    List<Task> getHistory();
}
