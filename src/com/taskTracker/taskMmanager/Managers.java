package com.taskTracker.taskMmanager;

import com.taskTracker.historyManager.FileBackedHistoryManager;
import com.taskTracker.historyManager.HistoryManager;
import com.taskTracker.historyManager.InMemoryHistoryManager;

import java.util.List;
import java.util.stream.Collectors;

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

    public static String toString(HistoryManager manager)
    {
        List<String> history = manager.getTasks().stream().
                map(k -> k.getId().toString()).collect(Collectors.toList());
        return String.join(",", history);
    }
}