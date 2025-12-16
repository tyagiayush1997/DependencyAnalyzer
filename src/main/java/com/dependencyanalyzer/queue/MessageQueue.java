package com.dependencyanalyzer.queue;

import com.dependencyanalyzer.model.DependencyEvent;
import java.util.ArrayDeque;
import java.util.Queue;

public class MessageQueue {
    private final Queue<DependencyEvent> queue = new ArrayDeque<>();

    public MessageQueue() {
    }

    public void publish(DependencyEvent event) {
        queue.offer(event);
    }

    public DependencyEvent consume() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}