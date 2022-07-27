package com.taskTracker.model;

import com.taskTracker.utils.TaskStatus;

import java.util.ArrayList;
import java.util.Objects;

import static com.taskTracker.model.TaskType.*;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status, EPIC);
        subtasksId = new ArrayList<>();
    }

    public void addSubtask(SubTask subtask) {
        if (!subtasksId.contains(subtask.getId())) {
            subtasksId.add(subtask.getId());
        }
    }

    public void removeSubtask(SubTask subTask) {
        for (int i = 0; i < subtasksId.size(); i++) {
            if (Objects.equals(subtasksId.get(i), subTask.id)) {
                subtasksId.remove(i);
                break;
            }
        }
    }

    public void clearSubtasks() {
        subtasksId.clear();
        status = TaskStatus.NEW;
    }

    @Override
    public String toString() {
            return String.join(",", id.toString(), type.toString(), name, status.toString(),description);
    }
}