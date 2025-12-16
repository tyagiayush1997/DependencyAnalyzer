package com.dependencyanalyzer.graph;

import java.util.*;

public class ServiceGraph {
    private final Map<String, Set<String>> adjacencyList = new HashMap<>();

    public ServiceGraph() {
    }

    /**
     * Adds a dependency relationship between two services.
     * Creates a directed edge from source to target in the graph.
     * Both services are added to the graph if they don't already exist.
     * 
     * @param source the service that depends on the target
     * @param target the service being depended upon
     */
    public void addDependency(String source, String target) {
        adjacencyList.computeIfAbsent(source, k -> new HashSet<>()).add(target);
        adjacencyList.computeIfAbsent(target, k -> new HashSet<>());
    }

    /**
     * Finds all services that are reachable from the given service.
     * Uses depth-first search (DFS) to traverse the dependency graph.
     * The starting service itself is excluded from the result.
     * 
     * @param serviceName the name of the starting service
     * @return a set of service names reachable from the given service,
     *         or an empty set if the service doesn't exist
     */
    public Set<String> getReachableServices(String serviceName) {
        if (!adjacencyList.containsKey(serviceName)) {
            return Collections.emptySet();
        }

        Set<String> reachable = new HashSet<>();
        Set<String> visited = new HashSet<>();
        dfs(serviceName, visited, reachable);
        
        reachable.remove(serviceName);
        return reachable;
    }

    /**
     * Performs depth-first search traversal starting from the current service.
     * Recursively visits all reachable services and adds them to the reachable set.
     * Uses visited set to prevent infinite loops in case of cycles.
     * 
     * @param current the current service being visited
     * @param visited set of services already visited (prevents cycles)
     * @param reachable set to collect all reachable services
     */
    private void dfs(String current, Set<String> visited, Set<String> reachable) {
        if (visited.contains(current)) {
            return;
        }
        
        visited.add(current);
        reachable.add(current);
        
        Set<String> neighbors = adjacencyList.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                dfs(neighbor, visited, reachable);
            }
        }
    }

    /**
     * Returns all services currently in the graph.
     * 
     * @return a new set containing all service names in the graph
     */
    public Set<String> getAllServices() {
        return new HashSet<>(adjacencyList.keySet());
    }

    /**
     * Returns a deep copy of the adjacency list representation of the graph.
     * This method provides read-only access to the internal graph structure.
     * 
     * @return a new map containing a copy of the adjacency list
     */
    public Map<String, Set<String>> getAdjacencyList() {
        Map<String, Set<String>> copy = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Checks if a service exists in the graph.
     * 
     * @param serviceName the name of the service to check
     * @return true if the service exists in the graph, false otherwise
     */
    public boolean hasService(String serviceName) {
        return adjacencyList.containsKey(serviceName);
    }

    /**
     * Removes all services and dependencies from the graph.
     */
    public void clear() {
        adjacencyList.clear();
    }
}