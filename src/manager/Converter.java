package manager;


import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.InputMismatchException;

import static task.TaskType.*;


public class Converter {


    public static Task taskFromString(String source) {
        String[] data = source.split(",");
        String taskType = data[1];
        String taskName = data[2];
        String taskDescription = data[4];
        TaskStatus status = null;
        for (TaskStatus item : TaskStatus.values())
            if (item.toString().equals(data[3])) {
                status = item;
                break;
            }
        if (status == null) {
            throw new InputMismatchException("В файле содержится неверный статус задачи. Id задачи " + data[0]);
        }
        if (taskType.equals(TASK.toString())) {
            Task task = new Task(taskName, status, taskDescription);
            task.setId(Integer.parseInt(data[0]));
            return task;
        } else if (taskType.equals(SUBTASK.toString())) {
            int epicId = Integer.parseInt(data[5]);
            SubTask subTask = new SubTask(taskName, status, taskDescription, epicId);
            subTask.setId(Integer.parseInt(data[0]));
            return subTask;
        } else if (taskType.equals(EPIC.toString())) {
            Epic epic = new Epic(taskName, taskDescription, status);
            epic.setId(Integer.parseInt(data[0]));
            return epic;
        } else {
            throw new InputMismatchException("В файле содержится неверный тип задачи " + data[0]);
        }
    }
}
