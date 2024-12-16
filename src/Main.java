import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
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

        Task updTask1 = new Task("updTask 1", TaskStatus.DONE, "updTask 1");
        updTask1.setId(task1.getId());
        Task updTask2 = new Task("updTask 2", TaskStatus.NEW, "updTask 2");
        updTask2.setId(task2.getId());
        SubTask updSubTask1 = new SubTask("updSubTask1", TaskStatus.DONE, "updSubTask1", 3);
        updSubTask1.setId(subTask1.getId());
        SubTask updSubTask2 = new SubTask("updSubTask2", TaskStatus.DONE, "updSubTask2", 3);
        updSubTask2.setId(subTask2.getId());
        SubTask updSubTask3 = new SubTask("updSubTask3", TaskStatus.IN_PROGRESS, "updSubTask3", 4);
        updSubTask3.setId(subTask3.getId());

        taskManager.updateTask(updTask1);
        taskManager.updateTask(updTask2);
        taskManager.updateSubtask(updSubTask1);
        taskManager.updateSubtask(updSubTask2);
        taskManager.updateSubtask(updSubTask3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());

        taskManager.removeSubTaskById(7);
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());

        SubTask subTask4 = new SubTask("SubTask4", TaskStatus.IN_PROGRESS, "SubTask4", 4);
        taskManager.addSubTask(subTask4);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
    }
}