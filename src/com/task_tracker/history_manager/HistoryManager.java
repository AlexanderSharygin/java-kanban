package com.task_tracker.history_manager;

import com.task_tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getTasks();
}