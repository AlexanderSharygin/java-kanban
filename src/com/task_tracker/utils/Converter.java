package com.task_tracker.utils;

import com.task_tracker.history_manager.HistoryManager;
import com.task_tracker.model.Epic;
import com.task_tracker.model.SubTask;
import com.task_tracker.model.Task;
import com.task_tracker.model.TaskStatus;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

import static com.task_tracker.model.TaskType.*;

public class Converter {

    public static String historyToString(HistoryManager manager) {
        List<String> history = manager.getTasks().stream()
                .map(k -> k.getId().toString())
                .collect(Collectors.toList());
        return String.join(",", history);
    }

    public static List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

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
            throw new InputMismatchException("Invalid task status presents in the history file");
        }
        if (taskType.equals(TASK.toString())) {
            Task task = new Task(taskName, taskDescription, status, TASK);
            task.setId(Integer.parseInt(data[0]));
            return task;
        } else if (taskType.equals(SUBTASK.toString())) {
            int epicId = Integer.parseInt(data[5]);
            SubTask subTask = new SubTask(taskName, taskDescription, status, epicId);
            subTask.setId(Integer.parseInt(data[0]));
            return subTask;
        } else if (taskType.equals(EPIC.toString())) {
            Epic epic = new Epic(taskName, taskDescription, status);
            epic.setId(Integer.parseInt(data[0]));
            return epic;
        } else {
            throw new InputMismatchException("Invalid history file");
        }
    }
}
