import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GraphLabTestRunner {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("ESSENTIAL TESTS (29)");
        System.out.println("========================================");

        run("ST-1", "Lab graph adjacency and pair snapshots", GraphLabTestRunner::testST1);
        run("AV-1", "Add first vertex", GraphLabTestRunner::testAV1);
        run("AV-3", "Duplicate vertex keeps one empty entry", GraphLabTestRunner::testAV3);
        run("AE-1", "Directed edge auto-adds vertices", GraphLabTestRunner::testAE1);
        run("AE-2", "Undirected edge adds both directions and pair", GraphLabTestRunner::testAE2);
        run("AE-3", "Duplicate directed edge is not repeated", GraphLabTestRunner::testAE3);
        run("AE-4", "Duplicate undirected reverse edge is not repeated", GraphLabTestRunner::testAE4);
        run("AE-5", "Directed to undirected conflict is rejected", GraphLabTestRunner::testAE5);
        run("AE-6", "Undirected to directed conflict is rejected", GraphLabTestRunner::testAE6);
        run("AE-7", "Undirected self-loop is stored once with a pair", GraphLabTestRunner::testAE7);
        run("AE-8", "Directed self-loop has no undirected pair", GraphLabTestRunner::testAE8);
        run("AE-9", "Opposite directed arcs remain directed", GraphLabTestRunner::testAE9);
        run("BFS-1", "Lab shortest path", GraphLabTestRunner::testBFS1);
        run("BFS-2", "Shortest path wins over longer first branch", GraphLabTestRunner::testBFS2);
        run("BFS-4", "Disconnected target returns empty", GraphLabTestRunner::testBFS4);
        run("BFS-5", "Singleton and unknown self-target", GraphLabTestRunner::testBFS5);
        run("DFS-1", "Lab DFS insertion-order path", GraphLabTestRunner::testDFS1);
        run("DFS-2", "DFS neighbor insertion order changes path", GraphLabTestRunner::testDFS2);
        run("DFS-5", "DFS disconnected target returns empty", GraphLabTestRunner::testDFS5);
        run("DFS-6", "DFS singleton and unknown self-target", GraphLabTestRunner::testDFS6);
        run("HC-1", "Empty and singleton graphs are acyclic", GraphLabTestRunner::testHC1);
        run("HC-2", "Undirected tree parent edge is not a cycle", GraphLabTestRunner::testHC2);
        run("HC-3", "Single undirected edge is not a cycle", GraphLabTestRunner::testHC3);
        run("HC-5", "Directed two-cycle is a cycle", GraphLabTestRunner::testHC5);
        run("HC-6", "Directed three-cycle is a cycle", GraphLabTestRunner::testHC6);
        run("HC-8", "Directed self-loop is a cycle", GraphLabTestRunner::testHC8);
        run("HC-9", "Undirected self-loop is a cycle", GraphLabTestRunner::testHC9);
        run("IM-1", "Snapshot copies are independent and mutable", GraphLabTestRunner::testIM1);
        run("IM-2", "Traversals leave graph snapshots unchanged", GraphLabTestRunner::testIM2);

        System.out.println();
        System.out.println("========================================");
        System.out.println("ADDITIONAL COVERAGE (27)");
        System.out.println("========================================");

        run("ST-2", "Canonical undirected pair equality", GraphLabTestRunner::testST2);
        run("AV-2", "Vertex insertion order is preserved", GraphLabTestRunner::testAV2);
        run("AV-4", "Null vertex is rejected", GraphLabTestRunner::testAV4);
        run("AV-5", "Duplicate vertex after auto-add is a no-op", GraphLabTestRunner::testAV5);
        run("AE-10", "Neighbor insertion order is preserved", GraphLabTestRunner::testAE10);
        run("AE-11", "Null edge endpoints are rejected without mutation", GraphLabTestRunner::testAE11);
        run("AE-12", "Auto-add key order follows source then destination", GraphLabTestRunner::testAE12);
        run("BFS-3", "BFS tie-breaking follows insertion order", GraphLabTestRunner::testBFS3);
        run("BFS-6", "Unknown start and target return empty", GraphLabTestRunner::testBFS6);
        run("BFS-7", "Null BFS inputs are rejected", GraphLabTestRunner::testBFS7);
        run("BFS-8", "BFS terminates on an undirected cycle", GraphLabTestRunner::testBFS8);
        run("BFS-9", "BFS handles a directed cycle correctly", GraphLabTestRunner::testBFS9);
        run("BFS-10", "BFS state does not leak between calls", GraphLabTestRunner::testBFS10);
        run("DFS-3", "DFS does not return the shortest path", GraphLabTestRunner::testDFS3);
        run("DFS-4", "DFS backtracks dead ends out of the path", GraphLabTestRunner::testDFS4);
        run("DFS-7", "Null DFS inputs are rejected", GraphLabTestRunner::testDFS7);
        run("DFS-8", "DFS terminates on an undirected cycle", GraphLabTestRunner::testDFS8);
        run("DFS-9", "DFS handles a directed cycle correctly", GraphLabTestRunner::testDFS9);
        run("DFS-10", "DFS state does not leak between calls", GraphLabTestRunner::testDFS10);
        run("HC-4", "Undirected triangle is a cycle", GraphLabTestRunner::testHC4);
        run("HC-7", "Directed DAG with shared child is acyclic", GraphLabTestRunner::testHC7);
        run("HC-10", "Cycle in a later disconnected component is found", GraphLabTestRunner::testHC10);
        run("HC-11", "Disconnected acyclic graph remains acyclic", GraphLabTestRunner::testHC11);
        run("HC-12", "Mixed undirected and directed cycle is found", GraphLabTestRunner::testHC12);
        run("IM-3", "Repeated traversals remain independent", GraphLabTestRunner::testIM3);
        run("IM-4", "Failed traversals do not mutate the graph", GraphLabTestRunner::testIM4);
        run("IM-5", "Separate graph instances are isolated", GraphLabTestRunner::testIM5);

        printSummary();
    }

    private static Graph labGraph() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("A", "C", true);
        graph.addEdge("B", "D", true);
        graph.addEdge("C", "D", true);
        graph.addEdge("C", "E", true);
        graph.addEdge("D", "F", true);
        graph.addEdge("E", "F", true);
        return graph;
    }

    private static void testST1() {
        assertGraph(labGraph(),
                adjacencyMap("A:B,C", "B:A,D", "C:A,D,E", "D:B,C,F", "E:C,F", "F:D,E"),
                pairSet("A,B", "A,C", "B,D", "C,D", "C,E", "D,F", "E,F"));
    }

    private static void testST2() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        Graph.UndirectedPair ab = new Graph.UndirectedPair("A", "B");
        Graph.UndirectedPair ba = new Graph.UndirectedPair("B", "A");
        assertTrue("A-B must equal B-A", ab.equals(ba));
        assertEquals("A-B and B-A hash codes", ab.hashCode(), ba.hashCode());
        Set<Graph.UndirectedPair> pairs = graph.undirectedEdgesSnapshot();
        assertEquals("Undirected pair count", 1, pairs.size());
        assertTrue("Snapshot contains reverse lookup", pairs.contains(ba));
        assertEquals("Stored canonical pair", ab, pairs.iterator().next());
    }

    private static void testAV1() {
        Graph graph = new Graph();
        graph.addVertex("A");
        assertGraph(graph, adjacencyMap("A:"), pairSet());
    }

    private static void testAV2() {
        Graph graph = new Graph();
        graph.addVertex("A");
        graph.addVertex("C");
        graph.addVertex("B");
        assertGraph(graph, adjacencyMap("A:", "C:", "B:"), pairSet());
    }

    private static void testAV3() {
        Graph graph = new Graph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("A");
        assertGraph(graph, adjacencyMap("A:", "B:"), pairSet());
    }

    private static void testAV4() {
        Graph graph = new Graph();
        expectThrows("addVertex(null)", IllegalArgumentException.class, () -> graph.addVertex(null));
        assertGraph(graph, adjacencyMap(), pairSet());
    }

    private static void testAV5() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addVertex("B");
        assertGraph(graph, adjacencyMap("A:B", "B:"), pairSet());
    }

    private static void testAE1() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        assertGraph(graph, adjacencyMap("A:B", "B:"), pairSet());
    }

    private static void testAE2() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        assertGraph(graph, adjacencyMap("A:B", "B:A"), pairSet("A,B"));
    }

    private static void testAE3() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("A", "B", false);
        assertGraph(graph, adjacencyMap("A:B", "B:"), pairSet());
    }

    private static void testAE4() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "A", true);
        assertGraph(graph, adjacencyMap("A:B", "B:A"), pairSet("A,B"));
    }

    private static void testAE5() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        expectThrows("directed to undirected conflict", IllegalStateException.class,
                () -> graph.addEdge("A", "B", true));
        assertGraph(graph, adjacencyMap("A:B", "B:"), pairSet());
    }

    private static void testAE6() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        expectThrows("undirected to directed conflict", IllegalStateException.class,
                () -> graph.addEdge("B", "A", false));
        assertGraph(graph, adjacencyMap("A:B", "B:A"), pairSet("A,B"));
    }

    private static void testAE7() {
        Graph graph = new Graph();
        graph.addEdge("A", "A", true);
        assertGraph(graph, adjacencyMap("A:A"), pairSet("A,A"));
    }

    private static void testAE8() {
        Graph graph = new Graph();
        graph.addEdge("A", "A", false);
        assertGraph(graph, adjacencyMap("A:A"), pairSet());
    }

    private static void testAE9() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("B", "A", false);
        assertGraph(graph, adjacencyMap("A:B", "B:A"), pairSet());
    }

    private static void testAE10() {
        Graph graph = new Graph();
        graph.addEdge("A", "C", false);
        graph.addEdge("A", "B", false);
        assertGraph(graph, adjacencyMap("A:C,B", "C:", "B:"), pairSet());
    }

    private static void testAE11() {
        Graph graph = new Graph();
        expectThrows("null source", IllegalArgumentException.class,
                () -> graph.addEdge(null, "B", false));
        expectThrows("null target", IllegalArgumentException.class,
                () -> graph.addEdge("A", null, true));
        assertGraph(graph, adjacencyMap(), pairSet());
    }

    private static void testAE12() {
        Graph graph = new Graph();
        graph.addEdge("B", "A", true);
        assertGraph(graph, adjacencyMap("B:A", "A:B"), pairSet("A,B"));
    }

    private static void testBFS1() {
        assertPath("lab BFS A to F", List.of("A", "B", "D", "F"),
                labGraph().breadthFirstSearch("A", "F"));
    }

    private static void testBFS2() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("B", "C", false);
        graph.addEdge("C", "D", false);
        graph.addEdge("A", "D", false);
        assertPath("BFS shortest direct edge", List.of("A", "D"),
                graph.breadthFirstSearch("A", "D"));
    }

    private static void testBFS3() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("A", "C", true);
        graph.addEdge("B", "D", true);
        graph.addEdge("C", "D", true);
        assertPath("BFS insertion-order tie", List.of("A", "B", "D"),
                graph.breadthFirstSearch("A", "D"));
    }

    private static void testBFS4() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("C", "D", true);
        assertPath("BFS disconnected target", List.of(),
                graph.breadthFirstSearch("A", "D"));
    }

    private static void testBFS5() {
        Graph graph = new Graph();
        graph.addVertex("A");
        assertPath("BFS A to A", List.of("A"), graph.breadthFirstSearch("A", "A"));
        assertPath("BFS A to B", List.of(), graph.breadthFirstSearch("A", "B"));
        assertPath("BFS Z to Z", List.of(), graph.breadthFirstSearch("Z", "Z"));
    }

    private static void testBFS6() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        assertPath("BFS unknown start", List.of(), graph.breadthFirstSearch("Z", "A"));
        assertPath("BFS unknown target", List.of(), graph.breadthFirstSearch("A", "Z"));
        assertGraph(graph, adjacencyMap("A:B", "B:A"), pairSet("A,B"));
    }

    private static void testBFS7() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        expectThrows("BFS null start", IllegalArgumentException.class,
                () -> graph.breadthFirstSearch(null, "A"));
        expectThrows("BFS null target", IllegalArgumentException.class,
                () -> graph.breadthFirstSearch("A", null));
    }

    private static void testBFS8() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", true);
        graph.addEdge("C", "A", true);
        graph.addEdge("C", "D", true);
        assertPath("BFS cycle shortest path", List.of("A", "C", "D"),
                graph.breadthFirstSearch("A", "D"));
    }

    private static void testBFS9() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("B", "A", false);
        graph.addEdge("B", "C", false);
        assertPath("BFS directed cycle A to C", List.of("A", "B", "C"),
                graph.breadthFirstSearch("A", "C"));
        assertPath("BFS directed cycle C to A", List.of(),
                graph.breadthFirstSearch("C", "A"));
    }

    private static void testBFS10() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("C", "D", true);
        assertPath("BFS first failed lookup", List.of(), graph.breadthFirstSearch("A", "D"));
        assertPath("BFS after failed lookup", List.of("A", "B"),
                graph.breadthFirstSearch("A", "B"));
        assertPath("BFS other component", List.of("C", "D"),
                graph.breadthFirstSearch("C", "D"));
    }

    private static void testDFS1() {
        assertPath("lab DFS A to F", List.of("A", "B", "D", "C", "E", "F"),
                labGraph().depthFirstSearch("A", "F"));
    }

    private static void testDFS2() {
        Graph first = new Graph();
        first.addEdge("A", "B", true);
        first.addEdge("A", "C", true);
        first.addEdge("B", "D", true);
        first.addEdge("C", "D", true);
        assertPath("DFS with A-B inserted first", List.of("A", "B", "D"),
                first.depthFirstSearch("A", "D"));

        Graph second = new Graph();
        second.addEdge("A", "C", true);
        second.addEdge("A", "B", true);
        second.addEdge("C", "D", true);
        second.addEdge("B", "D", true);
        assertPath("DFS with A-C inserted first", List.of("A", "C", "D"),
                second.depthFirstSearch("A", "D"));
    }

    private static void testDFS3() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("B", "C", false);
        graph.addEdge("C", "D", false);
        graph.addEdge("A", "D", false);
        assertPath("DFS long branch before direct edge", List.of("A", "B", "C", "D"),
                graph.depthFirstSearch("A", "D"));
    }

    private static void testDFS4() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("A", "C", true);
        graph.addEdge("B", "D", true);
        assertPath("DFS dead-end backtracking", List.of("A", "C"),
                graph.depthFirstSearch("A", "C"));
    }

    private static void testDFS5() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("C", "D", true);
        assertPath("DFS disconnected target", List.of(),
                graph.depthFirstSearch("A", "D"));
    }

    private static void testDFS6() {
        Graph graph = new Graph();
        graph.addVertex("A");
        assertPath("DFS A to A", List.of("A"), graph.depthFirstSearch("A", "A"));
        assertPath("DFS A to B", List.of(), graph.depthFirstSearch("A", "B"));
        assertPath("DFS Z to Z", List.of(), graph.depthFirstSearch("Z", "Z"));
    }

    private static void testDFS7() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        expectThrows("DFS null start", IllegalArgumentException.class,
                () -> graph.depthFirstSearch(null, "A"));
        expectThrows("DFS null target", IllegalArgumentException.class,
                () -> graph.depthFirstSearch("A", null));
    }

    private static void testDFS8() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", true);
        graph.addEdge("C", "A", true);
        graph.addEdge("C", "D", true);
        assertPath("DFS undirected cycle path", List.of("A", "B", "C", "D"),
                graph.depthFirstSearch("A", "D"));
    }

    private static void testDFS9() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("B", "A", false);
        graph.addEdge("B", "C", false);
        assertPath("DFS directed cycle A to C", List.of("A", "B", "C"),
                graph.depthFirstSearch("A", "C"));
        assertPath("DFS directed cycle C to A", List.of(),
                graph.depthFirstSearch("C", "A"));
    }

    private static void testDFS10() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("C", "D", true);
        assertPath("DFS first failed lookup", List.of(), graph.depthFirstSearch("A", "D"));
        assertPath("DFS after failed lookup", List.of("A", "B"),
                graph.depthFirstSearch("A", "B"));
        assertPath("DFS other component", List.of("C", "D"),
                graph.depthFirstSearch("C", "D"));
    }

    private static void testHC1() {
        assertFalse("empty graph has no cycle", new Graph().hasCycle());
        Graph single = new Graph();
        single.addVertex("A");
        assertFalse("singleton graph has no cycle", single.hasCycle());
    }

    private static void testHC2() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", true);
        graph.addEdge("B", "D", true);
        assertFalse("undirected tree parent edge", graph.hasCycle());
    }

    private static void testHC3() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        assertFalse("single undirected edge", graph.hasCycle());
    }

    private static void testHC4() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", true);
        graph.addEdge("C", "A", true);
        assertTrue("undirected triangle has cycle", graph.hasCycle());
    }

    private static void testHC5() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("B", "A", false);
        assertTrue("directed two-cycle", graph.hasCycle());
    }

    private static void testHC6() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("B", "C", false);
        graph.addEdge("C", "A", false);
        assertTrue("directed three-cycle", graph.hasCycle());
    }

    private static void testHC7() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", false);
        graph.addEdge("A", "C", false);
        graph.addEdge("B", "D", false);
        graph.addEdge("C", "D", false);
        assertFalse("directed DAG shared child", graph.hasCycle());
    }

    private static void testHC8() {
        Graph graph = new Graph();
        graph.addEdge("A", "A", false);
        assertTrue("directed self-loop", graph.hasCycle());
    }

    private static void testHC9() {
        Graph graph = new Graph();
        graph.addEdge("A", "A", true);
        assertTrue("undirected self-loop", graph.hasCycle());
    }

    private static void testHC10() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("C", "D", false);
        graph.addEdge("D", "E", false);
        graph.addEdge("E", "C", false);
        assertTrue("cycle in later component", graph.hasCycle());
    }

    private static void testHC11() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("C", "D", false);
        graph.addVertex("E");
        assertFalse("disconnected acyclic graph", graph.hasCycle());
    }

    private static void testHC12() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        graph.addEdge("B", "C", false);
        graph.addEdge("C", "A", false);
        assertTrue("mixed undirected and directed cycle", graph.hasCycle());
    }

    private static void testIM1() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        Map<String, List<String>> adjacency = graph.adjacencySnapshot();
        Set<Graph.UndirectedPair> pairs = graph.undirectedEdgesSnapshot();
        adjacency.put("X", new ArrayList<>(List.of("A")));
        adjacency.get("A").add("X");
        pairs.add(new Graph.UndirectedPair("A", "C"));
        assertTrue("adjacency snapshot map is mutable", adjacency.containsKey("X"));
        assertTrue("adjacency snapshot list is mutable", adjacency.get("A").contains("X"));
        assertTrue("undirected pair snapshot is mutable", pairs.contains(new Graph.UndirectedPair("C", "A")));
        assertGraph(graph, adjacencyMap("A:B", "B:A"), pairSet("A,B"));
    }

    private static void testIM2() {
        Graph graph = labGraph();
        assertGraph(graph,
                adjacencyMap("A:B,C", "B:A,D", "C:A,D,E", "D:B,C,F", "E:C,F", "F:D,E"),
                pairSet("A,B", "A,C", "B,D", "C,D", "C,E", "D,F", "E,F"));
        assertPath("immutability BFS", List.of("A", "B", "D", "F"),
                graph.breadthFirstSearch("A", "F"));
        assertPath("immutability DFS", List.of("A", "B", "D", "C", "E", "F"),
                graph.depthFirstSearch("A", "F"));
        assertTrue("lab graph has cycle", graph.hasCycle());
        assertGraph(graph,
                adjacencyMap("A:B,C", "B:A,D", "C:A,D,E", "D:B,C,F", "E:C,F", "F:D,E"),
                pairSet("A,B", "A,C", "B,D", "C,D", "C,E", "D,F", "E,F"));
    }

    private static void testIM3() {
        Graph graph = labGraph();
        assertPath("BFS first call", List.of("A", "B", "D", "F"),
                graph.breadthFirstSearch("A", "F"));
        assertPath("BFS second call", List.of("A", "B", "D", "F"),
                graph.breadthFirstSearch("A", "F"));
        assertPath("DFS first call", List.of("A", "B", "D", "C", "E", "F"),
                graph.depthFirstSearch("A", "F"));
        assertPath("DFS second call", List.of("A", "B", "D", "C", "E", "F"),
                graph.depthFirstSearch("A", "F"));
        assertPath("BFS after DFS", List.of("A", "B", "D", "F"),
                graph.breadthFirstSearch("A", "F"));
    }

    private static void testIM4() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", true);
        assertPath("failed BFS", List.of(), graph.breadthFirstSearch("A", "Z"));
        assertPath("failed DFS", List.of(), graph.depthFirstSearch("A", "Z"));
        assertGraph(graph, adjacencyMap("A:B", "B:A"), pairSet("A,B"));
        assertPath("BFS after failed lookup", List.of("A", "B"),
                graph.breadthFirstSearch("A", "B"));
    }

    private static void testIM5() {
        Graph first = new Graph();
        Graph second = new Graph();
        first.addEdge("A", "B", true);
        assertGraph(first, adjacencyMap("A:B", "B:A"), pairSet("A,B"));
        assertGraph(second, adjacencyMap(), pairSet());
    }

    private static Map<String, List<String>> adjacencyMap(String... specs) {
        Map<String, List<String>> expected = new LinkedHashMap<>();
        for (String spec : specs) {
            int colon = spec.indexOf(':');
            if (colon < 0) {
                throw new AssertionError("Bad adjacency specification: " + spec);
            }
            String key = spec.substring(0, colon);
            String csv = spec.substring(colon + 1);
            List<String> neighbors = csv.isEmpty() ? List.of() : List.of(csv.split(","));
            expected.put(key, new ArrayList<>(neighbors));
        }
        return expected;
    }

    private static Set<Graph.UndirectedPair> pairSet(String... specs) {
        Set<Graph.UndirectedPair> expected = new LinkedHashSet<>();
        for (String spec : specs) {
            String[] parts = spec.split(",");
            if (parts.length != 2) {
                throw new AssertionError("Bad pair specification: " + spec);
            }
            expected.add(new Graph.UndirectedPair(parts[0], parts[1]));
        }
        return expected;
    }

    private static void assertGraph(Graph graph, Map<String, List<String>> expectedAdjacency,
            Set<Graph.UndirectedPair> expectedPairs) {
        Map<String, List<String>> actualAdjacency = graph.adjacencySnapshot();
        Set<Graph.UndirectedPair> actualPairs = graph.undirectedEdgesSnapshot();
        List<String> expectedKeys = new ArrayList<>(expectedAdjacency.keySet());
        List<String> actualKeys = new ArrayList<>(actualAdjacency.keySet());
        assertEquals("adjacency key order", expectedKeys, actualKeys);
        assertEquals("adjacency contents", expectedAdjacency, actualAdjacency);
        assertEquals("undirected pairs", expectedPairs, actualPairs);
    }

    private static void assertPath(String label, List<String> expected, List<String> actual) {
        assertEquals(label + " path", expected, actual);
    }

    private static void assertTrue(String label, boolean condition) {
        if (!condition) {
            throw new AssertionError(label + ": expected true but was false");
        }
    }

    private static void assertFalse(String label, boolean condition) {
        if (condition) {
            throw new AssertionError(label + ": expected false but was true");
        }
    }

    private static void assertEquals(String label, Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(label + ": expected " + expected + " but was " + actual);
        }
    }

    private static void expectThrows(String label, Class<? extends Throwable> expected,
            Runnable action) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expected.isInstance(actual)) {
                return;
            }
            throw new AssertionError(label + ": expected " + expected.getSimpleName()
                    + " but got " + actual.getClass().getSimpleName() + ": " + actual.getMessage());
        }
        throw new AssertionError(label + ": expected " + expected.getSimpleName()
                + " but no exception was thrown");
    }

    private static void run(String id, String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS [" + id + "] " + name);
        } catch (Throwable error) {
            failed++;
            System.out.println("FAIL [" + id + "] " + name);
            String message = error.getMessage();
            System.out.println("      " + error.getClass().getSimpleName()
                    + (message == null ? "" : ": " + message));
        }
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Summary: " + passed + " passed, " + failed + " failed.");
        System.out.println("========================================");
        if (failed > 0) {
            System.exit(1);
        }
    }
}
