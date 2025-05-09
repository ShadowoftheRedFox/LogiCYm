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
    /**
     * List of cable queued to be looked, and to add to the {@link #cycleList} if
     * necessary.
     */
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

    /**
     * The actual function checking recursively. It's DFS.
     *
     * @param current Current component we are looking into.
     * @param visited List of visited component.
     * @param queued  List of component queued to be looked for.
     * @param depth   Actual depth. It's the amount of cable between the origin and
     *                current.
     * @return True if found a cycle, false otherwise.
     */
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

    /**
     * Check if origin is part of a cycle. The components forming the found cycle
     * can be retrieved with {@link #getComponentInCyle()}.
     *
     * @param origin The root component.
     * @return True if origin is in a cycle, false otherwise.
     */
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