import historyManager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static task.TaskStatus.IN_PROGRESS;
import static task.TaskStatus.NEW;

public class HistoryManagerTests {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getEmptyHistoryListSuccess() {
        assertEquals(0, taskManager.getHistory().size(), "");
    }


    @Test
    public void addTaskToHistoryManagerSuccess() {
        Task task1 = new Task("T1", NEW, "one");
        taskManager.addTask(task1);
        taskManager.getTaskById(1);
        assertEquals(1, taskManager.getHistory().size(), "Задача не добавлена в историю");
        assertEquals(task1.getName(), taskManager.getHistory().get(0).getName(), "Имя задачи изменено");
        assertEquals(task1.getDescription(), taskManager.getHistory().get(0).getDescription(), "Описание задачи изменено");
        assertEquals(task1.getStatus(), taskManager.getHistory().get(0).getStatus(), "Статус задачи изменен");
    }

    @Test
    public void addEpicToHistoryManagerSuccess() {
        Epic epic1 = new Epic("EpicOne", "one");
        taskManager.addEpic(epic1);
        taskManager.getEpicById(1);
        assertEquals(1, taskManager.getHistory().size(), "Эпик не добавлена в историю");
        assertEquals(epic1.getName(), taskManager.getHistory().get(0).getName(), "Имя эпика изменено");
        assertEquals(epic1.getDescription(), taskManager.getHistory().get(0).getDescription(), "Описание эпика изменено");

    }

    @Test
    public void addSubTaskToHistoryManagerSuccess() {
        Epic epic1 = new Epic("EpicOne", "one");
        taskManager.addEpic(epic1);
        SubTask subTask2 = new SubTask("ST2", IN_PROGRESS, "one", 1);
        taskManager.addSubTask(subTask2);
        taskManager.getSubTaskById(2);
        assertEquals(1, taskManager.getHistory().size(), "Эпик не добавлена в историю");
        assertEquals(subTask2.getName(), taskManager.getHistory().get(0).getName(), "Имя подзадачи изменено");
        assertEquals(subTask2.getDescription(), taskManager.getHistory().get(0).getDescription(), "Описание подзадачи изменено");
        assertEquals(subTask2.getStatus(), taskManager.getHistory().get(0).getStatus(), "Статус подзадачи изменен");
    }

    @Test
    public void getSameTaskSeveralTimesCorrectHistory() {
        Task task1 = new Task("T1", NEW, "one");
        taskManager.addTask(task1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        assertEquals(1, taskManager.getHistory().size(), "Задача не добавлена в историю");
    }

    @Test
    public void getSameTaskSeveralTimesWithDifferentDataCorrectHistory() {
        Task task1 = new Task("T1", NEW, "one");
        taskManager.addTask(task1);
        taskManager.getTaskById(1);
        task1.setId(1);
        task1.setDescription("upd");
        task1.setName("T2");
        task1.setStatus(IN_PROGRESS);
        taskManager.updateTask(task1);
        taskManager.getTaskById(1);
        assertEquals(1, taskManager.getHistory().size(), "Задача не добавлена в историю");
        assertEquals("upd", taskManager.getHistory().get(0).getDescription(), "Неверные данные задачи");
        assertEquals("T2", taskManager.getHistory().get(0).getName(), "Неверные данные задачи");
        assertEquals(IN_PROGRESS, taskManager.getHistory().get(0).getStatus(), "Неверные данные задачи");
    }

    @Test
    public void getSameSubTaskSeveralTimesCorrectHistory() {
        Epic epic1 = new Epic("EpicOne", "one");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Subtask", IN_PROGRESS, "one", 1);
        taskManager.addSubTask(subTask1);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(2);
        assertEquals(1, taskManager.getHistory().size(), "Подзадача не добавлена в историю");
    }

    @Test
    public void getSameSubTaskSeveralTimesWithDifferentDataCorrectHistory() {
        Epic epic1 = new Epic("Epic1", "1");
        Epic epic2 = new Epic("Epic2", "2");
        SubTask subtask = new SubTask("T1", NEW, "one", 1);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subtask);
        subtask.setId(3);
        taskManager.getSubTaskById(3);
        subtask.setDescription("upd");
        subtask.setName("T2");
        subtask.setStatus(IN_PROGRESS);
        subtask.setEpicId(2);
        taskManager.updateSubtask(subtask);
        taskManager.getSubTaskById(3);
        assertEquals(1, taskManager.getHistory().size(), "Задача не добавлена в историю");
        assertEquals("upd", taskManager.getHistory().get(0).getDescription(), "Неверные данные задачи");
        assertEquals("T2", taskManager.getHistory().get(0).getName(), "Неверные данные задачи");
        assertEquals(IN_PROGRESS, taskManager.getHistory().get(0).getStatus(), "Неверные данные задачи");
        SubTask subTask1 = (SubTask) taskManager.getHistory().get(0);
        assertEquals(2, subTask1.getEpicId(), "Неверные данные задачи");
    }

    @Test
    public void getSameEpicSeveralTimesWithDifferentDataCorrectHistory() {
        Epic epic1 = new Epic("Epic1", "1");
        taskManager.addEpic(epic1);
        taskManager.getEpicById(1);
        epic1.setDescription("upd");
        epic1.setName("upd");
        taskManager.updateEpic(epic1);
        taskManager.getEpicById(1);
        assertEquals(1, taskManager.getHistory().size(), "Задача не добавлена в историю");
        assertEquals("upd", taskManager.getHistory().get(0).getDescription(), "Неверные данные задачи");
        assertEquals("upd", taskManager.getHistory().get(0).getName(), "Неверные данные задачи");
    }

    @Test
    public void getSameEpicSeveralTimesCorrectHistory() {
        Epic epic1 = new Epic("Epic", "one");
        taskManager.addEpic(epic1);
        taskManager.getEpicById(1);
        taskManager.getEpicById(1);
        taskManager.getEpicById(1);
        assertEquals(1, taskManager.getHistory().size(), "Эпик не добавлен в историю");
    }


    @Test
    public void severalItemsHistoryManagerReturnCorrectHistory() {
        Task task1 = new Task("Task", NEW, "one");
        SubTask subTask1 = new SubTask("Subtask", NEW, "one", 1);
        Epic epic1 = new Epic("Epic", "one");
        taskManager.addEpic(epic1);
        taskManager.addTask(task1);
        taskManager.addSubTask(subTask1);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getEpicById(1);
        assertEquals(3, taskManager.getHistory().size(), "История содержит более 10 записей");
        assertEquals(3, taskManager.getHistory().get(0).getId(), "Первая запись истории неверна");
        assertEquals(1, taskManager.getHistory().get(2).getId(), "Последняя запись истории неверна");
    }

    @Test
    public void removedTaskNotExistInHistoryTaskInMiddleOfHistory() {
        Task task1 = new Task("T1", NEW, "one");
        Task task2 = new Task("T2", NEW, "two");
        Task task3 = new Task("T3", NEW, "three");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.removeTaskById(2);
        assertEquals(2, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(task1, taskManager.getHistory().get(0), "Задачи в историю отображаются неверно");
        assertEquals(task3, taskManager.getHistory().get(1), "Задачи в историю отображаются неверно");
    }

    @Test
    public void removedTaskNotExistInHistoryTaskAtEndOfHistory() {
        Task task1 = new Task("T1", NEW, "one");
        Task task2 = new Task("T2", NEW, "two");
        Task task3 = new Task("T3", NEW, "three");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.removeTaskById(3);
        assertEquals(2, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(task1, taskManager.getHistory().get(0), "Задачи в историю отображаются неверно");
        assertEquals(task2, taskManager.getHistory().get(1), "Задачи в историю отображаются неверно");
    }

    @Test
    public void removedTaskNotExistInHistoryTaskAtStartOfHistory() {
        Task task1 = new Task("T1", NEW, "one");
        Task task2 = new Task("T2", NEW, "two");
        Task task3 = new Task("T3", NEW, "three");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.removeTaskById(1);
        assertEquals(2, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(task2, taskManager.getHistory().get(0), "Задачи в историю отображаются неверно");
        assertEquals(task3, taskManager.getHistory().get(1), "Задачи в историю отображаются неверно");
    }

    @Test
    public void removedSubTaskNotExistInHistory() {
        Epic epic = new Epic("T1", "one");
        SubTask st1 = new SubTask("T1", NEW, "one", 1);
        SubTask st2 = new SubTask("T2", NEW, "two", 1);
        Task task1 = new Task("T1", NEW, "one");
        taskManager.addEpic(epic);
        taskManager.addSubTask(st1);
        taskManager.addSubTask(st2);
        taskManager.addTask(task1);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getSubTaskById(2);
        taskManager.removeSubTaskById(2);
        taskManager.removeSubTaskById(3);
        assertEquals(2, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(task1, taskManager.getHistory().get(1), "Задачи в историю отображаются неверно");
        assertEquals(epic, taskManager.getHistory().get(0), "Задачи в историю отображаются неверно");
    }

    @Test
    public void updatedTaskNotUpdatedInHistoryAndCorrectlyRemoved() {
        Task task1 = new Task("T1", NEW, "one");
        Task task2 = new Task("T2", NEW, "two");
        Task task3 = new Task("T3", NEW, "three");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        task2.setStatus(IN_PROGRESS);
        task2.setName("upd");
        task2.setDescription("upd");
        assertEquals(3, taskManager.getHistory().size(), "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T2", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("two", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(2).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T3", taskManager.getHistory().get(2).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("three", taskManager.getHistory().get(2).getDescription(),
                "Задачи в истории отображаются неверно");
        taskManager.removeTaskById(2);
        assertEquals(2, taskManager.getHistory().size(), "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T3", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("three", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");
    }

    @Test
    public void updatedTaskNotUpdatedInHistoryAndCorrectlyUpdatedAfterGet() {
        Task task1 = new Task("T1", NEW, "one");
        Task task2 = new Task("T2", NEW, "two");
        Task task3 = new Task("T3", NEW, "three");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        task2.setStatus(IN_PROGRESS);
        task2.setName("upd");
        task2.setDescription("upd");
        assertEquals(3, taskManager.getHistory().size(), "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T2", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("two", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(2).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T3", taskManager.getHistory().get(2).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("three", taskManager.getHistory().get(2).getDescription(),
                "Задачи в истории отображаются неверно");

        taskManager.getTaskById(2);

        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(IN_PROGRESS, taskManager.getHistory().get(2).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("upd", taskManager.getHistory().get(2).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("upd", taskManager.getHistory().get(2).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T3", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("three", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");
    }


    @Test
    public void updatedSubTaskNotUpdatedInHistoryAndCorrectlyRemoved() {
        Epic epic = new Epic("E1", "one");
        SubTask st1 = new SubTask("ST1", NEW, "one", 1);
        SubTask st2 = new SubTask("ST2", NEW, "two", 1);
        Task task1 = new Task("T1", NEW, "one");
        taskManager.addEpic(epic);
        taskManager.addSubTask(st1);
        taskManager.addSubTask(st2);
        taskManager.addTask(task1);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getSubTaskById(2);

        st1.setStatus(IN_PROGRESS);
        st1.setName("upd");
        st2.setDescription("upd");
        st2.setStatus(IN_PROGRESS);
        st2.setName("upd");
        st2.setDescription("upd");
        taskManager.updateSubtask(st1);
        taskManager.updateSubtask(st2);

        assertEquals(4, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("E1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("ST2", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("two", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(2).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(2).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(2).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(3).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("ST1", taskManager.getHistory().get(3).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(3).getDescription(),
                "Задачи в истории отображаются неверно");

        taskManager.removeSubTaskById(2);
        taskManager.removeSubTaskById(3);

        assertEquals(2, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("E1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");
    }

    @Test
    public void updatedSubTaskNotUpdatedInHistoryAndCorrectlyUpdatedAfterGet() {
        Epic epic = new Epic("E1", "one");
        SubTask st1 = new SubTask("ST1", NEW, "one", 1);
        SubTask st2 = new SubTask("ST2", NEW, "two", 1);
        Task task1 = new Task("T1", NEW, "one");
        taskManager.addEpic(epic);
        taskManager.addSubTask(st1);
        taskManager.addSubTask(st2);
        taskManager.addTask(task1);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getSubTaskById(2);

        st1.setStatus(IN_PROGRESS);
        st1.setName("upd");
        st2.setDescription("upd");
        st2.setStatus(IN_PROGRESS);
        st2.setName("upd");
        st2.setDescription("upd");
        taskManager.updateSubtask(st1);
        taskManager.updateSubtask(st2);


        assertEquals(4, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("E1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("ST2", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("two", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(2).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(2).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(2).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(3).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("ST1", taskManager.getHistory().get(3).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(3).getDescription(),
                "Задачи в истории отображаются неверно");

        taskManager.getSubTaskById(3);

        assertEquals(4, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("E1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(2).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("ST1", taskManager.getHistory().get(2).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(2).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(IN_PROGRESS, taskManager.getHistory().get(3).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("upd", taskManager.getHistory().get(3).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("upd", taskManager.getHistory().get(3).getDescription(),
                "Задачи в истории отображаются неверно");

        taskManager.getEpicById(1);

        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(NEW, taskManager.getHistory().get(1).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("ST1", taskManager.getHistory().get(1).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(1).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(IN_PROGRESS, taskManager.getHistory().get(2).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("upd", taskManager.getHistory().get(2).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("upd", taskManager.getHistory().get(2).getDescription(),
                "Задачи в истории отображаются неверно");

        assertEquals(IN_PROGRESS, taskManager.getHistory().get(3).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("E1", taskManager.getHistory().get(3).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(3).getDescription(),
                "Задачи в истории отображаются неверно");

        taskManager.removeEpicById(1);
        assertEquals(1, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(NEW, taskManager.getHistory().get(0).getStatus(),
                "Задачи в истории отображаются неверно");
        assertEquals("T1", taskManager.getHistory().get(0).getName(),
                "Задачи в истории отображаются неверно");
        assertEquals("one", taskManager.getHistory().get(0).getDescription(),
                "Задачи в истории отображаются неверно");
    }

    @Test
    public void removedEpicNotExistInHistory() {
        Epic epic = new Epic("T1", "one");
        SubTask st1 = new SubTask("T1", NEW, "one", 1);
        SubTask st2 = new SubTask("T2", NEW, "two", 1);
        Task task1 = new Task("T1", NEW, "one");
        taskManager.addEpic(epic);
        taskManager.addSubTask(st1);
        taskManager.addSubTask(st2);
        taskManager.addTask(task1);
        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getSubTaskById(2);
        taskManager.removeEpicById(1);
        assertEquals(1, taskManager.getHistory().size(), "Задачи в историю отображаются неверно");
        assertEquals(task1, taskManager.getHistory().get(0), "Задачи в историю отображаются неверно");
    }
}