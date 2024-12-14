package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    List<Integer> subtasksId;

    public Epic(String name, String description) {
        super(name, TaskStatus.NEW, description);
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