public class TaskBase {
    protected String name;
    protected String description;
    protected Integer id;
    protected TaskStatus status;

    public TaskBase(String name, String description, Integer id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public void update(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
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
        if (!(o instanceof TaskBase)) return false;
        TaskBase taskBase = (TaskBase) o;
        return id.equals(taskBase.id);
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
