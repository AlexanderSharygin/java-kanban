package com.taskTracker.taskMmanager;

import com.taskTracker.historyManager.HistoryManager;
import com.taskTracker.historyManager.InMemoryHistoryManager;

public class Managers {

    public static TaskManger getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}