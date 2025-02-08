package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtasksId;

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public Epic(String name, String description) {
        super(name, TaskStatus.NEW, description);
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, status, description);
        subtasksId = new ArrayList<>();
    }

    public void addSubtask(SubTask subtask) {
        if (!subtasksId.contains(subtask.getId())) {
            subtasksId.add(subtask.getId());
        }
    }

    @Override
    public String toString() {
        return "Epic{id=%d, name='%s', description='%s', status=%s, subTasks=%s}\n"
                .formatted(id, name, description, status.getStatus(), subtasksId.toString());
    }
}