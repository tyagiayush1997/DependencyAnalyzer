package com.dependencyanalyzer.service;

import com.dependencyanalyzer.model.DependencyEvent;
import com.dependencyanalyzer.queue.MessageQueue;

/**
 * Service responsible for publishing dependency events to the message queue.
 * This class provides methods to create and publish dependency events,
 * acting as a producer in the producer-consumer pattern for event processing.
 * 
 * The publisher encapsulates the logic for creating properly formatted
 * dependency events and adding them to the queue for later processing.
 */
public class EventPublisher {
    /** The message queue where events will be published */
    private final MessageQueue messageQueue;

    /**
     * Creates a new event publisher that will send events to the specified queue.
     * 
     * @param messageQueue the message queue to publish events to
     */
    public EventPublisher(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Creates and publishes a dependency event with the specified parameters.
     * This convenience method creates a dependency event with type "dependency"
     * and adds it to the message queue.
     * 
     * @param source the service that has the dependency
     * @param target the service being depended upon
     * @param latencyMs the latency in milliseconds for this dependency
     */
    public void publishDependencyEvent(String source, String target, int latencyMs) {
        DependencyEvent event = new DependencyEvent("dependency", source, target, latencyMs);
        messageQueue.publish(event);
    }

    /**
     * Publishes an already-created dependency event to the queue.
     * This method allows for publishing custom dependency events.
     * 
     * @param event the dependency event to publish
     */
    public void publishEvent(DependencyEvent event) {
        messageQueue.publish(event);
    }

    /**
     * Gets the current size of the message queue.
     * 
     * @return the number of events currently in the queue
     */
    public int getQueueSize() {
        return messageQueue.size();
    }

    /**
     * Checks if the message queue is empty.
     * 
     * @return true if the queue contains no events, false otherwise
     */
    public boolean isQueueEmpty() {
        return messageQueue.isEmpty();
    }
}