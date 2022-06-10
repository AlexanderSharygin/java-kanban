
public class Task {

    protected String name;
    protected String description;

    public void setId(Integer id) {
        this.id = id;
    }

    protected Integer id;
    protected TaskStatus status;


    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = null;
        this.status = status;
    }

    public void update(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
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


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != null) {
            hash = hash + id.hashCode();
        }
        hash = hash * 31;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        return hash;
    }
}