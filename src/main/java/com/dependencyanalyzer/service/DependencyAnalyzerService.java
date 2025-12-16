package com.dependencyanalyzer.service;

import com.dependencyanalyzer.graph.ServiceGraph;
import com.dependencyanalyzer.queue.MessageQueue;
import java.util.Set;

/**
 * Main service class that orchestrates dependency analysis operations.
 * This facade class provides a simplified interface to the dependency analyzer system,
 * coordinating between the message queue, service graph, event publisher, and event consumer.
 * 
 * Key responsibilities:
 * - Publishing dependency events to the queue
 * - Processing queued events and updating the service graph
 * - Querying the service graph for reachability analysis
 * - Managing system state and metrics
 */
public class DependencyAnalyzerService {
    /** Message queue for handling dependency events */
    private final MessageQueue messageQueue;
    /** Service graph for storing and querying service dependencies */
    private final ServiceGraph serviceGraph;
    /** Publisher for adding events to the queue */
    private final EventPublisher eventPublisher;
    /** Consumer for processing events from the queue and updating the graph */
    private final EventConsumer eventConsumer;

    /**
     * Creates a new dependency analyzer service with initialized components.
     * Sets up the message queue, service graph, event publisher, and event consumer.
     */
    public DependencyAnalyzerService() {
        this.messageQueue = new MessageQueue();
        this.serviceGraph = new ServiceGraph();
        this.eventPublisher = new EventPublisher(messageQueue);
        this.eventConsumer = new EventConsumer(messageQueue, serviceGraph);
    }

    /**
     * Publishes a new dependency event to the message queue.
     * The event will be processed later when processAllQueuedEvents() is called.
     * 
     * @param source the service that has the dependency
     * @param target the service being depended upon
     * @param latencyMs the latency in milliseconds for this dependency
     */
    public void publishDependencyEvent(String source, String target, int latencyMs) {
        eventPublisher.publishDependencyEvent(source, target, latencyMs);
    }

    /**
     * Processes all pending events in the message queue.
     * Each event will be consumed and used to update the service graph.
     * This method blocks until all queued events are processed.
     */
    public void processAllQueuedEvents() {
        eventConsumer.processAllEvents();
    }

    /**
     * Gets all services reachable from the specified service.
     * Uses depth-first search to find transitive dependencies.
     * 
     * @param serviceName the name of the starting service
     * @return a set of service names reachable from the given service
     */
    public Set<String> getReachableServices(String serviceName) {
        return serviceGraph.getReachableServices(serviceName);
    }

    /**
     * Gets all services currently known to the system.
     * 
     * @return a set containing all service names in the graph
     */
    public Set<String> getAllServices() {
        return serviceGraph.getAllServices();
    }

    /**
     * Checks if a service exists in the dependency graph.
     * 
     * @param serviceName the name of the service to check
     * @return true if the service exists, false otherwise
     */
    public boolean hasService(String serviceName) {
        return serviceGraph.hasService(serviceName);
    }

    /**
     * Gets the current number of unprocessed events in the queue.
     * 
     * @return the number of events waiting to be processed
     */
    public int getQueueSize() {
        return messageQueue.size();
    }

    /**
     * Gets the total number of events that have been processed since startup.
     * 
     * @return the total count of processed events
     */
    public int getProcessedEventCount() {
        return eventConsumer.getProcessedEventCount();
    }

    /**
     * Clears the service graph and resets the processed event counter.
     * The message queue is not affected by this operation.
     */
    public void clearGraph() {
        serviceGraph.clear();
        eventConsumer.resetCounter();
    }

    /**
     * Gets the event publisher instance for advanced usage.
     * 
     * @return the event publisher
     */
    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }

    /**
     * Gets the event consumer instance for advanced usage.
     * 
     * @return the event consumer
     */
    public EventConsumer getEventConsumer() {
        return eventConsumer;
    }

    /**
     * Gets the service graph instance for direct access.
     * 
     * @return the service graph
     */
    public ServiceGraph getServiceGraph() {
        return serviceGraph;
    }
}