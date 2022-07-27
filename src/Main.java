import com.taskTracker.model.Epic;
import com.taskTracker.model.Task;
import com.taskTracker.taskMmanager.FileBackedTasksManager;
import com.taskTracker.taskMmanager.Managers;

import java.io.File;
import java.util.List;

import static com.taskTracker.utils.TaskStatus.NEW;

public class Main {

    public static void main(String[] args) {

        FileBackedTasksManager newTaskManager = Managers.loadFromFile(new File("resources/tasks.csv"));
        Epic epic = new Epic("First", "one", NEW);
        newTaskManager.addEpic(epic);
        List<Task> history = newTaskManager.getHistory();
        System.out.println(history);
    }


}
