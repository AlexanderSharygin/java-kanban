import java.util.List;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("First", "one");
        inMemoryTaskManager.addEpic(epic);
        SubTask st1 = new SubTask("St1", "St1One", TaskStatus.IN_PROGRESS, 0);
        inMemoryTaskManager.addSubTask(st1);
        Epic epic2 = new Epic("Second", "EpicTwo");
        inMemoryTaskManager.addEpic(epic2);
        SubTask st3 = new SubTask("St3", "St1One", TaskStatus.NEW, 2);
        inMemoryTaskManager.addSubTask(st3);
        SubTask st4 = new SubTask("St4", "St1One", TaskStatus.NEW, 2);
        inMemoryTaskManager.addSubTask(st4);
        st4.id = 4;
        st4.status = TaskStatus.IN_PROGRESS;
        inMemoryTaskManager.updateSubtask(st4);
        st4.status = TaskStatus.DONE;
        inMemoryTaskManager.updateSubtask(st4);
        st3.id = 3;
        st3.status = TaskStatus.DONE;
        inMemoryTaskManager.updateSubtask(st3);
        st4.status = TaskStatus.DONE;
        inMemoryTaskManager.updateSubtask(st4);

        Task task1 = new Task("jjj", "one", TaskStatus.NEW);
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("jttjj", "otttne", TaskStatus.DONE);
        inMemoryTaskManager.addTask(task2);
        task1.status = TaskStatus.IN_PROGRESS;
        inMemoryTaskManager.updateTask(task1);
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());

        inMemoryTaskManager.getTaskById(5);
        inMemoryTaskManager.getTaskById(6);
        inMemoryTaskManager.getEpicById(0);
        inMemoryTaskManager.getTaskById(5);
        inMemoryTaskManager.getSubTaskById(3);
        inMemoryTaskManager.getSubTaskById(4);
        List<HistoryEntry> historyEntryList = inMemoryTaskManager.getHistory();
        System.out.println(historyEntryList);
    }
}
