package com.taskTracker.model;

import com.taskTracker.utils.TaskStatus;

import static com.taskTracker.model.TaskType.TASK;

public class Task {

    protected String name;
    protected String description;
    protected Integer id;
    protected TaskStatus status;

    public TaskType getType() {
        return type;
    }

    protected TaskType type;

    public Task(String name, String description, TaskStatus status, TaskType type) {
        this.name = name;
        this.description = description;
        this.id = null;
        this.status = status;
        this.type = type;
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = null;
        this.status = status;
        this.type = TASK;
    }

    @Override
    public String toString() {
        return String.join(",", id.toString(), type.toString(), name, status.toString(), description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != null) {
            hash = hash + id.hashCode();
        }
        hash = hash * 31;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        return hash;
    }
}