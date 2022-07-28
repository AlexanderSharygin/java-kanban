import com.task_tracker.model.Epic;
import com.task_tracker.model.Task;
import com.task_tracker.task_manager.FileBackedTasksManager;
import com.task_tracker.task_manager.Managers;

import java.io.File;
import java.util.List;

import static com.task_tracker.model.TaskStatus.NEW;

public class Main {

    public static void main(String[] args) {

        FileBackedTasksManager newTaskManager = Managers.loadFromFile(new File("resources/tasks.csv"));
        Epic epic = new Epic("First", "one", NEW);
        newTaskManager.addEpic(epic);
        List<Task> history = newTaskManager.getHistory();
        System.out.println(history);
    }


}
