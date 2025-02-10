package history.manager;

import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager loadFromFile(String filePath) {
        return new FileBackedTaskManager(filePath);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}