package com.task_tracker.task_manager;

import com.task_tracker.history_manager.FileBackedHistoryManager;
import com.task_tracker.history_manager.HistoryManager;
import com.task_tracker.history_manager.InMemoryHistoryManager;

public class Managers {

    public static TaskManger getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HistoryManager getFileBackedHistoryManager() {
        return new FileBackedHistoryManager();
    }
}