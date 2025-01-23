import history_manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Task 1", TaskStatus.NEW, "Task 1");
        Task task2 = new Task("Task 2", TaskStatus.IN_PROGRESS, "Task 2");
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        Epic epic2 = new Epic("Epic 2", "Epic 2");
        SubTask subTask1 = new SubTask("SubTask1", TaskStatus.NEW, "SubTask1", 3);
        SubTask subTask2 = new SubTask("SubTask2", TaskStatus.IN_PROGRESS, "SubTask2", 3);
        SubTask subTask3 = new SubTask("SubTask3", TaskStatus.DONE, "SubTask3", 4);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(7);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(7);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        System.out.println(taskManager.getHistory());
        taskManager.getSubTaskById(7);
        System.out.println(taskManager.getHistory());
    }

}