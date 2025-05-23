package com.pjava.src.components.input;

import java.util.BitSet;

import org.json.JSONObject;

import com.pjava.src.components.Synchronizer;

/**
 * A specific type of gate that can periodically send an update. It doesn't have
 * any inputs, and has only one output bus of size 1. This gate can be
 * enabled/disabled, and can be manually triggered. The update interval can be
 * set when instantiating the class or later. It is always powered.
 *
 * @see #cycleSpeed
 * @see #timeCycle()
 */
public class Clock extends Input {
    /**
     * The cycle speed between states, in ms.
     */
    private long cycleSpeed = 100l;

    /**
     * The timestamp of the last cycle.
     * It prevent using a sleep inside a loop.
     */
    private long lastCycle = 0l;

    /**
     * If the clock is enabled, it will periodically change states depending on
     * {@link #cycleSpeed}. {@link #timeCycle()} needs to be called for the
     * {@link #state} to change if enabled.
     */
    private boolean enabled = false;

    /**
     * Create a new clock with the default interval of 100ms.
     */
    public Clock() {
        this(100l);
    }

    /**
     * Create a new clock with the given interval.
     *
     * @param cycleSpeed The interval between each cycle, in ms.
     */
    public Clock(Long cycleSpeed) {
        super(new int[] { 1 });
        setCycleSpeed(cycleSpeed);
    }

    /**
     * Make a cycle instantaneously.
     */
    public void instantCycle() {
        BitSet state = getState();
        state.flip(0);
        setState(state);

        lastCycle = System.currentTimeMillis();
        // if clock disabled but this method has been called, update
        // else it's a synchronizer call, to add itself for the next call to happen
        if (!enabled) {
            Synchronizer.updateSimulation();
        } else {
            Synchronizer.addToCallStack(this);
        }
    }

    /**
     * Cycle accordingly to the given {@link #cycleSpeed} and {@link #enabled}.
     *
     * @return True if a cycle has been made, false otherwise
     */
    public boolean timeCycle() {
        if (!enabled) {
            return false;
        } else if (System.currentTimeMillis() - lastCycle >= cycleSpeed) {
            instantCycle();
            return true;
        }
        return false;
    }

    @Override
    public void updateState() {
        if (!enabled) {
            return;
        }
        timeCycle();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("cycleSpeed", cycleSpeed);
        return json;
    }

    // #region Getters
    /**
     * Getter for {@link #cycleSpeed}.
     *
     * @return The interval between cycle, in ms.
     */
    public Long getCycleSpeed() {
        return cycleSpeed;
    }

    /**
     * Getter for {@link #enabled}.
     *
     * @return Whether the clock is enabled or not.
     */
    public Boolean getEnabled() {
        return enabled;
    }
    // #endregion

    // #region Setters
    /**
     * Setter for {@link #cycleSpeed}.
     *
     * @param cycleSpeed The new cycle speed in ms. Does not change if it is equal
     *                   or below 0.
     */
    public void setCycleSpeed(long cycleSpeed) {
        if (cycleSpeed > 0) {
            this.cycleSpeed = cycleSpeed;
        }
    }

    /**
     * Setter for {@link #enabled}.
     * If enabled is set to true, it add itself to the synchronizer.
     *
     * @param enabled Whether to enable or disable the clock.
     * @see Synchronizer
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            Synchronizer.addToCallStack(this);
        }
    }
    // #endregion
}
