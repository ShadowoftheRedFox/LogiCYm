package com.pjava.src.components.gates;

import com.pjava.src.config.States;

public class Clock {
    /**
     * Current state of the clock.
     */
    private States state = States.Low;

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
    }

    public Clock(Long cycleSpeed) {
        setCycleSpeed(cycleSpeed);
    }

    /**
     * Make a cycle instantaneously.
     */
    public void instantCycle() {
        if (state == States.Low) {
            state = States.High;
        } else {
            state = States.Low;
        }

        lastCycle = System.currentTimeMillis();
        // TODO update output
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

    // #region Getters
    public Long getCycleSpeed() {
        return cycleSpeed;
    }

    public States getState() {
        return state;
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
