package com.dependencyanalyzer.model;

/**
 * Represents a dependency event between two services in a microservice architecture.
 * This immutable class encapsulates information about a service dependency including
 * the source service, target service, and the latency between them.
 * 
 * Events are used to model relationships in a service dependency graph where
 * one service depends on another service to fulfill its operations.
 */
public class DependencyEvent {
    /** The type of dependency event (e.g., "dependency") */
    private final String type;
    /** The name of the service that depends on another service */
    private final String source;
    /** The name of the service that is being depended upon */
    private final String target;
    /** The latency in milliseconds for this dependency relationship */
    private final int latencyMs;

    /**
     * Creates a new dependency event with the specified parameters.
     * 
     * @param type the type of event (typically "dependency")
     * @param source the service that has the dependency
     * @param target the service being depended upon
     * @param latencyMs the latency in milliseconds for this dependency
     */
    public DependencyEvent(String type, String source, String target, int latencyMs) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.latencyMs = latencyMs;
    }

    /**
     * Gets the type of this dependency event.
     * 
     * @return the event type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the source service name that has the dependency.
     * 
     * @return the source service name
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets the target service name that is being depended upon.
     * 
     * @return the target service name
     */
    public String getTarget() {
        return target;
    }

    /**
     * Gets the latency in milliseconds for this dependency.
     * 
     * @return the latency in milliseconds
     */
    public int getLatencyMs() {
        return latencyMs;
    }

    /**
     * Returns a JSON-formatted string representation of this dependency event.
     * 
     * @return a JSON string containing all event fields
     */
    @Override
    public String toString() {
        return String.format("{\"type\": \"%s\", \"source\": \"%s\", \"target\": \"%s\", \"latency_ms\": %d}", 
                           type, source, target, latencyMs);
    }

    /**
     * Checks if this dependency event is equal to another object.
     * Two events are considered equal if all their fields match.
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DependencyEvent event = (DependencyEvent) obj;
        return latencyMs == event.latencyMs &&
               type.equals(event.type) &&
               source.equals(event.source) &&
               target.equals(event.target);
    }

    /**
     * Generates a hash code for this dependency event based on all fields.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, source, target, latencyMs);
    }
}