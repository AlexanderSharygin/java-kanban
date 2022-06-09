public class SubTask extends TaskBase {

    private final int epicId;

    public SubTask(String name, String description, int id, TaskStatus status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, null, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "Имя='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", id=" + id +
                ", Статус=" + status +
                ", ID эпика=" + epicId +
                '}';
    }
}
