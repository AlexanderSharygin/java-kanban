import java.util.ArrayList;
import java.util.List;

public interface TaskManger {

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subtask);

    void removeSubTaskById(int id);


    void removeTaskById(int id);

    void removeEpicById(int id);

    List<SubTask> getEpicSubtasks(int epicId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void setHistoryManager(HistoryManager historyManager);
}
