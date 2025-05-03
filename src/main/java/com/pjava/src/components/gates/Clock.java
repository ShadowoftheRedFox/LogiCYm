package com.pjava.src.components.gates;

import java.util.BitSet;

import com.pjava.src.components.Gate;

/**
 * A specific type of gate that can periodically send an update. It doesn't have
 * any inputs, and has only one ouput bus of size 1. This gate can be
 * enabled/disabled, and can be manually triggered. The update interval can be
 * set when instantiating the class or later. It is always powered.
 *
 * @see #cycleSpeed
 * @see #timeCycle()
 */
public class Clock extends Gate {
    /**
     * Current state of the clock.
     */
    private BitSet state = new BitSet(1);

    /**
     * The cycle speed between states, in ms.
     */
    private long cycleSpeed = 100l;

    /**
     * The timestamp of the last cycle.
     * It prevent using a sleep inside a loop.
     */
    private long lastCycle = System.currentTimeMillis();

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
        super(new int[] {}, new int[] { 1 });
        setPowered(true);
        setCycleSpeed(cycleSpeed);
    }

    /**
     * Make a cycle instantaneously.
     */
    public void instantCycle() {
        state.flip(0);

        lastCycle = System.currentTimeMillis();
        updateState();
    }

    /**
     * Cycle accordingly to the given {@link #cycleSpeed} and {@link #enabled}.
     */
    public void timeCycle() {
        if (!enabled)
            return;

        if (System.currentTimeMillis() - cycleSpeed >= lastCycle) {
            instantCycle();
        }
    }

    /**
     * Return the current state, after a {@link #timeCycle()}.
     * {@inheritDoc}
     */
    @Override
    public BitSet getState() {
        timeCycle();
        return state;
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
     *
     * @param enabled Whether to enable or disable the clock.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    // #endregion
}
