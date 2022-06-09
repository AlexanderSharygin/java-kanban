
public class Task extends TaskBase{

    public Task(String name, String description, TaskStatus status) {
      super(name, description, null, status);
    }

    public Task(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status);
    }

    @Override
    public String toString() {
        return "Задача{" +
                "Имя='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", id=" + id +
                ", Статус=" + status +
                '}';
    }
}