package com.pjava.src.components.input;

import java.util.BitSet;

import org.json.JSONObject;

/**
 * A button input.
 * Button can be pressed and is released after a delay has passed.
 * They can be inverted to send a signal until pressed.
 */
public class Button extends Input {
    /**
     * Delay the button is "waiting" before it come back to it's original state.
     * Delay is in miliseconds.
     */
    private long delay = 100;

    /**
     * The timestamp of the last cycle.
     * It prevent using a sleep inside a loop.
     */
    private long lastCycle = 0l;

    /**
     * Create a new button with 100ms of delay.
     *
     * @throws Exception Should not throw here.
     * @see #setDelay(long)
     */
    public Button() throws Exception {
        this(100);
    }

    /**
     * Create a new button with the given delay.
     *
     * @param delay    The given delay in miliseconds.
     * @throws Exception Throws when delay is lesser or equals to 0.
     * @see #setDelay(long)
     */
    public Button(long delay) throws Exception {
        super(new int[] { 1 });
        setDelay(delay);
    }

    /**
     * Press the button. It will switch state for a period of time equals to
     * {@link #delay}.
     */
    public void press() {
        if (System.currentTimeMillis() - lastCycle < delay) {
            return;
        }

        lastCycle = System.currentTimeMillis();
        state.set(0, true);
        updateState();
    }

    /**
     * A system check to release the button after a press. Return the button to it's
     * original state after a period of time equals to
     * {@link #delay}.
     */
    public void release() {
        if (System.currentTimeMillis() - lastCycle >= delay) {
            state.set(0, false);
            updateState();
        }
    }

    // #region Getters
    @Override
    public BitSet getState() {
        return (BitSet) state.clone();
    }

    /**
     * Getter for {@link #delay}.
     *
     * @return The delay of the button before it comes back to it's original state,
     *         in miliseconds.
     */
    public long getDelay() {
        return delay;
    }

    // #endregion

    // #region Setters
    /**
     * Setter for {@link #delay}.
     *
     * @param delay The new delay of the button before it comes back to it's
     *              original state, in miliseconds.
     * @throws Exception Throws when delay is lesser or equals to 0.
     */
    public void setDelay(long delay) throws Exception {
        if (delay <= 0) {
            throw new Exception("Expected delay to be greater than 0, recived: " + delay);
        }
        this.delay = delay;
    }

    // #endregion

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("delay", delay);
        return json;
    }

}
