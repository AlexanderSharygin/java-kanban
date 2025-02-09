import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static task.TaskStatus.*;


public class InMemoryTaskManagerTests {

    Epic epic;
    SubTask subTaskNew1;
    SubTask subTaskNew2;
    Epic epic2;
    SubTask subTaskDone1;
    SubTask subTaskDone2;
    SubTask subTaskInProgress1;
    SubTask subTaskInProgress2;
    Task taskNew1;
    Task taskNew2;

    public InMemoryTaskManagerTests() {
        epic = new Epic("EpicOne", "one");
        subTaskNew1 = new SubTask("SubTaskN1", NEW, "stN1", 1);
        subTaskNew2 = new SubTask("SubTaskN2", NEW, "stN2", 1);
        epic2 = new Epic("EpicTwo", "Two");
        subTaskDone1 = new SubTask("SubTaskD1", DONE, "stD1", 1);
        subTaskDone2 = new SubTask("SubTaskD2", DONE, "stD1", 1);
        subTaskInProgress1 = new SubTask("SubTaskIP1", IN_PROGRESS, "stIP1", 1);
        subTaskInProgress2 = new SubTask("SubTaskIP2", IN_PROGRESS, "stIP2", 1);
        taskNew1 = new Task("FirstTask", NEW, "t1");
        taskNew2 = new Task("SecondTask", NEW, "t2");
    }

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void epicWithoutSubtaskHasNewStatusSuccessTest() {
        taskManager.addEpic(epic);
        assertEquals(epic.getStatus(), NEW, "Эпик без подзадач должен иметь статус новый");
    }


    @Test
    public void epicWithSeveralNewSubtaskHasNewStatusSuccess() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskNew2);
        assertEquals(epic.getStatus(), NEW, "Эпик c подзадачами в статусе new должен иметь статус новый");
    }

    @Test
    public void epicWithSeveralDoneSubtaskHasDoneStatusSuccess() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        assertEquals(epic.getStatus(), DONE, "Эпик c подзадачами в статусе DONE должен иметь статус DONE");
    }

    @Test
    public void epicWithNewAndDoneSubtaskHasInNewStatusSuccess() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskNew2);
        assertEquals(epic.getStatus(), IN_PROGRESS, "Эпик c подзадачами в статусе DONE и New должен иметь " +
                "статус IN_PROGRESS");
    }


    @Test
    public void epicWithSeveralInProgressSubtaskHasInProgressStatusSuccess() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);

        assertEquals(epic.getStatus(), IN_PROGRESS,
                "Эпик c подзадачами в статусе IN_PROGRESS должен иметь статус IN_PROGRESS");
    }

    @Test
    public void getTasksReturnListOfAllTasksSuccess() {
        taskManager.addTask(taskNew1);
        taskManager.addTask(taskNew2);
        assertEquals(2, taskManager.getTasks().size(), "Список задач отображается неправильно");
    }

    @Test
    public void getTasksReturnEmptyListForManagerWithoutTasksSuccess() {
        assertEquals(0, taskManager.getTasks().size(), "Список задач отображается неправильно");
    }

    @Test
    public void getEpicsReturnListOfAllEpicsSuccess() {
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(), "Список эпиков отображается неправильно");
    }

    @Test
    public void getEpicsReturnEmptyListForManagerWithoutEpicsSuccess() {
        assertEquals(0, taskManager.getEpics().size(), "Список эпиков отображается неправильно");
    }

    @Test
    public void getSubTasksReturnListOfAllSubTasksSuccess() {
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(2, taskManager.getSubTasks().size(), "Список подзадач отображается неправильно");
    }

    @Test
    public void getSubtasksReturnEmptyListForManagerWithoutSubtasksSuccess() {
        assertEquals(0, taskManager.getSubTasks().size(), "Список подзадач отображается неправильно");
    }

    @Test
    public void removeAllTasksSuccess() {
        taskManager.addTask(taskNew1);
        taskManager.addTask(taskNew2);
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getTasks().size(),
                "Список задач отображается неправильно после работы метода removeAllTasks");
    }

    @Test
    public void removeAllSubTasksSuccess() {
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);
        taskManager.removeAllSubTasks();
        assertEquals(0, taskManager.getSubTasks().size(),
                "Список задач отображается неправильно после работы метода removeAllSubTasks");
    }

    @Test
    public void removeAllEpicsSuccess() {
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getEpics().size(),
                "Список задач отображается неправильно после работы метода removeAllEpics");
    }

    @Test
    public void getTaskByIdCorrectIdReturnTaskSuccess() {
        taskManager.addTask(taskNew1);
        assertEquals("FirstTask", taskManager.getTaskById(1).getName(), "Возвращена неверная задача");
    }

    @Test
    public void getTaskByIdWrongIdReturnNull() {
        assertThrows(NoSuchElementException.class, () -> taskManager.getTaskById(101));
    }

    @Test
    public void getEpicByIdCorrectIdReturnEpicSuccess() {
        taskManager.addEpic(epic);
        assertEquals("EpicOne", taskManager.getEpicById(1).getName(), "Возвращена неверный эпик");
    }

    @Test
    public void getEpicByIdWrongIdReturnNull() {
        assertThrows(NoSuchElementException.class, () -> taskManager.getEpicById(101));
    }

    @Test
    public void getSubtaskByIdCorrectIdReturnSubtaskSuccess() {
        taskManager.addSubTask(subTaskInProgress1);
        assertEquals("SubTaskIP1", taskManager.getSubTaskById(1).getName(),
                "Имя задачи отображается неверно");
    }

    @Test
    public void getSubtaskByIdWrongIdReturnNull() {
        assertThrows(NoSuchElementException.class, () -> taskManager.getTaskById(101));
    }

    @Test
    public void addTaskWithCorrectTaskAdded() {
        taskManager.addTask(taskNew1);
        assertEquals(1, taskManager.getTasks().size(), "Задача не добавлена");
    }

    @Test
    public void addNullTaskNotAdded() {
        taskManager.addTask(null);
        assertEquals(0, taskManager.getTasks().size(), "Задача null добавлена");
    }

    @Test
    public void addEpicWithCorrectEpicAdded() {
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getEpics().size(), "Эпик не добавлен");
    }

    @Test
    public void addNullEpicNotAdded() {
        taskManager.addEpic(null);
        assertEquals(0, taskManager.getEpics().size(), "Эпик null не добавлен");
    }

    @Test
    public void addSubtaskWithCorrectSubtaskAdded() {
        taskManager.addSubTask(subTaskInProgress1);
        assertEquals(1, taskManager.getSubTasks().size(), "Подзадача не добавлена");
    }

    @Test
    public void addNullSubtaskNotAdded() {
        taskManager.addSubTask(null);
        assertEquals(0, taskManager.getSubTasks().size(), "Подзадача null добавлена");
    }

    @Test
    public void updateTaskWithCorrectDataSuccess() {
        taskManager.addTask(taskNew1);
        Task task2 = new Task("Updated", DONE, "upd");
        task2.setId(1);
        taskManager.updateTask(task2);
        assertEquals("Updated", taskManager.getTaskById(1).getName(), "Название задачи отображается неверно");
        assertEquals("upd", taskManager.getTaskById(1).getDescription(), "Описание задачи отображается неверно");
        assertEquals(DONE, taskManager.getTaskById(1).getStatus(), "Статус задачи отображается неверно");
    }

    @Test
    public void updateTaskByTaskWithWrongIdNoSuchElementException() {
        taskManager.addTask(taskNew1);
        Task task2 = new Task("Updated", DONE, "upd");
        task2.setId(2);
        assertThrows(NoSuchElementException.class, () -> taskManager.updateTask(task2));
    }

    @Test
    public void updateSubTaskWithCorrectDataSuccess() {
        Epic epic = new Epic("upd", "upd");
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskInProgress1);
        SubTask subTask2 = new SubTask("upd", NEW, "upd", 1);
        subTask2.setId(2);
        taskManager.updateSubtask(subTask2);
        assertEquals("upd", taskManager.getSubTaskById(2).getName(), "Название подзадачи отображается неверно");
        assertEquals("upd", taskManager.getSubTaskById(2).getDescription(), "Описание подзадачи отображается неверно");
        assertEquals(NEW, taskManager.getSubTaskById(2).getStatus(), "Статус подзадачи отображается неверно");
    }

    @Test
    public void updateSubTaskByTaskWithWrongIdNoSuchElementException() {
        taskManager.addSubTask(subTaskInProgress1);
        SubTask subTask2 = new SubTask("upd", NEW, "upd", 1);
        subTask2.setId(2);
        assertThrows(NoSuchElementException.class, () -> taskManager.updateSubtask(subTask2));
    }

    @Test
    public void updateEpicWithCorrectDataSuccess() {
        taskManager.addEpic(epic);
        Epic epic2 = new Epic("upd", "upd");
        epic2.setId(1);
        taskManager.updateEpic(epic2);
        assertEquals("upd", taskManager.getEpicById(1).getName(), "Название эпика отображается неверно");
        assertEquals("upd", taskManager.getEpicById(1).getDescription(), "Описание эпика отображается неверно");
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика отображается неверно");
    }

    @Test
    public void updateEpicByTaskWithWrongIdNoSuchElementException() {
        taskManager.addEpic(epic);
        Epic epic2 = new Epic("upd", "upd");
        epic2.setId(2);
        assertThrows(NoSuchElementException.class, () -> taskManager.updateEpic(epic2));
    }

    @Test
    public void removeTaskCorrectIdSuccess() {
        taskManager.addTask(taskNew1);
        taskManager.removeTaskById(1);
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена");
    }

    @Test
    public void removeTaskWrongIdNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> taskManager.removeTaskById(1));
    }

    @Test
    public void removeSubTaskCorrectIdSuccess() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.removeSubTaskById(2);
        assertEquals(0, taskManager.getSubTasks().size(), "Подзадача не удалена");
    }

    @Test
    public void removeSubTaskWrongIdNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> taskManager.removeSubTaskById(1));
    }

    @Test
    public void removeEpicCorrectIdSuccess() {
        taskManager.addEpic(epic);
        taskManager.removeEpicById(1);
        assertEquals(0, taskManager.getEpics().size(), "Эпик не удален");
    }

    @Test
    public void removeEpicWrongIdNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> taskManager.removeEpicById(1));
    }

    @Test
    public void getEpicsSubtasksEpicWithoutSubtasksEmptyList() {
        taskManager.addEpic(epic);
        assertEquals(0, taskManager.getSubTasksByEpicId(1).size(), "Список подзадач не пуст");
    }

    @Test
    public void getEpicsSubtasksEpicWithSubtasksSubtasksList() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskNew2);
        assertEquals(2, taskManager.getSubTasksByEpicId(1).size(), "Список подзадач пуст");
    }


    @Test
    public void getEpicsSubtasksWrongEpicIdNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> taskManager.getSubTasksByEpicId(0));
    }

    @Test
    public void epicStatusUpdateAddInProgressSubTaskToNewEpicInProgress() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void epicStatusUpdateAddDoneSubTaskToNewEpicInProgress() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
        taskManager.addSubTask(subTaskDone2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void epicStatusUpdateAddDoneSubTaskToInProgressEpicInProgress() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void epicStatusUpdateAddInProgressSubTaskToDoneEpicInProgress() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void epicStatusUpdateAddNewSubTaskToDoneEpicInProgress() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
        taskManager.addSubTask(subTaskNew2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void epicStatusUpdateAddDoneSubTaskToDoneEpicDone() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    public void epicStatusUpdateNewEpicSubTaskToInProgressEpicInProgress() {
        SubTask subTask3 = new SubTask("ST2", IN_PROGRESS, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskNew2);
        subTask3.setId(2);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
        taskManager.updateSubtask(subTask3);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void epicStatusUpdateInProgressEpicSubTaskToNewEpicInProgress() {
        SubTask subTask3 = new SubTask("ST2", NEW, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
        subTask3.setId(3);
        taskManager.updateSubtask(subTask3);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
    }

    @Test
    public void epicStatusUpdateInProgressEpicSubTaskToDoneEpicDone() {
        SubTask subTask3 = new SubTask("ST2", DONE, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskInProgress2);
        subTask3.setId(3);
        taskManager.updateSubtask(subTask3);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    public void epicStatusUpdateDoneEpicSubTaskToInProgressEpicInProgress() {
        SubTask subTask3 = new SubTask("ST2", IN_PROGRESS, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        subTask3.setId(3);
        taskManager.updateSubtask(subTask3);

        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    public void epicStatusRemoveDoneEpicSubTaskEpicDone() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        taskManager.removeSubTaskById(3);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    public void epicStatusRemoveAllEpicSubTasksEpicNew() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        taskManager.removeSubTaskById(2);
        taskManager.removeSubTaskById(3);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
    }

    @Test
    public void epicStatusRemoveInProgressEpicSubTasksEpicNew() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быт IN_PROGRESS");
        taskManager.removeSubTaskById(3);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быт NEW");
    }

    @Test
    public void epicNotContainsRemovedSubTasks() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskInProgress2);
        taskManager.removeSubTaskById(2);
        assertEquals(1, taskManager.getEpicById(1).getSubtasksId().size(),
                "Количество подзадач в эпике больше ожидаемого" );
        assertEquals(3, taskManager.getEpicById(1).getSubtasksId().get(0),
                "Id подзадачи не верен");
    }
}