package com.pjava.src.components.gates;

import java.util.BitSet;

import com.pjava.src.components.Gate;

public class Clock extends Gate {
    /**
     * Current state of the clock.
     */
    private BitSet state = new BitSet(1);

    /**
     * The cycle speed between states, in ms.
     */
    private Long cycleSpeed = 100l;

    /**
     * The timestamp of the last cycle.
     * It prevent using a sleep inside a loop.
     */
    private Long lastCycle = System.currentTimeMillis();

    /**
     * If the clock is enabled, it will periodically change states depending on
     * {@link #cycleSpeed}.
     */
    private Boolean enabled = false;

    public Clock() {
        setPowered(true);
    }

    public Clock(Long cycleSpeed) {
        setCycleSpeed(cycleSpeed);
    }

    /**
     * Make a cycle instantaneously.
     */
    public void instantCycle() {
        if (state.get(0)) {
            state.clear(0);
        } else {
            state.set(0);
        }

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

    @Override
    public BitSet getState() {
        return state;
    }

    // #region Getters
    public Long getCycleSpeed() {
        return cycleSpeed;
    }

    public Boolean getEnabled() {
        return enabled;
    }
    // #endregion

    // #region Setters
    public void setCycleSpeed(Long cycleSpeed) {
        if (cycleSpeed > 0) {
            this.cycleSpeed = cycleSpeed;
        }
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    // #endregion
}
