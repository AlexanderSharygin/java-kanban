package HistoryManager;


import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node> tasks;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        this.tasks = new HashMap<>();
    }

    public List<Task> getTasks() {
        List<Task> items = new ArrayList<>();
        Node current = head;
        while (current != null) {
            items.add(current.value);
            current = current.after;
        }
        return items;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (tasks.containsKey(task.getId())) {
            Node oldNode = tasks.get(task.getId());
            removeNode(oldNode);
        }
        Node newNode = new Node(task);
        tasks.put(task.getId(), newNode);
        linkNodeAtEnd(newNode);
    }

    @Override
    public void remove(int id) {
        if (tasks.containsKey(id)) {
            Node oldNode = tasks.get(id);
            removeNode(oldNode);
            tasks.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkNodeAtEnd(Node newTail) {
        Node oldTail = tail;
        tail = newTail;
        if (oldTail == null)
            head = newTail;
        else {
            newTail.before = oldTail;
            oldTail.after = newTail;
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node before = node.before;
        Node after = node.after;
        if (before == null && after == null) {
            head = null;
            tail = null;
        } else if (before == null) {
            head = node.after;
            head.before = null;
        } else if (after == null) {
            tail.after = null;
            tail = node.before;
        } else {
            before.after = after;
            after.before = before;
        }
    }
}