package task;

public class SubTask extends Task {
    public int getEpicId() {
        return epicId;
    }

    private final Integer epicId;

    public SubTask(String name, TaskStatus status, String description, int epicId) {
        super(name, status, description);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{id=%d, name='%s', description='%s', status=%s, epicId=%s}\n"
                .formatted(id, name, description, status.getStatus(), epicId);
    }
}