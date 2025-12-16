package com.dependencyanalyzer;

import com.dependencyanalyzer.service.DependencyAnalyzerService;
import java.util.Scanner;
import java.util.Set;

/**
 * Main entry point for the Dependency Analyzer application.
 * This class provides both demo and interactive modes for exploring
 * service dependency analysis capabilities.
 * 
 * The application supports:
 * - Demo mode: Runs with pre-loaded sample data to demonstrate functionality
 * - Interactive mode: Provides a command-line interface for real-time interaction
 * 
 * Usage:
 * - Run with "demo" argument for demo mode
 * - Run without arguments for interactive mode
 */
public class DependencyAnalyzerMain {
    /** The dependency analyzer service instance used throughout the application */
    private static final DependencyAnalyzerService analyzer = new DependencyAnalyzerService();

    /**
     * Main method that determines which mode to run based on command line arguments.
     * 
     * @param args command line arguments; pass "demo" for demo mode, otherwise interactive mode runs
     */
    public static void main(String[] args) {
        if (args.length > 0 && "demo".equals(args[0])) {
            runDemo();
        } else {
            runInteractiveMode();
        }
    }

    /**
     * Runs the application in demo mode with pre-loaded sample data.
     * Demonstrates the core functionality of the dependency analyzer by:
     * 1. Loading a sample dataset of service dependencies
     * 2. Processing all events to build the service graph
     * 3. Showing all services in the graph
     * 4. Testing reachability analysis for various services
     */
    public static void runDemo() {
        System.out.println("=== Dependency Analyzer Demo ===\n");
        
        System.out.println("Loading sample dataset...");
        loadSampleDataset();
        
        System.out.println("Processing events from message queue...");
        analyzer.processAllQueuedEvents();
        System.out.println("Processed " + analyzer.getProcessedEventCount() + " events\n");
        
        System.out.println("All services in the graph: " + analyzer.getAllServices() + "\n");
        
        System.out.println("=== Testing getReachableServices ===");
        String[] testServices = {"A", "F", "B", "G", "E"};
        
        for (String service : testServices) {
            if (analyzer.hasService(service)) {
                Set<String> reachable = analyzer.getReachableServices(service);
                System.out.printf("Reachable services from '%s': %s%n", service, reachable);
            } else {
                System.out.printf("Service '%s' not found in graph%n", service);
            }
        }
    }

    /**
     * Runs the application in interactive mode, providing a command-line interface
     * for users to interact with the dependency analyzer. Supports commands for:
     * - Adding dependencies
     * - Processing queued events
     * - Querying reachable services
     * - Viewing system status
     * - Loading demo data
     * - Clearing the graph
     */
    public static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Dependency Analyzer Interactive Mode ===");
        System.out.println("Commands:");
        System.out.println("  add <source> <target> <latency> - Add a dependency");
        System.out.println("  process                         - Process all queued events");
        System.out.println("  reachable <service>             - Get reachable services from a service");
        System.out.println("  services                        - List all services");
        System.out.println("  queue                           - Show queue status");
        System.out.println("  demo                            - Load demo dataset");
        System.out.println("  clear                           - Clear the graph");
        System.out.println("  quit                            - Exit");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) continue;
            
            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();
            
            try {
                switch (command) {
                    case "add":
                        // Handle adding a new dependency event to the queue
                        if (parts.length != 4) {
                            System.out.println("Usage: add <source> <target> <latency>");
                            break;
                        }
                        String source = parts[1];
                        String target = parts[2];
                        int latency = Integer.parseInt(parts[3]);
                        analyzer.publishDependencyEvent(source, target, latency);
                        System.out.println("Event published to queue");
                        break;
                        
                    case "process":
                        // Process all events in the queue and update the service graph
                        int before = analyzer.getProcessedEventCount();
                        analyzer.processAllQueuedEvents();
                        int processed = analyzer.getProcessedEventCount() - before;
                        System.out.println("Processed " + processed + " events");
                        break;
                        
                    case "reachable":
                        // Find and display all services reachable from the specified service
                        if (parts.length != 2) {
                            System.out.println("Usage: reachable <service>");
                            break;
                        }
                        String serviceName = parts[1];
                        if (analyzer.hasService(serviceName)) {
                            Set<String> reachable = analyzer.getReachableServices(serviceName);
                            System.out.println("Reachable from " + serviceName + ": " + reachable);
                        } else {
                            System.out.println("Service '" + serviceName + "' not found");
                        }
                        break;
                        
                    case "services":
                        // Display all services currently in the graph
                        System.out.println("All services: " + analyzer.getAllServices());
                        break;
                        
                    case "queue":
                        // Display current queue status and processing statistics
                        System.out.println("Queue size: " + analyzer.getQueueSize());
                        System.out.println("Total processed: " + analyzer.getProcessedEventCount());
                        break;
                        
                    case "demo":
                        // Load the demo dataset into the queue
                        loadSampleDataset();
                        System.out.println("Demo dataset loaded into queue");
                        break;
                        
                    case "clear":
                        // Clear the service graph and reset counters
                        analyzer.clearGraph();
                        System.out.println("Graph cleared");
                        break;
                        
                    case "quit":
                    case "exit":
                        // Exit the application
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Unknown command: " + command);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Loads a sample dataset of service dependencies for demonstration purposes.
     * Creates a connected graph with the following dependencies:
     * A -> B, C
     * B -> D
     * C -> D, E
     * D -> F
     * E -> F
     * F -> C, G (note: F->C creates a cycle)
     * G -> H
     * 
     * This dataset demonstrates various graph scenarios including cycles and
     * multiple paths between services.
     */
    private static void loadSampleDataset() {
        analyzer.publishDependencyEvent("A", "B", 5);
        analyzer.publishDependencyEvent("A", "C", 3);
        analyzer.publishDependencyEvent("B", "D", 2);
        analyzer.publishDependencyEvent("C", "D", 7);
        analyzer.publishDependencyEvent("C", "E", 4);
        analyzer.publishDependencyEvent("D", "F", 6);
        analyzer.publishDependencyEvent("E", "F", 1);
        analyzer.publishDependencyEvent("F", "C", 8);  // Creates a cycle: C -> E -> F -> C
        analyzer.publishDependencyEvent("F", "G", 10);
        analyzer.publishDependencyEvent("G", "H", 9);
    }
}