import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static task.TaskStatus.*;


public class TaskManagerTestsLogic {

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

    public TaskManagerTestsLogic() {
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


    public void epicWithoutSubtaskHasNewStatusSuccessTest(TaskManager taskManager) {
        taskManager.addEpic(epic);
        assertEquals(epic.getStatus(), NEW, "Эпик без подзадач должен иметь статус новый");
    }

    public void epicWithSeveralNewSubtaskHasNewStatusSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskNew2);
        assertEquals(epic.getStatus(), NEW, "Эпик c подзадачами в статусе new должен иметь статус новый");
    }

    public void epicWithSeveralDoneSubtaskHasDoneStatusSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        assertEquals(epic.getStatus(), DONE, "Эпик c подзадачами в статусе DONE должен иметь статус DONE");
    }

    public void epicWithNewAndDoneSubtaskHasInNewStatusSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskNew2);
        assertEquals(epic.getStatus(), IN_PROGRESS, "Эпик c подзадачами в статусе DONE и New должен иметь " +
                "статус IN_PROGRESS");
    }

    public void epicWithSeveralInProgressSubtaskHasInProgressStatusSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);

        assertEquals(epic.getStatus(), IN_PROGRESS,
                "Эпик c подзадачами в статусе IN_PROGRESS должен иметь статус IN_PROGRESS");
    }

    public void getTasksReturnListOfAllTasksSuccess(TaskManager taskManager) {
        taskManager.addTask(taskNew1);
        taskManager.addTask(taskNew2);
        assertEquals(2, taskManager.getTasks().size(), "Список задач отображается неправильно");
    }

    public void getTasksReturnEmptyListForManagerWithoutTasksSuccess(TaskManager taskManager) {
        assertEquals(0, taskManager.getTasks().size(), "Список задач отображается неправильно");
    }

    public void getEpicsReturnListOfAllEpicsSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(), "Список эпиков отображается неправильно");
    }

    public void getEpicsReturnEmptyListForManagerWithoutEpicsSuccess(TaskManager taskManager) {
        assertEquals(0, taskManager.getEpics().size(), "Список эпиков отображается неправильно");
    }

    public void getSubTasksReturnListOfAllSubTasksSuccess(TaskManager taskManager) {
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(2, taskManager.getSubTasks().size(), "Список подзадач отображается неправильно");
    }

    public void getSubtasksReturnEmptyListForManagerWithoutSubtasksSuccess(TaskManager taskManager) {
        assertEquals(0, taskManager.getSubTasks().size(), "Список подзадач отображается неправильно");
    }

    public void removeAllTasksSuccess(TaskManager taskManager) {
        taskManager.addTask(taskNew1);
        taskManager.addTask(taskNew2);
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getTasks().size(),
                "Список задач отображается неправильно после работы метода removeAllTasks");
    }

    public void removeAllSubTasksSuccess(TaskManager taskManager) {
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);
        taskManager.removeAllSubTasks();
        assertEquals(0, taskManager.getSubTasks().size(),
                "Список задач отображается неправильно после работы метода removeAllSubTasks");
    }

    public void removeAllEpicsSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getEpics().size(),
                "Список задач отображается неправильно после работы метода removeAllEpics");
    }

    public void getTaskByIdCorrectIdReturnTaskSuccess(TaskManager taskManager) {
        taskManager.addTask(taskNew1);
        assertEquals("FirstTask", taskManager.getTaskById(1).getName(), "Возвращена неверная задача");
    }

    public void getTaskByIdWrongIdReturnNull(TaskManager taskManager) {
        assertThrows(NoSuchElementException.class, () -> taskManager.getTaskById(101));
    }

    public void getEpicByIdCorrectIdReturnEpicSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        assertEquals("EpicOne", taskManager.getEpicById(1).getName(), "Возвращена неверный эпик");
    }

    public void getEpicByIdWrongIdReturnNull(TaskManager taskManager) {
        assertThrows(NoSuchElementException.class, () -> taskManager.getEpicById(101));
    }

    public void getSubtaskByIdCorrectIdReturnSubtaskSuccess(TaskManager taskManager) {
        taskManager.addSubTask(subTaskInProgress1);
        assertEquals("SubTaskIP1", taskManager.getSubTaskById(1).getName(),
                "Имя задачи отображается неверно");
    }

    public void getSubtaskByIdWrongIdReturnNull(TaskManager taskManager) {
        assertThrows(NoSuchElementException.class, () -> taskManager.getTaskById(101));
    }

    public void addTaskWithCorrectTaskAdded(TaskManager taskManager) {
        taskManager.addTask(taskNew1);
        assertEquals(1, taskManager.getTasks().size(), "Задача не добавлена");
    }

    public void addNullTaskNotAdded(TaskManager taskManager) {
        taskManager.addTask(null);
        assertEquals(0, taskManager.getTasks().size(), "Задача null добавлена");
    }

    public void addEpicWithCorrectEpicAdded(TaskManager taskManager) {
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getEpics().size(), "Эпик не добавлен");
    }

    public void addNullEpicNotAdded(TaskManager taskManager) {
        taskManager.addEpic(null);
        assertEquals(0, taskManager.getEpics().size(), "Эпик null не добавлен");
    }

    public void addSubtaskWithCorrectSubtaskAdded(TaskManager taskManager) {
        taskManager.addSubTask(subTaskInProgress1);
        assertEquals(1, taskManager.getSubTasks().size(), "Подзадача не добавлена");
    }

    public void addNullSubtaskNotAdded(TaskManager taskManager) {
        taskManager.addSubTask(null);
        assertEquals(0, taskManager.getSubTasks().size(), "Подзадача null добавлена");
    }

    public void updateTaskWithCorrectDataSuccess(TaskManager taskManager) {
        taskManager.addTask(taskNew1);
        Task task2 = new Task("Updated", DONE, "upd");
        task2.setId(1);
        taskManager.updateTask(task2);
        assertEquals("Updated", taskManager.getTaskById(1).getName(), "Название задачи отображается неверно");
        assertEquals("upd", taskManager.getTaskById(1).getDescription(), "Описание задачи отображается неверно");
        assertEquals(DONE, taskManager.getTaskById(1).getStatus(), "Статус задачи отображается неверно");
    }

    public void updateTaskByTaskWithWrongIdNoSuchElementException(TaskManager taskManager) {
        taskManager.addTask(taskNew1);
        Task task2 = new Task("Updated", DONE, "upd");
        task2.setId(2);
        assertThrows(NoSuchElementException.class, () -> taskManager.updateTask(task2));
    }

    public void updateSubTaskWithCorrectDataSuccess(TaskManager taskManager) {
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

    public void updateSubTaskByTaskWithWrongIdNoSuchElementException(TaskManager taskManager) {
        taskManager.addSubTask(subTaskInProgress1);
        SubTask subTask2 = new SubTask("upd", NEW, "upd", 1);
        subTask2.setId(2);
        assertThrows(NoSuchElementException.class, () -> taskManager.updateSubtask(subTask2));
    }

    public void updateEpicWithCorrectDataSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        Epic epic2 = new Epic("upd", "upd");
        epic2.setId(1);
        taskManager.updateEpic(epic2);
        assertEquals("upd", taskManager.getEpicById(1).getName(), "Название эпика отображается неверно");
        assertEquals("upd", taskManager.getEpicById(1).getDescription(), "Описание эпика отображается неверно");
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика отображается неверно");
    }

    public void updateEpicByTaskWithWrongIdNoSuchElementException(TaskManager taskManager) {
        taskManager.addEpic(epic);
        Epic epic2 = new Epic("upd", "upd");
        epic2.setId(2);
        assertThrows(NoSuchElementException.class, () -> taskManager.updateEpic(epic2));
    }

    public void removeTaskCorrectIdSuccess(TaskManager taskManager) {
        taskManager.addTask(taskNew1);
        taskManager.removeTaskById(1);
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена");
    }

    public void removeTaskWrongIdNoSuchElementException(TaskManager taskManager) {
        assertThrows(NoSuchElementException.class, () -> taskManager.removeTaskById(1));
    }

    public void removeSubTaskCorrectIdSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.removeSubTaskById(2);
        assertEquals(0, taskManager.getSubTasks().size(), "Подзадача не удалена");
    }

    public void removeSubTaskWrongIdNoSuchElementException(TaskManager taskManager) {
        assertThrows(NoSuchElementException.class, () -> taskManager.removeSubTaskById(1));
    }

    public void removeEpicCorrectIdSuccess(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.removeEpicById(1);
        assertEquals(0, taskManager.getEpics().size(), "Эпик не удален");
    }

    public void removeEpicWrongIdNoSuchElementException(TaskManager taskManager) {
        assertThrows(NoSuchElementException.class, () -> taskManager.removeEpicById(1));
    }

    public void getEpicsSubtasksEpicWithoutSubtasksEmptyList(TaskManager taskManager) {
        taskManager.addEpic(epic);
        assertEquals(0, taskManager.getSubTasksByEpicId(1).size(), "Список подзадач не пуст");
    }

    public void getEpicsSubtasksEpicWithSubtasksSubtasksList(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskNew2);
        assertEquals(2, taskManager.getSubTasksByEpicId(1).size(), "Список подзадач пуст");
    }

    public void getEpicsSubtasksWrongEpicIdNoSuchElementException(TaskManager taskManager) {
        assertThrows(NoSuchElementException.class, () -> taskManager.getSubTasksByEpicId(0));
    }

    public void epicStatusUpdateAddInProgressSubTaskToNewEpicInProgress(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    public void epicStatusUpdateAddDoneSubTaskToNewEpicInProgress(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
        taskManager.addSubTask(subTaskDone2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    public void epicStatusUpdateAddDoneSubTaskToInProgressEpicInProgress(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskInProgress1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    public void epicStatusUpdateAddInProgressSubTaskToDoneEpicInProgress(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    public void epicStatusUpdateAddNewSubTaskToDoneEpicInProgress(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
        taskManager.addSubTask(subTaskNew2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    public void epicStatusUpdateAddDoneSubTaskToDoneEpicDone(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
    }

    public void epicStatusUpdateNewEpicSubTaskToInProgressEpicInProgress(TaskManager taskManager) {
        SubTask subTask3 = new SubTask("ST2", IN_PROGRESS, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskNew2);
        subTask3.setId(2);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
        taskManager.updateSubtask(subTask3);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    public void epicStatusUpdateInProgressEpicSubTaskToNewEpicInProgress(TaskManager taskManager) {
        SubTask subTask3 = new SubTask("ST2", NEW, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
        subTask3.setId(3);
        taskManager.updateSubtask(subTask3);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
    }

    public void epicStatusUpdateInProgressEpicSubTaskToDoneEpicDone(TaskManager taskManager) {
        SubTask subTask3 = new SubTask("ST2", DONE, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskInProgress2);
        subTask3.setId(3);
        taskManager.updateSubtask(subTask3);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
    }

    public void epicStatusUpdateDoneEpicSubTaskToInProgressEpicInProgress(TaskManager taskManager) {
        SubTask subTask3 = new SubTask("ST2", IN_PROGRESS, "one", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        subTask3.setId(3);
        taskManager.updateSubtask(subTask3);

        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    public void epicStatusRemoveDoneEpicSubTaskEpicDone(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        taskManager.removeSubTaskById(3);
        assertEquals(DONE, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть DONE");
    }

    public void epicStatusRemoveAllEpicSubTasksEpicNew(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskDone1);
        taskManager.addSubTask(subTaskDone2);
        taskManager.removeSubTaskById(2);
        taskManager.removeSubTaskById(3);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быть NEW");
    }

    public void epicStatusRemoveInProgressEpicSubTasksEpicNew(TaskManager taskManager) {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTaskNew1);
        taskManager.addSubTask(subTaskInProgress2);
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быт IN_PROGRESS");
        taskManager.removeSubTaskById(3);
        assertEquals(NEW, taskManager.getEpicById(1).getStatus(), "Статус эпика должен быт NEW");
    }

    public void epicNotContainsRemovedSubTasks(TaskManager taskManager) {
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