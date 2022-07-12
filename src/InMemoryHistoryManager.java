import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> tasksSet;
    private Node first;
    private Node last;

    public InMemoryHistoryManager() {

        this.tasksSet = new HashMap<>();
    }

    void linkLast(Node newLastNode) {
        Node oldLastNode = last;
        newLastNode.next = null;
        newLastNode.prev = oldLastNode;
        last = newLastNode;
        if (oldLastNode == null)
            first = newLastNode;
        else
            oldLastNode.next = newLastNode;
    }

    void removeNode(Node node) {
        Node before = node.prev;
        Node after = node.next;
        if (before == null && after == null) {
            first = null;
            last = null;
        } else if (before == null) {
            first = after;
            first.prev = null;
        } else if (after == null) {
            last = before;
            last.next = null;
        } else {
            before.next = after;
            after.prev = before;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasksList.add(node.data);
            node = node.next;
        }
        return tasksList;
    }

    @Override
    public void add(Task task) {
        if (!tasksSet.containsKey(task.id)) {
            Node node = new Node(task);
            tasksSet.put(task.id, node);
            linkLast(node);
        } else {
            Node oldNode = tasksSet.get(task.id);
            removeNode(oldNode);
            Node node = new Node(task);
            tasksSet.put(task.id, node);
            linkLast(node);
        }
    }

    @Override
    public void remove(int id) {
        if (tasksSet.containsKey(id)) {
            Node oldNode = tasksSet.get(id);
            removeNode(oldNode);
            tasksSet.remove(id);
        }
    }

    @Override
    public List<Task> getTasksSet() {
        return getTasks();
    }

    static class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}