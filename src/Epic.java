import java.util.ArrayList;
import java.util.Objects;

public class Epic extends TaskBase {
    private final ArrayList<Integer> subtasks;

    public Epic(String name, String description, int id) {
        super(name, description, id, TaskStatus.NEW);
        subtasks = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description, null, TaskStatus.NEW);
        subtasks = new ArrayList<>();
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addSubtask(SubTask subtask) {
        if (!subtasks.contains(subtask.getId())) {
            subtasks.add(subtask.getId());
        }
    }

    public void removeSubtask(SubTask subTask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (Objects.equals(subtasks.get(i), subTask.id)) {
                subtasks.remove(i);
                break;
            }
        }
    }

    public void clearSubtasks() {
        subtasks.clear();
        status = TaskStatus.NEW;
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "Имя='" + name + '\'' +
                ", Описание:'" + description + '\'' +
                ", ID = " + id +
                ", Статус = " + status +
                ", ID Подзадач: " + subtasks.toString() +
                '}';
    }
}