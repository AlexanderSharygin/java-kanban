package com.task_tracker.history_manager;

import com.task_tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

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
        if (oldLastNode == null) {
            first = newLastNode;
        } else {
            oldLastNode.next = newLastNode;
        }
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

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (!tasksSet.containsKey(task.getId())) {
            Node node = new Node(task);
            tasksSet.put(task.getId(), node);
            linkLast(node);
        } else {
            Node oldNode = tasksSet.get(task.getId());
            removeNode(oldNode);
            Node node = new Node(task);
            tasksSet.put(task.getId(), node);
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
}