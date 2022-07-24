import com.taskTracker.historyManager.HistoryManager;
import com.taskTracker.model.Epic;
import com.taskTracker.model.SubTask;
import com.taskTracker.model.Task;
import com.taskTracker.taskMmanager.FileBackedTasksManager;
import com.taskTracker.taskMmanager.Managers;
import com.taskTracker.taskMmanager.TaskManger;

import java.io.IOException;
import java.util.List;

import static com.taskTracker.utils.TaskStatus.*;

public class Main {

    public static void main(String[] args) {

        TaskManger taskManger = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        taskManger.setHistoryManager(inMemoryHistoryManager);
        Epic epic = new Epic("First", "one");
        taskManger.addEpic(epic);
        SubTask st1 = new SubTask("St1", "St1One", IN_PROGRESS, 0);
        taskManger.addSubTask(st1);
        Epic epic2 = new Epic("Second", "EpicTwo");
        taskManger.addEpic(epic2);
        SubTask st3 = new SubTask("St3", "St1One", NEW, 2);
        taskManger.addSubTask(st3);
        SubTask st4 = new SubTask("St4", "St1One", NEW, 2);
        taskManger.addSubTask(st4);
        st4.setId(4);
        st4.setStatus(IN_PROGRESS);
        taskManger.updateSubtask(st4);
        st4.setStatus(DONE);
        taskManger.updateSubtask(st4);
        st3.setId(3);
        st3.setStatus(DONE);
        taskManger.updateSubtask(st3);
        st4.setStatus(DONE);
        taskManger.updateSubtask(st4);

        Task task1 = new Task("jjj", "one", NEW);
        taskManger.addTask(task1);
        Task task2 = new Task("jttjj", "otttne",DONE);
        taskManger.addTask(task2);
        task1.setStatus(IN_PROGRESS);
        taskManger.updateTask(task1);
        System.out.println(taskManger.getTasks());
        System.out.println(taskManger.getEpics());
        System.out.println(taskManger.getSubTasks());

        taskManger.getTaskById(5);
        taskManger.getTaskById(6);
        taskManger.getEpicById(0);
        taskManger.getTaskById(5);
        taskManger.getSubTaskById(3);
        taskManger.getSubTaskById(4);
        taskManger.getTaskById(5);
        taskManger.getTaskById(6);
        taskManger.getEpicById(0);
        taskManger.getTaskById(5);
        taskManger.getEpicById(0);
        taskManger.getEpicById(2);
        taskManger.removeEpicById(2);

        List<Task> history = taskManger.getHistory();
        System.out.println(history);


       try {
           TaskManger  newTaskManager = new FileBackedTasksManager("tasks.csv");
           HistoryManager FileBackedHistoryManager = Managers.getFileBackedHistoryManager();
           newTaskManager.setHistoryManager(FileBackedHistoryManager);
           newTaskManager.addEpic(epic);
           SubTask st11 = new SubTask("St1", "St1One", IN_PROGRESS, 0);
           newTaskManager.addSubTask(st1);
           Epic epic12 = new Epic("Second", "EpicTwo");
           newTaskManager.addEpic(epic2);
           SubTask st13 = new SubTask("St3", "St1One", NEW, 2);
           newTaskManager.addSubTask(st3);
           SubTask st14 = new SubTask("St4", "St1One", NEW, 2);
           newTaskManager.addSubTask(st4);
           Task task12 = new Task("jjj", "one", NEW);
           newTaskManager.addTask(task1);
           newTaskManager.getTaskById(5);
           newTaskManager.getEpicById(0);
           newTaskManager.getSubTaskById(4);


       }
       catch (IOException exception)
       {
           System.out.println("Ошибка при создании файла");
       }

    }
}