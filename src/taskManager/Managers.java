package taskManager;

import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;

public class Managers {

    public static TaskManger getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}