import com.task_tracker.history_manager.FileBackedHistoryManager;
import com.task_tracker.model.Epic;
import com.task_tracker.model.SubTask;
import com.task_tracker.model.Task;
import com.task_tracker.task_manager.FileBackedTasksManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.task_tracker.model.TaskStatus.*;

public class Main {

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager newTaskManager = new FileBackedTasksManager("resources/tasks.csv");
        newTaskManager.setHistoryManager(new FileBackedHistoryManager());
        Task task = new Task("FirstTask", "one", NEW);
        newTaskManager.addTask(task);
        task = new Task("SecondTask", "two", IN_PROGRESS);
        newTaskManager.addTask(task);
        Epic epic = new Epic("EpicOne", "one", NEW);
        newTaskManager.addEpic(epic);
        SubTask subTasktask = new SubTask("ST1", "one", DONE, 2);
        newTaskManager.addSubTask(subTasktask);
        subTasktask = new SubTask("ST2", "two", DONE, 2);
        newTaskManager.addSubTask(subTasktask);
        epic = new Epic("EpicTwo", "eptwo", NEW);
        newTaskManager.addEpic(epic);

        newTaskManager.getTaskById(0);
        newTaskManager.getEpicById(5);
        newTaskManager.getSubTaskById(4);
        newTaskManager.getSubTaskById(3);
        newTaskManager.getEpicById(2);

        List<Integer> historyFirst = newTaskManager.getHistory().stream().map(Task::getId).collect(Collectors.toList());
        FileBackedTasksManager newTaskManager2 = new FileBackedTasksManager("resources/tasks.csv");
        newTaskManager2.loadFromFile();
        List<Integer> historySecond = newTaskManager.getHistory().stream().map(Task::getId).collect(Collectors.toList());
        boolean result = true;
        for (int i = 0; i < historyFirst.size(); i++) {
            if (historyFirst.get(i) != historySecond.get(i)) {
                result = false;
            }
        }
        List<Integer> tasksFirst = newTaskManager.getTasks().stream().map(Task::getId).collect(Collectors.toList());
        List<Integer> tasksSecond = newTaskManager.getTasks().stream().map(Task::getId).collect(Collectors.toList());
        for (int i = 0; i < tasksFirst.size(); i++) {
            if (tasksFirst.get(i) != tasksSecond.get(i)) {
                result = false;
            }
        }
        List<Integer> epicsFirst = newTaskManager.getEpics().stream().map(Task::getId).collect(Collectors.toList());
        List<Integer> epicsSecond = newTaskManager.getEpics().stream().map(Task::getId).collect(Collectors.toList());
        for (int i = 0; i < epicsFirst.size(); i++) {
            if (epicsFirst.get(i) != epicsSecond.get(i)) {
                result = false;
            }
        }
        List<Integer> subtasksFirst = newTaskManager.getSubTasks().stream().map(Task::getId).collect(Collectors.toList());
        List<Integer> subtasksSecond = newTaskManager.getSubTasks().stream().map(Task::getId).collect(Collectors.toList());
        for (int i = 0; i < subtasksFirst.size(); i++) {
            if (subtasksFirst.get(i) != subtasksSecond.get(i)) {
                result = false;
            }
        }

        if (result) {
            System.out.println("Все работает верно");
        } else {
            System.out.println("Ошибка");
        }
    }
}
