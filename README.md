# Graphs-Network_Route_Finder_Rivera_Mervin_John

# Network Route Finder

**Course:** CpE 211 – Data Structures and Algorithms  
**Module:** 2 – Graphs

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Project Structure](#2-project-structure)
3. [Graph Representation](#3-graph-representation)
4. [Implemented Algorithms](#4-implemented-algorithms)
5. [Sample Graph and Expected Results](#5-sample-graph-and-expected-results)
6. [Testing](#6-testing)
7. [Complexity Analysis](#7-complexity-analysis)
8. [Reflection](#8-reflection)
9. [Development Record](#9-development-record)

---

## 1. Project Overview

The Network Route Finder is a Java project that demonstrates graph representation and traversal algorithms using adjacency lists.

The project supports directed and undirected edges and implements three main algorithms:

- Breadth-First Search (BFS)
- Depth-First Search (DFS)
- Cycle Detection

The graph represents a network in which vertices are connected through edges. BFS finds a path with the fewest edges, while DFS finds a path according to neighbor insertion order.

## 2. Project Structure

```text
networkRouteFinder/
│
├── .vscode/
│   └── settings.json
│
├── src/
│   └── Graph.java
│
├── test/
│   └── GraphLabTestRunner.java
│
├── lib/
│
├── bin/
│
├── README.md
└── AGENT_LOG.md
```

**Main files:**

- `Graph.java` – Contains the graph representation and algorithms.
- `GraphLabTestRunner.java` – Contains the 56 acceptance and additional test cases.
- `README.md` – Provides project documentation and instructions.
- `AGENT_LOG.md` – Records the development workflow, testing progress, audit, and reflection.

## 3. Graph Representation

The graph uses the following Java collections:

- `LinkedHashMap` – Stores vertices and their adjacency lists while preserving insertion order.
- `ArrayList` – Stores neighboring vertices.
- `LinkedHashSet` – Tracks undirected edges and prevents duplicate entries.

The implementation supports:

- Adding vertices.
- Adding directed and undirected edges.
- Automatically creating missing vertices when adding edges.
- Preventing duplicate edges.
- Rejecting conflicting edge directions.
- Creating independent graph snapshots.

## 4. Implemented Algorithms

### Breadth-First Search (BFS)

BFS uses a queue to explore vertices level by level.

It returns a shortest path based on the number of edges when all edges have equal cost.

### Depth-First Search (DFS)

DFS explores one path as far as possible before backtracking.

It returns the first available path according to neighbor insertion order. It does not guarantee the shortest path.

### Cycle Detection

Cycle detection uses DFS with three vertex states:

- **WHITE** – Vertex has not been visited.
- **GRAY** – Vertex is currently being explored.
- **BLACK** – Vertex has finished processing.

The implementation distinguishes directed back-edges from undirected parent edges and checks every connected component.

## 5. Sample Graph and Expected Results

The sample undirected graph contains six vertices and seven edges.

**Edges:**

```text
A — B
A — C
B — D
C — D
C — E
D — F
E — F
```

**Expected adjacency list:**

```text
A: [B, C]
B: [A, D]
C: [A, D, E]
D: [B, C, F]
E: [C, F]
F: [D, E]
```

**Expected algorithm results:**

| Operation | Expected Result |
|---|---|
| BFS from A to F | `[A, B, D, F]` |
| DFS from A to F | `[A, B, D, C, E, F]` |
| BFS path length | 3 edges |
| DFS path length | 5 edges |
| Cycle detection | `true` |

The exact DFS path depends on neighbor insertion order.

## 6. Testing

The project includes a dependency-free Java test runner with 56 test cases.

The tests cover:

- Graph construction and vertex insertion.
- Directed and undirected edges.
- Duplicate and conflicting edges.
- BFS shortest-path behavior.
- DFS traversal and backtracking.
- Directed and undirected cycle detection.
- Disconnected components.
- Snapshot independence.
- Graph immutability during traversal.

### Running the Tests

Open the project folder in VS Code and use the terminal.

**Compile both Java files:**

```powershell
javac -d bin src\Graph.java test\GraphLabTestRunner.java
```

**Run the test runner:**

```powershell
java -cp bin GraphLabTestRunner
```

### Final Test Results

| Development Stage | Passed | Failed |
|---|---:|---:|
| Initial skeleton | 0 | 56 |
| Graph structure and edge operations | 21 | 35 |
| BFS implementation | 31 | 25 |
| DFS implementation | 43 | 13 |
| Final implementation | 56 | 0 |

**Final verification:**

- 56 tests passed.
- 0 tests failed.
- Compilation successful.
- `javac -Xlint:all` completed without warnings.

### Student-Authored Test Disclosure

The original 56-case test suite was generated with OpenCode. I did not personally write a failing test before the Build phase, so the original test-first requirement was not completed.

An additional student-authored test is intended to use this directed graph:

```text
A → B
B → C
A → C
```

Expected behavior:

- BFS from A to C returns `[A, C]`.
- BFS from C to A returns an empty list.
- A directed edge does not automatically create a reverse connection.

**Status:** This additional test must still be personally written and executed. It does not retroactively satisfy the original pre-Build requirement.

## 7. Complexity Analysis

Let:

- **V** = Number of vertices.
- **E** = Number of edges.

| Operation | Time Complexity | Auxiliary Space |
|---|---:|---:|
| Graph storage | O(V + E) | O(V + E) |
| BFS | O(V + E) | O(V) |
| DFS | O(V + E) | O(V) |
| Cycle Detection | O(V + E) | O(V) |
| Vertex lookup | O(1) average | O(1) |

Adjacency-list representation requires O(V + E) storage. BFS, DFS, and cycle detection visit vertices and examine adjacency entries, resulting in O(V + E) time complexity.

## 8. Reflection

### 1. Why does BFS return a shortest path here, and what changes with weights?

BFS returns a shortest path because it explores the graph level by level using a queue. It visits all vertices that are one edge away before exploring vertices that are two edges away. Therefore, when the target is reached, the path contains the minimum number of edges.

This guarantee works because all edges in the graph have equal cost. If edges have different weights, BFS may no longer find the path with the lowest total cost. An algorithm such as Dijkstra's algorithm is more appropriate for graphs with nonnegative edge weights.

### 2. How did your cycle logic distinguish an undirected parent edge from a directed back-edge?

The cycle detection algorithm uses three states: WHITE, GRAY, and BLACK.

For undirected edges, the algorithm skips the edge that leads directly back to the parent because it represents the same physical connection used to reach the current vertex. That reverse connection alone does not form a cycle.

For directed edges, an edge pointing to a GRAY vertex indicates a back-edge and therefore a cycle. BLACK vertices are already finished and are not automatically treated as evidence of a cycle.

This distinction allows the algorithm to detect cycles correctly in both directed and undirected graphs.

## 9. Development Record

Development followed four stages:

1. **Plan** – Established the graph representation, API behavior, and algorithm logic.
2. **Test** – Prepared the acceptance-test suite and checked expected behavior.
3. **Build** – Implemented graph operations, BFS, DFS, and cycle detection.
4. **Audit** – Reviewed the implementation, compiled the files, and verified all 56 tests.

For the detailed development history, testing progression, and agent audit, see [AGENT_LOG.md](AGENT_LOG.md).

