import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManger taskManger = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        taskManger.setHistoryManager(inMemoryHistoryManager);
        Epic epic = new Epic("First", "one");
        taskManger.addEpic(epic);
        SubTask st1 = new SubTask("St1", "St1One", TaskStatus.IN_PROGRESS, 0);
        taskManger.addSubTask(st1);
        Epic epic2 = new Epic("Second", "EpicTwo");
        taskManger.addEpic(epic2);
        SubTask st3 = new SubTask("St3", "St1One", TaskStatus.NEW, 2);
        taskManger.addSubTask(st3);
        SubTask st4 = new SubTask("St4", "St1One", TaskStatus.NEW, 2);
        taskManger.addSubTask(st4);
        st4.id = 4;
        st4.status = TaskStatus.IN_PROGRESS;
        taskManger.updateSubtask(st4);
        st4.status = TaskStatus.DONE;
        taskManger.updateSubtask(st4);
        st3.id = 3;
        st3.status = TaskStatus.DONE;
        taskManger.updateSubtask(st3);
        st4.status = TaskStatus.DONE;
        taskManger.updateSubtask(st4);

        Task task1 = new Task("jjj", "one", TaskStatus.NEW);
        taskManger.addTask(task1);
        Task task2 = new Task("jttjj", "otttne", TaskStatus.DONE);
        taskManger.addTask(task2);
        task1.status = TaskStatus.IN_PROGRESS;
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
        taskManger.removeAllSubTasks();

        System.out.println("-------------------------------");
        List<Task> history = inMemoryHistoryManager.getHistory();
        System.out.println(history);
    }
}
