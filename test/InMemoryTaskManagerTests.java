import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class InMemoryTaskManagerTests extends TaskManagerTestsLogic {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void epicWithoutSubtaskHasNewStatusSuccessTest() {
        super.epicWithoutSubtaskHasNewStatusSuccessTest(taskManager);
    }


    @Test
    public void epicWithSeveralNewSubtaskHasNewStatusSuccess() {
        super.epicWithSeveralNewSubtaskHasNewStatusSuccess(taskManager);
    }

    @Test
    public void epicWithSeveralDoneSubtaskHasDoneStatusSuccess() {
        super.epicWithSeveralDoneSubtaskHasDoneStatusSuccess(taskManager);
    }

    @Test
    public void epicWithNewAndDoneSubtaskHasInNewStatusSuccess() {
        super.epicWithNewAndDoneSubtaskHasInNewStatusSuccess(taskManager);
    }


    @Test
    public void epicWithSeveralInProgressSubtaskHasInProgressStatusSuccess() {
        super.epicWithSeveralInProgressSubtaskHasInProgressStatusSuccess(taskManager);
    }

    @Test
    public void getTasksReturnListOfAllTasksSuccess() {
        super.getTasksReturnListOfAllTasksSuccess(taskManager);
    }

    @Test
    public void getTasksReturnEmptyListForManagerWithoutTasksSuccess() {
        super.getTasksReturnEmptyListForManagerWithoutTasksSuccess(taskManager);
    }

    @Test
    public void getEpicsReturnListOfAllEpicsSuccess() {
        super.getEpicsReturnListOfAllEpicsSuccess(taskManager);
    }

    @Test
    public void getEpicsReturnEmptyListForManagerWithoutEpicsSuccess() {
        super.getEpicsReturnEmptyListForManagerWithoutEpicsSuccess(taskManager);
    }

    @Test
    public void getSubTasksReturnListOfAllSubTasksSuccess() {
        super.getSubTasksReturnListOfAllSubTasksSuccess(taskManager);
    }

    @Test
    public void getSubtasksReturnEmptyListForManagerWithoutSubtasksSuccess() {
        super.getSubtasksReturnEmptyListForManagerWithoutSubtasksSuccess(taskManager);
    }

    @Test
    public void removeAllTasksSuccess() {
        super.removeAllTasksSuccess(taskManager);
    }

    @Test
    public void removeAllSubTasksSuccess() {
        super.removeAllSubTasksSuccess(taskManager);
    }

    @Test
    public void removeAllEpicsSuccess() {
        super.removeAllEpicsSuccess(taskManager);
    }

    @Test
    public void getTaskByIdCorrectIdReturnTaskSuccess() {
        super.getTaskByIdCorrectIdReturnTaskSuccess(taskManager);
    }

    @Test
    public void getTaskByIdWrongIdReturnNull() {
        super.getTaskByIdWrongIdReturnNull(taskManager);
    }

    @Test
    public void getEpicByIdCorrectIdReturnEpicSuccess() {
        super.getEpicByIdCorrectIdReturnEpicSuccess(taskManager);
    }

    @Test
    public void getEpicByIdWrongIdReturnNull() {
        super.getEpicByIdWrongIdReturnNull(taskManager);
    }

    @Test
    public void getSubtaskByIdCorrectIdReturnSubtaskSuccess() {
        super.getSubtaskByIdCorrectIdReturnSubtaskSuccess(taskManager);
    }

    @Test
    public void getSubtaskByIdWrongIdReturnNull() {
        super.getSubtaskByIdWrongIdReturnNull(taskManager);
    }

    @Test
    public void addTaskWithCorrectTaskAdded() {
        super.addTaskWithCorrectTaskAdded(taskManager);
    }

    @Test
    public void addNullTaskNotAdded() {
        super.addNullTaskNotAdded(taskManager);
    }

    @Test
    public void addEpicWithCorrectEpicAdded() {
        super.addEpicWithCorrectEpicAdded(taskManager);
    }

    @Test
    public void addNullEpicNotAdded() {
        super.addNullEpicNotAdded(taskManager);
    }

    @Test
    public void addSubtaskWithCorrectSubtaskAdded() {
        super.addSubtaskWithCorrectSubtaskAdded(taskManager);
    }

    @Test
    public void addNullSubtaskNotAdded() {
        super.addNullSubtaskNotAdded(taskManager);
    }

    @Test
    public void updateTaskWithCorrectDataSuccess() {
        super.updateTaskWithCorrectDataSuccess(taskManager);
    }

    @Test
    public void updateTaskByTaskWithWrongIdNoSuchElementException() {
        super.updateTaskByTaskWithWrongIdNoSuchElementException(taskManager);
    }

    @Test
    public void updateSubTaskWithCorrectDataSuccess() {
        super.updateSubTaskWithCorrectDataSuccess(taskManager);
    }

    @Test
    public void updateSubTaskByTaskWithWrongIdNoSuchElementException() {
        super.updateSubTaskByTaskWithWrongIdNoSuchElementException(taskManager);
    }

    @Test
    public void updateEpicWithCorrectDataSuccess() {
        super.updateEpicWithCorrectDataSuccess(taskManager);
    }

    @Test
    public void updateEpicByTaskWithWrongIdNoSuchElementException() {
        super.updateEpicByTaskWithWrongIdNoSuchElementException(taskManager);
    }

    @Test
    public void removeTaskCorrectIdSuccess() {
        super.removeTaskCorrectIdSuccess(taskManager);
    }

    @Test
    public void removeTaskWrongIdNoSuchElementException() {
        super.removeTaskWrongIdNoSuchElementException(taskManager);
    }

    @Test
    public void removeSubTaskCorrectIdSuccess() {
        super.removeSubTaskCorrectIdSuccess(taskManager);
    }

    @Test
    public void removeSubTaskWrongIdNoSuchElementException() {
        super.removeSubTaskWrongIdNoSuchElementException(taskManager);
    }

    @Test
    public void removeEpicCorrectIdSuccess() {
        super.removeEpicCorrectIdSuccess(taskManager);
    }

    @Test
    public void removeEpicWrongIdNoSuchElementException() {
        super.removeEpicWrongIdNoSuchElementException(taskManager);
    }

    @Test
    public void getEpicsSubtasksEpicWithoutSubtasksEmptyList() {
        super.getEpicsSubtasksEpicWithoutSubtasksEmptyList(taskManager);
    }

    @Test
    public void getEpicsSubtasksEpicWithSubtasksSubtasksList() {
        super.getEpicsSubtasksEpicWithSubtasksSubtasksList(taskManager);
    }


    @Test
    public void getEpicsSubtasksWrongEpicIdNoSuchElementException() {
        super.getEpicsSubtasksWrongEpicIdNoSuchElementException(taskManager);
    }

    @Test
    public void epicStatusUpdateAddInProgressSubTaskToNewEpicInProgress() {
        super.epicStatusUpdateAddInProgressSubTaskToNewEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusUpdateAddDoneSubTaskToNewEpicInProgress() {
        super.epicStatusUpdateAddDoneSubTaskToNewEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusUpdateAddDoneSubTaskToInProgressEpicInProgress() {
        super.epicStatusUpdateAddDoneSubTaskToInProgressEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusUpdateAddInProgressSubTaskToDoneEpicInProgress() {
        super.epicStatusUpdateAddInProgressSubTaskToDoneEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusUpdateAddNewSubTaskToDoneEpicInProgress() {
        super.epicStatusUpdateAddNewSubTaskToDoneEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusUpdateAddDoneSubTaskToDoneEpicDone() {
        super.epicStatusUpdateAddDoneSubTaskToDoneEpicDone(taskManager);
    }

    @Test
    public void epicStatusUpdateNewEpicSubTaskToInProgressEpicInProgress() {
        super.epicStatusUpdateNewEpicSubTaskToInProgressEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusUpdateInProgressEpicSubTaskToNewEpicInProgress() {
        super.epicStatusUpdateInProgressEpicSubTaskToNewEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusUpdateInProgressEpicSubTaskToDoneEpicDone() {
        super.epicStatusUpdateInProgressEpicSubTaskToDoneEpicDone(taskManager);
    }

    @Test
    public void epicStatusUpdateDoneEpicSubTaskToInProgressEpicInProgress() {
        super.epicStatusUpdateDoneEpicSubTaskToInProgressEpicInProgress(taskManager);
    }

    @Test
    public void epicStatusRemoveDoneEpicSubTaskEpicDone() {
        super.epicStatusRemoveDoneEpicSubTaskEpicDone(taskManager);
    }

    @Test
    public void epicStatusRemoveAllEpicSubTasksEpicNew() {
        super.epicStatusRemoveAllEpicSubTasksEpicNew(taskManager);
    }

    @Test
    public void epicStatusRemoveInProgressEpicSubTasksEpicNew() {
        super.epicStatusRemoveInProgressEpicSubTasksEpicNew(taskManager);
    }

    @Test
    public void epicNotContainsRemovedSubTasks() {
        super.epicNotContainsRemovedSubTasks(taskManager);
    }
}