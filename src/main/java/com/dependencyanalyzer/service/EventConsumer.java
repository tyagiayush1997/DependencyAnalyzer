package com.dependencyanalyzer.service;

import com.dependencyanalyzer.model.DependencyEvent;
import com.dependencyanalyzer.queue.MessageQueue;
import com.dependencyanalyzer.graph.ServiceGraph;

/**
 * Service responsible for consuming dependency events from the message queue
 * and updating the service graph accordingly. This class acts as the consumer
 * in the producer-consumer pattern for event processing.
 * 
 * The consumer processes events by:
 * - Polling events from the message queue
 * - Interpreting the event data
 * - Updating the service graph with new dependencies
 * - Tracking processing metrics
 */
public class EventConsumer {
    /** The message queue to consume events from */
    private final MessageQueue messageQueue;
    /** The service graph to update with dependency information */
    private final ServiceGraph serviceGraph;
    /** Counter tracking the total number of events processed */
    private int processedEventCount;

    /**
     * Creates a new event consumer that will process events from the specified
     * queue and update the specified service graph.
     * 
     * @param messageQueue the message queue to consume events from
     * @param serviceGraph the service graph to update with dependencies
     */
    public EventConsumer(MessageQueue messageQueue, ServiceGraph serviceGraph) {
        this.messageQueue = messageQueue;
        this.serviceGraph = serviceGraph;
        this.processedEventCount = 0;
    }

    /**
     * Consumes a single event from the queue and processes it.
     * If an event is available, it will be processed and the counter incremented.
     * 
     * @return the consumed event, or null if the queue is empty
     */
    public DependencyEvent consumeEvent() {
        DependencyEvent event = messageQueue.consume();
        if (event != null) {
            processEvent(event);
            processedEventCount++;
        }
        return event;
    }

    /**
     * Processes all available events in the queue.
     * This method will continue consuming and processing events until
     * the queue is empty. It blocks until all events are processed.
     */
    public void processAllEvents() {
        DependencyEvent event;
        while ((event = messageQueue.consume()) != null) {
            processEvent(event);
            processedEventCount++;
        }
    }

    /**
     * Processes a single dependency event by updating the service graph.
     * Currently handles events of type "dependency" by adding the dependency
     * relationship to the graph.
     * 
     * @param event the dependency event to process
     */
    private void processEvent(DependencyEvent event) {
        if ("dependency".equals(event.getType())) {
            serviceGraph.addDependency(event.getSource(), event.getTarget());
        }
    }

    /**
     * Gets the total number of events processed by this consumer.
     * 
     * @return the count of events processed since creation or last reset
     */
    public int getProcessedEventCount() {
        return processedEventCount;
    }

    /**
     * Checks if there are more events available in the queue.
     * 
     * @return true if the queue has events waiting to be processed, false otherwise
     */
    public boolean hasMoreEvents() {
        return !messageQueue.isEmpty();
    }

    /**
     * Resets the processed event counter to zero.
     * This is useful when clearing the system state.
     */
    public void resetCounter() {
        processedEventCount = 0;
    }
}