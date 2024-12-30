package HistoryManager;


import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasks;

    public InMemoryHistoryManager() {
        this.tasks = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return tasks.reversed();
    }

    @Override
    public void add(Task task) {
        if (tasks.size() < 10) {
            tasks.add(task);
        } else {
            tasks.removeFirst();
            tasks.add(task);
        }
    }
}