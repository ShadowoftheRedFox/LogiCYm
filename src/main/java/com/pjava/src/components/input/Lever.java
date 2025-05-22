package com.pjava.src.components.input;

import java.util.BitSet;

import org.json.JSONObject;

/**
 * A lever input. Emit or not a signal {@link #flip()} is called.
 */
public class Lever extends Input {

    /**
     * Create a new lever without emitting a signal.
     */
    public Lever() {
        this(false);
    }

    /**
     * Create a new lever, emitting a signal if stated with the given argument.
     *
     * @param flipped If the lever should emit a signal.
     */
    public Lever(boolean flipped) {
        super(new int[] { 1 });
        state.set(0, flipped);
    }


    /**
     * Flip the lever.
     */
    public void flip() {
        state.flip(0);
        updateState();
    }

    // #region Getters
    @Override
    public BitSet getState() {
        return (BitSet) state.clone();
    }
    // #endregion

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("flipped", this.getState(0));
        return json;
    }
}
