

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Epic epic = new Epic("First", "one");
        taskManager.createNewEpic(epic);
        SubTask st1 = new SubTask("St1", "St1One", TaskStatus.DONE, 0);
        taskManager.createNewSubTask(st1);

        Epic epic2 = new Epic("Second", "EpicTwo");
        taskManager.createNewEpic(epic2);
        SubTask st3 = new SubTask("St3", "St1One", TaskStatus.NEW, 2);
        taskManager.createNewSubTask(st3);

        SubTask st4 = new SubTask("St4", "St1One", TaskStatus.NEW, 2);
        taskManager.createNewSubTask(st4);

        st4.id=4;
        st4.status=TaskStatus.IN_PROGRESS;
        taskManager.updateSubtask(st4);
        st4.status=TaskStatus.DONE;
        taskManager.updateSubtask(st4);
        st3.id=3;
        st3.status=TaskStatus.NEW;
        taskManager.updateSubtask(st3);
        st4.status=TaskStatus.DONE;
        taskManager.updateSubtask(st4);
        taskManager.removeItemById(4, TaskType.SUB_TASK);
        taskManager.removeAllItems(TaskType.SUB_TASK);


        Task task1 = new Task("jjj", "one",TaskStatus.NEW);
        taskManager.createNewTask(task1);
        Task task2 = new Task("jttjj", "otttne",TaskStatus.DONE);
        taskManager.createNewTask(task2);
        task1.id =5;
        task1.status=TaskStatus.IN_PROGRESS;
        taskManager.updateTask(task1);



        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
    }
}
