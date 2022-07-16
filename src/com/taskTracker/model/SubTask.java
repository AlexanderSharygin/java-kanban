package com.taskTracker.model;

import com.taskTracker.utils.TaskStatus;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "Имя='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", id=" + id +
                ", Статус=" + status +
                ", ID эпика=" + epicId +
                '}';
    }
}