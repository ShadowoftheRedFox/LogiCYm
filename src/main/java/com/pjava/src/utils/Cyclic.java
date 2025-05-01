package com.pjava.src.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Component;
import com.pjava.src.components.Gate;

/**
 * This class is used to detect cycle between gates when we do a connection.
 * It should return true if there is a path looping from the starting gate to
 * the starting gate, and not if there is any other cycle.
 *
 * This class is not static because we can specify a max depth, and get the list
 * of component in the last cycle.
 *
 * ! We could make the assumption that if we find our starting point, we could
 * ! return true.
 * ! We can also make the assumption, since we know both input and output, that
 * ! if either one of them is empty, we can skip it. Special case to the
 * ! starting point, we can already return false.
 */
public class Cyclic {
    /**
     * List of components in the cycle that has been found.
     */
    private ArrayList<Component> cycleList = new ArrayList<Component>();
    private ArrayList<Component> queuedCable = new ArrayList<Component>();

    /**
     * The depth of the search. It's counted by how many layers have been looked at.
     */
    private int depth = 10;

    /**
     * Create a new cyclic class.
     */
    public Cyclic() {
    }

    /**
     * Once {@link #isCyclic(Gate)} has run, if there is a cycle, all component
     * composing this cycle will be added to the result.
     *
     * @return Component making the cycle.
     */
    public List<Component> getComponentInCyle() {
        return cycleList;
    }

    /**
     * Setter for {@link #depth}.
     * Depth limit how many layer of cable to look into. A larger depth is great to
     * make sure there is no cycle, but may take longer.
     *
     * @param depth The new depth.
     * @throws IllegalArgumentException Throw when depth is less than 1.
     */
    public void setDepth(int depth) {
        if (depth <= 1) {
            throw new IllegalArgumentException("Expected depth greater than 1, received " + depth);
        }
        this.depth = depth;
    }

    private boolean checkIsCyclic(Gate current, ArrayList<Gate> visited, ArrayList<Gate> queued, int depth) {
        // System.err.println(current);
        if (depth < 0 || current == null) {
            // System.out.println("looking too deep");
            return false;
        }
        // if the current gate has either no input or no ouput, it can't be part of a
        // cycle, therefore skip it
        if (current.getInputNumber() == 0 || current.getOutputNumber() == 0) {
            // System.out.println("one way gate");
            return false;
        }

        // if the current gate is in the queued list, then a cycle is detected
        if (queued.contains(current)) {
            addToCycleList(current);
            addToCycleList(queuedCable.get(queuedCable.size() - 1));
            // System.out.println("already queued");
            return true;
        }

        // if the current gate already visited and not in the queued, then the gate is
        // not part of the cycle
        if (visited.contains(current)) {
            // System.out.println("already visited");
            return false;
        }

        // add the current gate in visited and queued
        visited.add(current);
        queued.add(current);

        // go through all output and recursive on each gates
        for (Cable cable : current.getOutputCable()) {
            if (cable != null) {
                queuedCable.add(cable);
                for (Gate gate : cable.getOutputGate()) {
                    if (gate != null) {
                        if (checkIsCyclic(gate, visited, queued, depth - 1)) {
                            addToCycleList(gate);
                            addToCycleList(cable);
                            return true;
                        }
                    }
                }
                queuedCable.remove(cable);
            }
        }

        // backtrack and remove the current from the queue
        queued.remove(current);
        return false;
    }

    /**
     * Add x to the {@link #cycleList} if x isn't in the list already.
     *
     * @param x The component to add into the list.
     */
    private void addToCycleList(Component x) {
        if (!cycleList.contains(x)) {
            cycleList.add(x);
        }
    }

    public boolean isCyclic(Gate origin) {
        if (origin == null) {
            return false;
        }

        ArrayList<Gate> visited = new ArrayList<Gate>();
        ArrayList<Gate> queued = new ArrayList<Gate>();
        cycleList = new ArrayList<Component>();
        queuedCable = new ArrayList<Component>();

        boolean res = false;
        try {
            res = checkIsCyclic(origin, visited, queued, depth);
            if (res) {
                addToCycleList(origin);
            }
        } catch (Error e) {
            System.out.println("Early return: " + e.getMessage());
        }
        return res;
    }
}

class CyclicInt {
    // Function to perform DFS and detect cycle in a
    // directed graph
    private boolean isCyclicUtil(List<Integer>[] adj,
            int u,
            boolean[] visited,
            boolean[] recStack) {
        // If the current node is already in the recursion
        // stack, a cycle is detected
        if (recStack[u])
            return true;

        // If already visited and not in recStack, it's not
        // part of a cycle
        if (visited[u])
            return false;

        // Mark the current node as visited and add it to
        // the recursion stack
        visited[u] = true;
        recStack[u] = true;

        // Recur for all adjacent vertices
        for (int v : adj[u]) {
            if (isCyclicUtil(adj, v, visited, recStack))
                return true;
        }

        // Backtrack: remove the vertex from recursion stack
        recStack[u] = false;
        return false;
    }

    // Function to construct adjacency list from edge list
    private List<Integer>[] constructAdj(
            int V, int[][] edges) {
        // Create an array of lists
        @SuppressWarnings("unchecked")
        List<Integer>[] adj = (List<Integer>[]) Collections.nCopies(V, null).toArray();
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }

        // Add edges to the adjacency list (directed)
        for (int[] edge : edges) {
            adj[edge[0]].add(edge[1]);
        }

        return adj;
    }

    // Main function to check if the directed graph contains
    // a cycle
    public boolean isCyclic(int V, int[][] edges) {
        List<Integer>[] adj = constructAdj(V, edges);
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];

        // Perform DFS from each unvisited vertex
        for (int i = 0; i < V; i++) {
            if (!visited[i]
                    && isCyclicUtil(adj, i, visited, recStack))
                return true; // Cycle found
        }

        return false; // No cycle found
    }

    public void main(String[] args) {
        int V = 4; // Number of vertices

        // Directed edges of the graph
        int[][] edges = {
                { 0, 1 },
                { 0, 2 },
                { 1, 2 },
                { 2, 0 }, // This edge creates a cycle (0 → 2 → 0)
                { 2, 3 }
        };

        // Print result
        System.out.println(isCyclic(V, edges) ? "true"
                : "false");
    }

}
