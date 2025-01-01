package HistoryManager;


import task.Epic;
import task.SubTask;
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
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (tasks.size() >= 10) {
            tasks.removeFirst();
        }
        if (task instanceof Epic) {
            Epic epic = new Epic(task.getName(), task.getDescription());
            epic.setId(task.getId());
            tasks.add(epic);
        } else if (task instanceof SubTask) {
            SubTask subTask = new SubTask(task.getName(), task.getStatus(), task.getDescription(),
                    ((SubTask) task).getEpicId());
            subTask.setId(task.getId());
            tasks.add(subTask);
        } else {
            tasks.add(new Task(task.getName(), task.getStatus(), task.getDescription()));
        }
    }
}