import manager.FileBackedTaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;
import utils.CsvEditor;

import java.io.File;

import static java.time.ZonedDateTime.parse;

public class Main {

    public static void main(String[] args) {
        String filePath = "resources/tasks.csv";
        File file = new File(filePath);
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        Task task1 = new Task("Task 1", TaskStatus.NEW, "Task 1", 30,
                parse("2025-02-23T17:10:40.049300100+01:00[Europe/London]"));
        Task task2 = new Task("Task 2", TaskStatus.IN_PROGRESS, "Task 2", 15,
                parse("2025-02-23T17:45:40.049300100+01:00[Europe/London]"));
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        Epic epic2 = new Epic("Epic 2", "Epic 2");
        SubTask subTask1 = new SubTask("SubTask1", TaskStatus.NEW, "SubTask1", 10,
                parse("2025-02-23T18:45:00.049300100+01:00[Europe/London]"), 3);
        SubTask subTask2 = new SubTask("SubTask2", TaskStatus.IN_PROGRESS, "SubTask2",
                20, parse("2025-02-23T18:55:01.049300100+01:00[Europe/London]"), 3);
        SubTask subTask3 = new SubTask("SubTask3", TaskStatus.DONE, "SubTask3",
                20, parse("2025-02-23T19:15:02.049300100+01:00[Europe/London]"), 4);

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
        CsvEditor.clearFile(filePath);
    }

}