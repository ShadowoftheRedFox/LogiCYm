package com.pjava.src.components;

import java.io.FileReader;
import java.util.HashSet;

import com.pjava.src.document.FileReaderSimulation;
import com.pjava.src.utils.Utils;
import com.pjava.src.utils.Utils.TimeoutCatch;

/**
 * This class is purely static. All {@link Element} when they update their
 * state, will call to this class. This class will also call the next update
 * after a timeout.
 * <p>
 * </p>
 * This class works for every element, even if they're not in the same related
 * component. Meaning you can't launch a new simulation if all component are
 * not stabilized. You can however add new element to the current simulation
 * whenever with {@link #addToCallStack(Element)}.
 */
public abstract class Synchronizer {
    /**
     * The simulation speed in Hertz. It will call "simulationSpeed" updates per
     * seconds. If simulation speed is negative or 0, it will call it as fast as
     * possible.
     *
     * By default, simulation speed is as fast a possible (-1).
     */
    private static int simulationSpeed = -1;
    /**
     * This a flag used to check if the simulation is running. In a running
     * simulation, {@link #updateSimulation()} will do nothing.
     */
    private static boolean simulationRunning = false;
    /**
     * Set the lever input to predeterminate state from a file step by step
     */
    private static FileReaderSimulation inputSimulator = null;
    /**
     * The next group of element to update. Each {@link Element#updateState()} will
     * add their own instance into this group. Then the group will update a cycle
     * between the two.
     * <p>
     * </p>
     * True updates {@link #nextUpdateGroupTrue}, false updates
     * {@link #nextUpdateGroupFalse}.
     * <p>
     * </p>
     * This value is changed internally with {@link #updateSimulation()}.
     */
    private static boolean groupOrder = true;
    /**
     * The group that is filling when {@link #groupOrder} is false. Complementary,
     * it
     * is emptying when {@link #groupOrder} is true.
     */
    private static HashSet<Element> nextUpdateGroupTrue = new HashSet<Element>();
    /**
     * The group that is filling when {@link #groupOrder} is true. Complementary,
     * it is emptying when {@link #groupOrder} is false.
     */
    private static HashSet<Element> nextUpdateGroupFalse = new HashSet<Element>();

    /**
     * Launch a simulation are {@link #nextUpdateGroupTrue} and
     * {@link #nextUpdateGroupFalse} are both empty. To add a new
     * {@link Element} to the call stack, use {@link #addToCallStack(Element)}.
     */
    public static void updateSimulation() {
        updateSimulation(false);
    }

    /**
     * The internal function to update the simulation. The given parameters enable
     * the class to know it's the internal timeout call.
     *
     * @param internal The flag to know if the function has been internally called
     *                 by the timeout.
     */
    private static void updateSimulation(boolean internal) {
        // return if a signal is already launched (the whole system is not yet stable)
        // and it's not an internal call (meaning we check for the next cycle)
        // and shoudlPropagate is false (meaning it should instantly run, even from
        // external)
        if (nextUpdateGroupFalse.size() != 0 && nextUpdateGroupTrue.size() != 0 &&
                simulationRunning && (!internal || !shouldPropagate())) {
            return;
        }
        // stop the simulation if both group are empty
        if (nextUpdateGroupFalse.size() == 0 && nextUpdateGroupTrue.size() == 0) {
            if (!shouldPropagate()) {
                simulationRunning = false;
            }
            return;
        }
        //TODO verif if it's good (Hugo pls) signed Axel
        if(inputSimulator != null && inputSimulator.simulationActivated){
            try{
                 inputSimulator.simulationOrganizer();
            }
            catch( Exception e){
                System.err.println(e);
            }
        }

        // calculte the time to the next update
        long nextUpdate = System.currentTimeMillis() + (shouldPropagate() ? 0 : hertzToMs(simulationSpeed));

        // call the elements updateState
        if (groupOrder) {
            nextUpdateGroupTrue.forEach(element -> {
                element.updateState();
            });
            nextUpdateGroupTrue.clear();
        } else {
            nextUpdateGroupFalse.forEach(element -> {
                element.updateState();
            });
            nextUpdateGroupFalse.clear();
        }
        groupOrder = !groupOrder;

        // calculate the amount of time left before next update
        long beforeNextUpdate = System.currentTimeMillis() - nextUpdate;

        // each call have been made, apply the timeout to the next cycle
        if (shouldPropagate() || beforeNextUpdate <= 0) {
            updateSimulation(true);
        } else {
            // updateState
            Utils.timeout(() -> {
                updateSimulation(true);
            }, beforeNextUpdate, new TimeoutCatch() {
                @Override
                public void handle(Exception e) {
                    throw new Error("Internal simulation update has been broken", e);
                }
            });
        }
    }

    /**
     * Add the element to the call stack. On the next simulation cycle, the
     * {@link Element#updateState()} will be called.
     *
     * @param element The element to add to the call stack.
     */
    public static void addToCallStack(Element element) {
        if (element == null) {
            return;
        }
        // if currently emptying one group, add to the other
        if (groupOrder) {
            nextUpdateGroupFalse.add(element);
        } else {
            nextUpdateGroupTrue.add(element);
        }
    }

    /**
     * Used to tell the {@link Element} whether to propagate with
     * {@link Element#updateState(boolean)}. Changing {@link #simulationSpeed} will
     * also change the behaviour of said functions.
     *
     * @return True is {@link #simulationSpeed} is lower or equal to 0.
     */
    public static boolean shouldPropagate() {
        return simulationSpeed <= 0;
    }

    /**
     * Convert a hertz value into the amount of miliseconds between two signal.
     *
     * @param hertz The hertz value to convert.
     * @return The interval in milisconds.
     * @throws IllegalArgumentException Throws if the given value is 0 (result would
     *                                  be infinity), or if the value is negative.
     */
    public static long hertzToMs(int hertz) throws IllegalArgumentException {
        if (hertz <= 0) {
            throw new IllegalArgumentException("Excpeted hertz to be greater than 0, received " + hertz);
        }
        return (long) ((1f / hertz) * 1000);
    }

    // #region Getters
    /**
     * Getter for {@link #simulationSpeed}.
     *
     * @return the simulationSpeed
     */
    public static int getSimulationSpeed() {
        return simulationSpeed;
    }

    public static FileReaderSimulation getInputSimulator() {
        return inputSimulator;
    }

    // #endregion

    // #region Setters
    /**
     * Simulation speed in Hertz. It is the amount of update to do in a second.
     * Negative or 0 value means as fast as possible.
     * <p>
     * </p>
     * If a simulation is running, it will change the speed.
     *
     * @param simulationSpeed the new simulationSpeed to set
     */
    public static void setSimulationSpeed(int simulationSpeed) {
        Synchronizer.simulationSpeed = simulationSpeed;
        // if always propagating, then the simulation is "running"
        simulationRunning = shouldPropagate();
    }

    public static void setInputSimulator(FileReaderSimulation newSimulator){
        Synchronizer.inputSimulator = newSimulator;
    }
    // #endregion
}
