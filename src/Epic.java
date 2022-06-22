import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subtasksId = new ArrayList<>();
    }

    public void addSubtask(SubTask subtask) {
        if (!subtasksId.contains(subtask.getId())) {
            subtasksId.add(subtask.getId());
        }
    }

    public void removeSubtask(SubTask subTask) {
        for (int i = 0; i < subtasksId.size(); i++) {
            if (Objects.equals(subtasksId.get(i), subTask.id)) {
                subtasksId.remove(i);
                break;
            }
        }
    }

    public void clearSubtasks() {
        subtasksId.clear();
        status = TaskStatus.NEW;
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "Имя='" + name + '\'' +
                ", Описание:'" + description + '\'' +
                ", ID = " + id +
                ", Статус = " + status +
                ", ID Подзадач: " + subtasksId.toString() +
                '}';
    }
}