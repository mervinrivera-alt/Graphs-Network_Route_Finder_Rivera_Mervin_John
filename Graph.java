import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Graph {
    private final Map<String, List<String>> adjacency = new LinkedHashMap<>();
    private final Set<UndirectedPair> undirectedEdges = new LinkedHashSet<>();

    public Graph() {
    }

    public void addVertex(String v) {
        if (v == null) {
            throw new IllegalArgumentException("Vertex must not be null");
        }
        if (!adjacency.containsKey(v)) {
            adjacency.put(v, new ArrayList<>());
        }
    }

    public void addEdge(String u, String v, boolean bidirectional) {
        if (u == null || v == null) {
            throw new IllegalArgumentException("Edge endpoints must not be null");
        }

        UndirectedPair pair = new UndirectedPair(u, v);
        boolean pairIsUndirected = undirectedEdges.contains(pair);
        boolean pairHasDirectedArc = hasDirectedArc(u, v) || hasDirectedArc(v, u);

        if (bidirectional && pairHasDirectedArc && !pairIsUndirected) {
            throw new IllegalStateException("Cannot convert a directed edge to an undirected edge");
        }
        if (!bidirectional && pairIsUndirected) {
            throw new IllegalStateException("Cannot convert an undirected edge to a directed edge");
        }

        addVertex(u);
        addVertex(v);

        if (bidirectional) {
            appendNeighborOnce(u, v);
            appendNeighborOnce(v, u);
            undirectedEdges.add(pair);
        } else {
            appendNeighborOnce(u, v);
        }
    }

    public List<String> breadthFirstSearch(String start, String target) {
        if (start == null || target == null) {
            throw new IllegalArgumentException("BFS endpoints must not be null");
        }
        if (!adjacency.containsKey(start) || !adjacency.containsKey(target)) {
            return new ArrayList<>();
        }
        if (start.equals(target)) {
            List<String> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        Deque<String> queue = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();
        Map<String, String> parent = new LinkedHashMap<>();

        visited.add(start);
        queue.addLast(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            if (current.equals(target)) {
                return buildPath(parent, start, target);
            }
            for (String neighbor : adjacency.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.addLast(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }

    public List<String> depthFirstSearch(String start, String target) {
        if (start == null || target == null) {
            throw new IllegalArgumentException("DFS endpoints must not be null");
        }
        if (!adjacency.containsKey(start) || !adjacency.containsKey(target)) {
            return new ArrayList<>();
        }
        if (start.equals(target)) {
            List<String> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        Set<String> visited = new LinkedHashSet<>();
        List<String> path = new ArrayList<>();
        if (depthFirstSearchFrom(start, target, visited, path)) {
            return new ArrayList<>(path);
        }
        return new ArrayList<>();
    }

    public boolean hasCycle() {
        Map<String, Color> colors = new LinkedHashMap<>();
        for (String vertex : adjacency.keySet()) {
            colors.put(vertex, Color.WHITE);
        }
        for (String vertex : adjacency.keySet()) {
            if (colors.get(vertex) == Color.WHITE
                    && hasCycleFrom(vertex, null, colors)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, List<String>> adjacencySnapshot() {
        Map<String, List<String>> copy = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : adjacency.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    public Set<UndirectedPair> undirectedEdgesSnapshot() {
        return new LinkedHashSet<>(undirectedEdges);
    }

    private boolean hasDirectedArc(String from, String to) {
        List<String> neighbors = adjacency.get(from);
        return neighbors != null && neighbors.contains(to);
    }

    private void appendNeighborOnce(String source, String target) {
        List<String> neighbors = adjacency.get(source);
        if (!neighbors.contains(target)) {
            neighbors.add(target);
        }
    }

    private List<String> buildPath(Map<String, String> parent, String start, String target) {
        List<String> path = new ArrayList<>();
        String current = target;
        while (current != null) {
            path.add(current);
            if (current.equals(start)) {
                break;
            }
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private boolean depthFirstSearchFrom(String current, String target,
            Set<String> visited, List<String> path) {
        visited.add(current);
        path.add(current);

        if (current.equals(target)) {
            return true;
        }

        for (String neighbor : adjacency.get(current)) {
            if (!visited.contains(neighbor)
                    && depthFirstSearchFrom(neighbor, target, visited, path)) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    private boolean hasCycleFrom(String current, String parent,
            Map<String, Color> colors) {
        colors.put(current, Color.GRAY);

        for (String neighbor : adjacency.get(current)) {
            if (neighbor.equals(parent) && isUndirectedPair(current, neighbor)) {
                continue;
            }

            Color neighborColor = colors.getOrDefault(neighbor, Color.WHITE);
            if (neighborColor == Color.GRAY) {
                return true;
            }
            if (neighborColor == Color.WHITE
                    && hasCycleFrom(neighbor, current, colors)) {
                return true;
            }
        }

        colors.put(current, Color.BLACK);
        return false;
    }

    private boolean isUndirectedPair(String u, String v) {
        return undirectedEdges.contains(new UndirectedPair(u, v));
    }

    private enum Color {
        WHITE,
        GRAY,
        BLACK
    }

    public static final class UndirectedPair {
        private final String first;
        private final String second;

        public UndirectedPair(String a, String b) {
            if (a == null || b == null) {
                throw new IllegalArgumentException("UndirectedPair vertices must not be null");
            }
            if (a.compareTo(b) <= 0) {
                first = a;
                second = b;
            } else {
                first = b;
                second = a;
            }
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof UndirectedPair)) {
                return false;
            }
            UndirectedPair pair = (UndirectedPair) other;
            return first.equals(pair.first) && second.equals(pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return "{" + first + "," + second + "}";
        }
    }
}
