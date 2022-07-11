import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<Task>> historyNodes;
    private Node<Task> first;
    private Node<Task> last;

    public InMemoryHistoryManager() {

        this.historyNodes = new HashMap<>();
    }

    void linkLast(Node<Task> newNode) {
        Node<Task> oldLastNode = last;
        newNode.next = null;
        newNode.prev = oldLastNode;
        last = newNode;
        if (oldLastNode == null)
            first = newNode;
        else
            oldLastNode.next = newNode;
    }

    void removeNode(Node<Task> node) {
        Node<Task> nodeBefore = node.prev;
        Node<Task> nodeAfter = node.next;
        if (nodeBefore == null && nodeAfter == null) {
            first = null;
            last = null;
        } else if (nodeBefore == null) {
            first = nodeAfter;
            first.prev = null;
        } else if (nodeAfter == null) {
            last = nodeBefore;
            last.next = null;
        } else {
            nodeBefore.next = nodeAfter;
            nodeAfter.prev = nodeBefore;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        Node<Task> temp = first;
        while (temp != null) {
            tasksList.add(temp.data);
            temp = temp.next;
        }
        return tasksList;
    }

    @Override
    public void add(Task task) {
        if (!historyNodes.containsKey(task.id)) {
            Node<Task> node = new Node<>(task);
            historyNodes.put(task.id, node);
            linkLast(node);
        } else {
            Node<Task> oldNode = historyNodes.get(task.id);
            removeNode(oldNode);
            Node<Task> node = new Node<>(task);
            historyNodes.put(task.id, node);
            linkLast(node);
        }
    }

    @Override
    public void remove(int id) {
       if(historyNodes.containsKey(id)) {
           Node<Task> oldNode = historyNodes.get(id);
           removeNode(oldNode);
           historyNodes.remove(id);
       }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
