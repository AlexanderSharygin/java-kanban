package com.task_tracker.history_manager;

import com.task_tracker.model.Task;

public class Node {

    public Task data;
    public Node next;
    public Node prev;

    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}