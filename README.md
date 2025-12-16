# Dependency Analyzer

A Java implementation of a simplified dependency analyzer for distributed systems. This application ingests events from a message queue, maintains an in-memory directed graph of services, and performs analytical queries using graph traversal.

## Features

- **Message Queue**: Simple in-memory message queue for event publishing and consumption
- **Directed Graph**: In-memory graph to store service dependencies
- **Event Processing**: Publisher/Consumer pattern for handling dependency events
- **Graph Traversal**: DFS-based algorithm to find reachable services
- **Interactive Mode**: Command-line interface for real-time interaction
- **Demo Mode**: Automated demonstration with sample dataset

## Architecture

```
src/main/java/com/dependencyanalyzer/
├── model/
│   └── DependencyEvent.java        # Event data model
├── queue/
│   └── MessageQueue.java           # Simple in-memory message queue
├── graph/
│   └── ServiceGraph.java           # Directed graph implementation
├── service/
│   ├── EventPublisher.java         # Event publishing service
│   ├── EventConsumer.java          # Event consumption service
│   └── DependencyAnalyzerService.java # Main service orchestrator
└── DependencyAnalyzerMain.java     # Application entry point
```

## Prerequisites

- Java 8 or higher
- No external dependencies required

## Compilation Instructions

1. Navigate to the project directory:
   ```bash
   cd DependencyAnalyzer
   ```

2. Compile the Java source files:
   ```bash
   javac -d build -sourcepath src/main/java src/main/java/com/dependencyanalyzer/*.java src/main/java/com/dependencyanalyzer/*/*.java
   ```

3. Create a JAR file (optional):
   ```bash
   cd build
   jar cfe dependency-analyzer.jar com.dependencyanalyzer.DependencyAnalyzerMain .
   cd ..
   ```

## Execution Instructions

### Running from Compiled Classes

```bash
# Interactive mode
java -cp build com.dependencyanalyzer.DependencyAnalyzerMain

# Demo mode
java -cp build com.dependencyanalyzer.DependencyAnalyzerMain demo
```

### Running from JAR (if created)

```bash
# Interactive mode
java -jar build/dependency-analyzer.jar

# Demo mode
java -jar build/dependency-analyzer.jar demo
```

## Usage

### Demo Mode

Run the application with the `demo` argument to see it in action with the provided sample dataset:

```bash
java -cp build com.dependencyanalyzer.DependencyAnalyzerMain demo
```

This will:
1. Load the sample dataset into the message queue
2. Process all events to build the dependency graph
3. Demonstrate the `getReachableServices` functionality for various services

### Interactive Mode

Run without arguments to enter interactive mode:

```bash
java -cp build com.dependencyanalyzer.DependencyAnalyzerMain
```

Available commands:
- `add <source> <target> <latency>` - Add a dependency event to the queue
- `process` - Process all queued events and update the graph
- `reachable <service>` - Get all reachable services from a given service
- `services` - List all services in the graph
- `queue` - Show queue status and processing statistics
- `demo` - Load the demo dataset into the queue
- `clear` - Clear the graph and reset counters
- `quit` - Exit the application

### Example Session

```
> demo
Demo dataset loaded into queue

> process
Processed 10 events

> services
All services: [A, B, C, D, E, F, G, H]

> reachable F
Reachable from F: [C, E, G, H]

> reachable A
Reachable from A: [B, C, D, E, F, G, H]
```

## Sample Dataset

The application includes a sample dataset that demonstrates the dependency graph:

```
A → B (5ms)    A → C (3ms)    B → D (2ms)    C → D (7ms)    C → E (4ms)
D → F (6ms)    E → F (1ms)    F → C (8ms)    F → G (10ms)   G → H (9ms)
```

This creates a graph with cycles (F → C → E → F) and demonstrates reachability analysis.

## Implementation Details

### Message Queue
- Simple implementation using `ArrayDeque`
- FIFO ordering for event processing
- Lightweight and efficient for single-threaded access

### Graph Structure
- Adjacency list representation using `HashMap`
- Efficient storage and traversal
- Simple design optimized for single-threaded access

### Graph Traversal Algorithm
- Depth-First Search (DFS) implementation
- Handles cycles gracefully using visited set
- Returns all reachable nodes excluding the starting node

### Event Model
- Simple POJO with type, source, target, and latency fields
- Immutable design
- JSON-like string representation

## Assumptions

1. **Event Types**: Only "dependency" type events are processed; other types are ignored
2. **Service Names**: Service names are treated as case-sensitive strings
3. **Latency Values**: Latency is stored but not used in reachability calculations
4. **Memory Constraints**: Assumes reasonable dataset sizes that fit in memory
5. **Single-threaded Design**: Application runs in a single thread with sequential processing
6. **Graph Cycles**: Cycles in the dependency graph are handled correctly by the traversal algorithm
7. **Directed Graph**: Graph assumed to be directed so A -> B doesn't also mean B -> A

## Performance Characteristics

- **Queue Operations**: O(1) for publish/consume
- **Graph Updates**: O(1) for adding dependencies
- **Reachability Query**: O(V + E) where V is vertices and E is edges
- **Memory Usage**: O(V + E) for graph storage

## Error Handling

- Invalid commands in interactive mode are gracefully handled
- Number format exceptions are caught and reported
- Non-existent services return empty result sets
- Queue operations are simple and efficient

## Future Enhancements - Phase 2

### Production-Ready Features
- **REST API**: HTTP endpoints for event publishing instead of CLI
- **Real-time Processing**: Continuous background event consumption
- **WebSocket Streaming**: Live dependency graph updates

### Message Queue Integration
- **Kafka**: Persistent storage, horizontal scaling, event replay
- **RabbitMQ**: Complex routing, acknowledgments, dead letter queues

### Distributed Architecture
- **Multi-threading**: Separate producer/consumer/query thread pools
- **Microservices**: Split into ingestion, processing, and query services
- **Synchronization**: Read-write locks, event ordering, CQRS pattern

This would evolve the prototype into an enterprise-ready, real-time dependency analysis platform.
