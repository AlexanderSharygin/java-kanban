package history.manager;


import task.Task;

public class Node {

    public Task value;
    public Node after;
    public Node before;

    public Node(Task task) {
        this.value = task;
        this.after = null;
        this.before = null;
    }
}