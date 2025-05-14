package com.pjava.src.utils;

import java.util.ArrayList;
import java.util.List;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Element;
import com.pjava.src.components.Gate;

/**
 * This class is used to detect cycle between gates when we do a connection.
 * It should return true if there is a path looping from the starting gate to
 * the starting gate, and not if there is any other cycle.
 *
 * This class is not static because we can specify a max depth, and get the list
 * of element in the last cycle.
 *
 * ! We could make the assumption that if we find our starting point, we could
 * ! return true.
 * ! We can also make the assumption, since we know both input and output, that
 * ! if either one of them is empty, we can skip it. Special case to the
 * ! starting point, we can already return false.
 */
public class Cyclic {
    /**
     * List of elements in the cycle that has been found.
     */
    private ArrayList<Element> cycleList = new ArrayList<Element>();

    /**
     * List of cable that are the inputs of the elements in {@link #cycleList}. If
     * all are powered, that means the cycle should be powered. The opposite stands
     * true.
     */
    private ArrayList<Cable> inputCableList = new ArrayList<Cable>();

    /**
     * List of cable that are the outputs of the elements in {@link #cycleList}. If
     * cycle if powered, then those outputs should receive a power update.
     */
    private ArrayList<Cable> outputCableList = new ArrayList<Cable>();

    /**
     * List of cable queued to be looked, and to add to the {@link #cycleList} if
     * necessary.
     */
    private ArrayList<Element> queuedCable = new ArrayList<Element>();

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
     * Once {@link #isCyclic(Gate)} has run, if there is a cycle, all element
     * composing this cycle will be added to the result.
     *
     * @return Element making the cycle.
     */
    public List<Element> getElementInCyle() {
        return cycleList;
    }

    /**
     * Once {@link #isCyclic(Gate)} has run, if there is a cycle, all cable that are
     * inputs of said cycle will be returned here. Cables in this list are not in
     * {@link #getElementInCyle()}.
     *
     * @return Cable inputs of the cycle.
     */
    public List<Cable> getCycleInput() {
        return inputCableList;
    }

    /**
     * Once {@link #isCyclic(Gate)} has run, if there is a cycle, all cable that are
     * outputs of said cycle will be returned here. Cables in this list are not in
     * {@link #getElementInCyle()}.
     *
     * @return Cable outputs of the cycle.
     */
    public List<Cable> getCycleOutput() {
        return outputCableList;
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
     * @param current Current element we are looking into.
     * @param visited List of visited element.
     * @param queued  List of element queued to be looked for.
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
        // if the current gate has either no input or no output, it can't be part of a
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
     * @param x The element to add into the list.
     */
    private void addToCycleList(Element x) {
        if (!cycleList.contains(x)) {
            cycleList.add(x);
        }
    }

    /**
     * Check if origin is part of a cycle. The elements forming the found cycle
     * can be retrieved with {@link #getElementInCyle()}.
     *
     * @param origin The root element.
     * @return True if origin is in a cycle, false otherwise.
     */
    public boolean isCyclic(Gate origin) {
        if (origin == null) {
            return false;
        }

        ArrayList<Gate> visited = new ArrayList<Gate>();
        ArrayList<Gate> queued = new ArrayList<Gate>();
        queuedCable = new ArrayList<Element>();
        // result if cycle
        cycleList = new ArrayList<Element>();
        inputCableList = new ArrayList<Cable>();
        outputCableList = new ArrayList<Cable>();

        boolean res = false;
        try {
            res = checkIsCyclic(origin, visited, queued, depth);
            if (res) {
                addToCycleList(origin);
            }
        } catch (Error e) {
            System.out.println("Early return: " + e.getMessage());
        }

        if (res) {
            // add all inputs and outputs of the cycleList into inputCableList and
            // outputCableList respectively
            cycleList.forEach(element -> {
                if (element instanceof Gate) {
                    ((Gate) element).getInputCable().forEach(cable -> {
                        if (cable != null) {
                            // if the cable connect to a gate outside of the cycle, it is used as both cycle
                            // element and input
                            boolean usedAsBoth = false;
                            for (Gate gate : cable.getInputGate()) {
                                if (!cycleList.contains(gate)) {
                                    usedAsBoth = true;
                                }
                            }

                            if ((!cycleList.contains(cable) || usedAsBoth) && !inputCableList.contains(cable)) {
                                inputCableList.add(cable);
                            }
                        }
                    });
                    ((Gate) element).getOutputCable().forEach(cable -> {
                        if (cable != null) {
                            // if the cable connect to a gate outside of the cycle, it is used as both cycle
                            // element and output
                            boolean usedAsBoth = false;
                            for (Gate gate : cable.getOutputGate()) {
                                if (!cycleList.contains(gate)) {
                                    usedAsBoth = true;
                                }
                            }

                            if ((!cycleList.contains(cable) || usedAsBoth) && !outputCableList.contains(cable)) {
                                outputCableList.add(cable);
                            }
                        }
                    });
                }
            });
        }

        return res;
    }
}
