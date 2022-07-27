package com.taskTracker.model;

import com.taskTracker.utils.TaskStatus;
import com.taskTracker.utils.TaskType;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status, TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.join(",", id.toString(), type.toString(), name, status.toString(), description,
                String.valueOf(epicId));
    }
}