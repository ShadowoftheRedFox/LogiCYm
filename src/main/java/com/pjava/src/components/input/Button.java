package com.pjava.src.components.input;

import java.util.BitSet;

/**
 * A button input.
 * Button can be pressed and is released after a delay has passed.
 * They can be inverted to send a signal until pressed.
 */
public class Button extends Input {
    /**
     * Whether the button is inverted.
     * False means the button send a signal when pressed, otherwise it does not send
     * a signal.
     * True is the opposite of the previously stated false.
     */
    private boolean inverted = false;

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
        this(100, false);
    }

    /**
     * Create a new button with the given delay.
     *
     * @param delay The given delay in miliseconds.
     * @throws Exception Throws when delay is lesser or equals to 0.
     * @see #setDelay(long)
     */
    public Button(long delay) throws Exception {
        this(delay, false);
    }

    /**
     * Create a new button with the given invertion.
     *
     * @param inverted True if the button is inverted.
     * @throws Exception Should not throw here.
     */
    public Button(boolean inverted) throws Exception {
        this(100l, inverted);
    }

    /**
     * Create a new button with the given delay, and whether or not it is inverted.
     *
     * @param delay    The given delay in miliseconds.
     * @param inverted True if the button is inverted.
     * @throws Exception Throws when delay is lesser or equals to 0.
     * @see #setDelay(long)
     */
    public Button(long delay, boolean inverted) throws Exception {
        super(new int[] { 1 });
        setDelay(delay);
        setInverted(inverted);
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
        state.set(0, !inverted);
        updateState();
    }

    /**
     * A system check to release the button after a press. Return the button to it's
     * original state after a period of time equals to
     * {@link #delay}.
     */
    public void release() {
        if (System.currentTimeMillis() - lastCycle >= delay && getState(0) != inverted) {
            state.set(0, inverted);
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

    /**
     * Getter for {@link #inverted}.
     *
     * @return Whether or not the button is inverted.
     */
    public boolean getInverted() {
        return inverted;
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

    /**
     * Setter for {@link #inverted}. Changing the inverted state will send a
     * {@link #updateState()} signal as well as changing the internal state.
     *
     * @param inverted Whether or not to invert the button.
     */
    public void setInverted(boolean inverted) {
        if (this.inverted != inverted) {
            this.inverted = inverted;
            state.flip(0);
            updateState();
        }
    }
    // #endregion
}
