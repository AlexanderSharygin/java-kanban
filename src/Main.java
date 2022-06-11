

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Epic epic = new Epic("First", "one");
        taskManager.addEpic(epic);
        SubTask st1 = new SubTask("St1", "St1One", TaskStatus.IN_PROGRESS, 0);
        taskManager.addSubTask(st1);
        Epic epic2 = new Epic("Second", "EpicTwo");
        taskManager.addEpic(epic2);
        SubTask st3 = new SubTask("St3", "St1One", TaskStatus.NEW, 2);
        taskManager.addSubTask(st3);
        SubTask st4 = new SubTask("St4", "St1One", TaskStatus.NEW, 2);
        taskManager.addSubTask(st4);
        st4.id = 4;
        st4.status = TaskStatus.IN_PROGRESS;
        taskManager.updateSubtask(st4);
        st4.status = TaskStatus.DONE;
        taskManager.updateSubtask(st4);
        st3.id = 3;
        st3.status = TaskStatus.DONE;
        taskManager.updateSubtask(st3);
        st4.status = TaskStatus.DONE;
        taskManager.updateSubtask(st4);
        taskManager.removeAllSubTasks();
        Task task1 = new Task("jjj", "one", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("jttjj", "otttne", TaskStatus.DONE);
        taskManager.addTask(task2);
        task1.id = 1;
        task1.status = TaskStatus.IN_PROGRESS;
        taskManager.updateTask(task1);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
    }
}
