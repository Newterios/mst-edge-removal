# MST Edge Removal and Replacement

A Java implementation demonstrating Minimum Spanning Tree (MST) construction using Kruskal's algorithm with efficient edge removal and replacement capabilities.

---

##  Quick Start

### Prerequisites
- Java 24 or higher
- Maven 3.9 +
- IntelliJ IDEA (recommended-_-)

### Clone and Run

```bash
git clone https://github.com/yourusername/mst-edge-removal.git
cd mst-edge-removal
```

### Running with IntelliJ IDEA (Recommended)

**Note:** Maven commands may not work properly through terminal. Use IntelliJ IDEA interface instead.

#### Running Tests:
1. Open project in IntelliJ IDEA
2. Navigate to `src/test/java/com/aitbek/mst/`
3. Right-click on any test class (e.g., `MSTAlgorithmTest.java`)
4. Select **"Run 'MSTAlgorithmTest'"**
5. View test results in the Run panel

Or run all tests:
1. Right-click on `src/test/java` folder
2. Select **"Run 'All Tests'"**

#### Running Main Application:
1. Navigate to `src/main/java/com/aitbek/mst/cli/MSTApplication.java`
2. Right-click on the file
3. Select **"Run 'MSTApplication.main()'"**
4. View output in the Run console
5. Enter edge number when prompted (1-5)

#### Building JAR:
1. Open Maven panel (View → Tool Windows → Maven)
2. Expand Lifecycle
3. Double-click **clean**
4. Double-click **package**
5. JAR file will be in `target/mst-edge-removal-1.0.0.jar`

### Alternative: Command Line (if working)

```bash
# Build
mvn clean install

# Run tests
mvn test

# Run application
mvn exec:java -Dexec.mainClass="com.aitbek.mst.cli.MSTApplication"

# Or use JAR
java -jar target/mst-edge-removal-1.0.0.jar
```

---

##  Project Overview

This project efficiently handles edge removal from a Minimum Spanning Tree and finds optimal replacement edges to maintain MST properties.

### Core Features

- **MST Construction**: Implements Kruskal's algorithm with Union-Find optimization
- **Edge Removal**: Identifies connected components after edge deletion
- **Replacement Edge**: Finds minimum weight edge to reconnect components
- **Performance Tracking**: Records and exports execution metrics to CSV
- **Comprehensive Testing**: 35 unit tests covering all scenarios
- **JMH Benchmarking**: Performance measurement for various graph sizes

---

##  Project Structure

```
mst-edge-removal/
│
├── pom.xml                          # Maven configuration
├── README.md                        # This file
├── mst_metrics.csv                  # Performance metrics output
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/aitbek/mst/
│   │           │
│   │           ├── model/
│   │           │   ├── Edge.java              # Edge data structure
│   │           │   └── Graph.java             # Graph representation
│   │           │
│   │           ├── algorithm/
│   │           │   ├── UnionFind.java         # Disjoint set union
│   │           │   └── MSTAlgorithm.java      # MST operations
│   │           │
│   │           ├── cli/
│   │           │   └── MSTApplication.java    # Main CLI application
│   │           │
│   │           ├── metrics/
│   │           │   └── PerformanceMetrics.java # Performance tracking
│   │           │
│   │           └── benchmark/
│   │               └── MSTBenchmark.java      # JMH benchmarks
│   │
│   └── test/
│       └── java/
│           └── com/aitbek/mst/
│               ├── model/
│               │   ├── EdgeTest.java          # 6 tests
│               │   └── GraphTest.java         # 7 tests
│               │
│               ├── algorithm/
│               │   ├── UnionFindTest.java     # 7 tests
│               │   └── MSTAlgorithmTest.java  # 10 tests
│               │
│               └── metrics/
│                   └── PerformanceMetricsTest.java # 5 tests
│
└── target/                          # Maven build output
    ├── classes/                     # Compiled classes
    ├── test-classes/                # Compiled test classes
    ├── surefire-reports/            # Test reports
    └── mst-edge-removal-1.0.0.jar  # Packaged JAR
```

**Total:** 7 source classes + 5 test classes = 35 comprehensive tests

---

##  Algorithm Details

### MST Construction (Kruskal's Algorithm)

**Time Complexity:** O(E log E)  
**Space Complexity:** O(V + E)

**Algorithm Steps:**
1. Sort all edges by weight (O(E log E))
2. Initialize Union-Find structure (O(V))
3. For each edge in sorted order:
    - Check if adding edge creates cycle using Union-Find
    - If no cycle, add edge to MST
    - Stop when V-1 edges are added

**Implementation:**
```java
public List<Edge> buildMST() {
    List<Edge> edges = graph.getEdges();
    Collections.sort(edges);
    
    UnionFind uf = new UnionFind(graph.getVertices());
    List<Edge> mstEdges = new ArrayList<>();
    
    for (Edge edge : edges) {
        if (uf.union(edge.getSource(), edge.getDestination())) {
            mstEdges.add(edge);
        }
        if (mstEdges.size() == graph.getVertices() - 1) break;
    }
    
    return mstEdges;
}
```

### Edge Removal and Replacement

**Time Complexity:** O(E)  
**Space Complexity:** O(V)

**Algorithm Steps:**
1. Remove specified edge from MST (O(1))
2. Find resulting components using Union-Find (O(V))
3. Search all graph edges for minimum weight edge connecting components (O(E))
4. Return replacement edge

**Implementation:**
```java
public Edge findReplacementEdge(Edge removedEdge) {
    List<Set<Integer>> components = getComponentsAfterRemoval(removedEdge);
    Set<Integer> component1 = components.get(0);
    Set<Integer> component2 = components.get(1);
    
    Edge bestReplacement = null;
    int minWeight = Integer.MAX_VALUE;
    
    for (Edge edge : graph.getEdges()) {
        if (edge.equals(removedEdge)) continue;
        
        boolean connects = 
            (component1.contains(edge.getSource()) && 
             component2.contains(edge.getDestination())) ||
            (component2.contains(edge.getSource()) && 
             component1.contains(edge.getDestination()));
        
        if (connects && edge.getWeight() < minWeight) {
            minWeight = edge.getWeight();
            bestReplacement = edge;
        }
    }
    
    return bestReplacement;
}
```

### Union-Find Optimization

**Operations:** O(α(n)) amortized per operation

**Optimizations:**
- **Path Compression**: Flattens tree structure during find operations
- **Union by Rank**: Attaches smaller tree to larger tree root

```java
public int find(int vertex) {
    if (parent[vertex] != vertex) {
        parent[vertex] = find(parent[vertex]); // Path compression
    }
    return parent[vertex];
}

public boolean union(int vertex1, int vertex2) {
    int root1 = find(vertex1);
    int root2 = find(vertex2);
    
    if (root1 == root2) return false;
    
    // Union by rank
    if (rank[root1] < rank[root2]) {
        parent[root1] = root2;
    } else if (rank[root1] > rank[root2]) {
        parent[root2] = root1;
    } else {
        parent[root2] = root1;
        rank[root1]++;
    }
    
    return true;
}
```

---

##  Complexity Analysis

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| MST Construction | O(E log E) | O(V + E) |
| Edge Removal | O(V) | O(V) |
| Component Detection | O(V) | O(V) |
| Replacement Search | O(E) | O(V) |
| Union-Find Operation | O(α(n)) | O(V) |
| **Overall** | **O(E log E)** | **O(V + E)** |

Where:
- V = number of vertices
- E = number of edges
- α(n) = inverse Ackermann function (effectively constant)

---

##  Testing

### Test Coverage (35 Tests Total)

#### Model Tests (13 tests)
**EdgeTest.java** (6 tests):
- Edge creation and validation
- Comparable implementation (sorting by weight)
- Equals and hashCode for bidirectional edges
- toString format verification

**GraphTest.java** (7 tests):
- Graph initialization with vertices
- Edge addition and retrieval
- Adjacent edges lookup
- Sample graph factory method
- Empty graph handling
- Single vertex graph

#### Algorithm Tests (17 tests)
**UnionFindTest.java** (7 tests):
- Initial state verification (all separate components)
- Union operation correctness
- Cycle detection (union returns false)
- Multiple unions and connectivity
- Path compression verification
- Transitive connectivity
- Large scale union-find (1000 elements)

**MSTAlgorithmTest.java** (10 tests):
- MST construction correctness
- MST weight calculation
- Component detection after edge removal
- Replacement edge finding
- New MST with replacement
- Simple 3-vertex graph
- Disconnected component handling
- Single edge graph
- Current MST retrieval
- Edge case scenarios

#### Metrics Tests (5 tests)
**PerformanceMetricsTest.java** (5 tests):
- Operation recording
- Multiple metric records
- CSV export functionality
- CSV format validation
- Performance summary printing

### Running Tests in IntelliJ IDEA

**All Tests:**
1. Right-click `src/test/java`
2. Select "Run 'All Tests'"
3. View results in Run panel

**Specific Test Class:**
1. Navigate to test class
2. Right-click on class name
3. Select "Run 'ClassName'"

**Single Test Method:**
1. Right-click on test method
2. Select "Run 'methodName()'"

### Test Results

All 35 tests pass successfully:
```
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
Success rate: 100%
```

---

##  Usage Example

### Sample Output

```
=== MST Edge Removal and Replacement ===

Original MST:
  1. (1 -- 2, weight: 1)
  2. (1 -- 3, weight: 2)
  3. (3 -- 4, weight: 2)
  4. (0 -- 2, weight: 3)
  5. (4 -- 5, weight: 3)
Total MST Weight: 11

Select an edge to remove (enter number 1-5):
2

Removing edge: (1 -- 3, weight: 2)

Components after edge removal:
  Component 1: [0, 1, 2]
  Component 2: [3, 4, 5]

Replacement edge found: (2 -- 3, weight: 4)

New MST after replacement:
  1. (1 -- 2, weight: 1)
  2. (3 -- 4, weight: 2)
  3. (0 -- 2, weight: 3)
  4. (4 -- 5, weight: 3)
  5. (2 -- 3, weight: 4)
Total MST Weight: 13

=== Performance Metrics ===
Build MST: 0.402 ms (vertices: 6, edges: 8)
Find Components: 0.097 ms (vertices: 6, edges: 5)
Find Replacement: 0.027 ms (vertices: 6, edges: 8)

Metrics exported to mst_metrics.csv
```

### Performance Metrics CSV

The application exports detailed metrics to `mst_metrics.csv`:

```csv
Operation,Duration(ns),Duration(ms),Vertices,Edges
Build MST,402100,0.402,6,8
Find Components,96600,0.097,6,5
Find Replacement,27200,0.027,6,8
```

---

##  Design Patterns Used

### 1. Model-View-Separation
- **Model**: `Edge`, `Graph` (data structures)
- **Algorithm**: `MSTAlgorithm`, `UnionFind` (business logic)
- **View**: `MSTApplication` (user interface)

### 2. Factory Pattern
```java
public static Graph createSampleGraph() {
    Graph graph = new Graph(6);
    graph.addEdge(0, 1, 4);
    // ... more edges
    return graph;
}
```

### 3. Strategy Pattern
Different MST operations encapsulated in `MSTAlgorithm`:
- `buildMST()`
- `getComponentsAfterRemoval()`
- `findReplacementEdge()`
- `getMSTWithReplacement()`

### 4. Metrics Collector Pattern
`PerformanceMetrics` collects and aggregates performance data

---

##  Technical Implementation Details

### Edge Class
- Implements `Comparable<Edge>` for sorting by weight
- Bidirectional equality (edge 0→1 equals edge 1→0)
- Immutable design with final fields

### Graph Class
- Adjacency list representation for efficient edge lookup
- Stores both edge list and adjacency structure
- O(1) edge addition, O(V) adjacency retrieval

### UnionFind Class
- Path compression flattens tree during find operations
- Union by rank minimizes tree height
- Near-constant time operations in practice

### MSTAlgorithm Class
- Stateful design maintains current MST
- Efficient component detection using Union-Find
- Linear search for replacement edge (can be optimized with data structures)

---

##  Performance Benchmarks

### JMH Benchmark Results

Run benchmarks with:
```java
// In IntelliJ IDEA:
// Right-click MSTBenchmark.java → Run
```

**Graph Sizes Tested:**
- 10 vertices, 20 edges
- 50 vertices, 100 edges
- 100 vertices, 200 edges
- 500 vertices, 1000 edges

**Metrics:**
- Average execution time
- Throughput (operations/second)
- Warmup iterations: 3
- Measurement iterations: 5

---

##  Troubleshooting

### Maven Commands Not Working

**Issue:** Terminal Maven commands fail or hang  
**Solution:** Use IntelliJ IDEA interface instead:
1. Open Maven panel (View → Tool Windows → Maven)
2. Use lifecycle goals (clean, compile, test, package)
3. Run configurations for main application

### Cannot Run Tests

**Issue:** Tests don't run from terminal  
**Solution:**
1. Right-click test class in IntelliJ IDEA
2. Select "Run 'TestClass'"
3. Or use Maven panel → Lifecycle → test

### Application Won't Start

**Issue:** Main application doesn't run  
**Solution:**
1. Navigate to `MSTApplication.java`
2. Right-click on file
3. Select "Run 'MSTApplication.main()'"
4. Check Run console for output

### JMH Benchmarks Not Found

**Issue:** Benchmark classes not generated  
**Solution:**
1. Maven panel → Lifecycle → clean
2. Maven panel → Lifecycle → compile
3. Check `target/generated-sources/annotations/`

---

##  Key Features

###  Algorithm Correctness
- Proper Kruskal's MST implementation
- Efficient Union-Find with optimizations
- Correct replacement edge selection

###  Code Quality
- Clean, readable code
- No comments (self-documenting)
- Proper naming conventions
- Follows Java standards

###  Testing Excellence
- 35 comprehensive unit tests
- 100% test success rate
- Edge cases covered
- Integration tests included

###  Performance
- O(E log E) MST construction
- O(α(n)) Union-Find operations
- Metrics tracking and export
- JMH benchmarking suite

###  Professional Development
- Proper Maven structure
- Clear package organization
- Comprehensive documentation
- Production-ready code

---

##  Learning Outcomes

### Algorithms Mastered
- Minimum Spanning Tree (Kruskal's algorithm)
- Union-Find with path compression and union by rank
- Graph traversal and component detection
- Greedy algorithms

### Software Engineering Skills
- Clean architecture and separation of concerns
- Test-driven development practices
- Performance measurement and optimization
- Build automation with Maven
- Git version control

### Java Expertise
- Object-oriented design principles
- Java Collections framework
- JUnit 5 testing framework
- JMH benchmarking
- File I/O operations
- Generic programming

---

##  Academic Context

**Course:** Design and Analysis of Algorithms  
**Task:** Edge Removal from MST (5% Endterm Registration)  
**Objective:** Efficiently find replacement edges to maintain MST property

**Requirements Met:**
-  Build MST from graph
-  Display MST edges
-  Remove edge from MST
-  Show resulting components
-  Find replacement edge
-  Display new MST
-  Runnable after clone
-  Clear documentation

---

## Author

**Aitbek**  
AITU Student  
Design and Analysis of Algorithms Course

---

##  License

This project is created for educational purposes as part of the Design and Analysis of Algorithms course.

---

##  Acknowledgments

- **Kruskal's Algorithm**: Joseph Kruskal (1956)
- **Union-Find**: Bernard A. Galler and Michael J. Fischer (1964)
- **Path Compression**: Robert Tarjan and Jan van Leeuwen
- **JUnit**: Kent Beck and Erich Gamma
- **JMH**: Aleksey Shipilëv and Oracle

---

**Last Updated:** November 13, 2025  
**Version:** 1.0.0  
**Build Status:**  All tests passing