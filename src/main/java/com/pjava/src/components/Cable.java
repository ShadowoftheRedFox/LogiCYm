package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.cables.Splitter;
import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.Utils;

/**
 * Used to link multiple {@link Gate}s together.
 */
public class Cable extends Element {

    /**
     * The previous state of the gate. Should always be a clone.
     */
    private BitSet oldState = new BitSet(1);

    /**
     * The size of the bus. It must be a power of 2.
     *
     * @see Utils#isPower2(int)
     * @see Utils#pow2(int)
     */
    private int busSize = 1;

    /**
     * The input gates.
     */
    protected ArrayList<Gate> inputGate = new ArrayList<Gate>();

    /**
     * The output gates.
     */
    protected ArrayList<Gate> outputGate = new ArrayList<Gate>();

    /**
     * Create a new cable with the specified bus size.
     *
     * @param busSize The size of the cable bus.
     * @throws BusSizeException Throws error from {@link #setBusSize(int)}.
     */
    public Cable(int busSize) throws BusSizeException {
        setBusSize(busSize);
    }

    /**
     * This function is called when inputs state change.
     * Equivalent of {@code updateState(true)} ({@link #updateState(boolean)}).
     */
    public void updateState() {
        updateState(true);
    }

    /**
     * This function is called when inputs state changes.
     *
     * @param propagate Whether or not to propagate the changes to the outputs.
     */
    public void updateState(boolean propagate) {
        // early returns
        if (getOutputGate().size() == 0 || getPowered() == false) {
            return;
        }

        // if multiple input, add (the "or" bitwise) the result
        state.clear();
        for (Gate gate : getInputGate()) {
            // special case if gate in a splitter
            if (gate instanceof Splitter) {
                BitSet gateState;
                try {
                    // get the correct state specific to this cable
                    gateState = ((Splitter) gate).getState(this);
                } catch (Exception e) {
                    throw new Error(e);
                }
                if (gateState != null) {
                    state.or(gateState);
                }
            } else {
                BitSet gateState = gate.getState();
                if (gate != null &&
                        gate.getPowered() &&
                        gateState != null) {
                    state.or(gateState);
                }
            }
        }

        // // another early return
        if (oldState.equals(this.getState())) {
            // set to the old state
            state.clear();
            state.or(oldState);
            return;
        }

        // then call all output
        setOldState();
        if (propagate) {
            getOutputGate().forEach(gate -> {
                if (gate != null) {
                    gate.updateState();
                }
            });
        }
    }

    /**
     * Should be called when input/output changes.
     * Update the power of himself and its output accordingly.
     */
    public void updatePower() {
        // if at least one gate is powered, then the cable is powered
        int countPoweredGates = 0;
        for (Gate gate : getInputGate()) {
            if (gate != null && gate.getPowered()) {
                countPoweredGates++;
            }
        }

        // send update to output when powered changed
        if ((getPowered() && countPoweredGates == 0) ||
                (!getPowered() && countPoweredGates != 0)) {

            setPowered(countPoweredGates != 0);

            for (Gate gate : getOutputGate()) {
                if (gate != null && gate.getPowered() != getPowered()) {
                    gate.updatePower();
                }
            }
        }

    }

    // #region Getters
    /**
     * Getter for {@link #busSize}.
     *
     * @return The size of the cable bus.
     */
    public int getBusSize() {
        return busSize;
    }

    /**
     * Get the number of gates connected as input for this cable.
     *
     * @return The number of input gates.
     */
    public Integer getInputNumber() {
        return inputGate.size();
    }

    /**
     * Get the number of gates connected as output for this cable.
     *
     * @return The number of output gates.
     */
    public Integer getOutputNumber() {
        return outputGate.size();
    }

    /**
     * Getter for {@link #inputGate}.
     *
     * @return The input gate array.
     */
    public ArrayList<? extends Gate> getInputGate() {
        return inputGate;
    }

    /**
     * Getter for {@link #outputGate}.
     *
     * @return The output gate array.
     */
    public ArrayList<? extends Gate> getOutputGate() {
        return outputGate;
    }
    // #endregion

    // #region Setters
    /**
     * The setter for {@link #oldState}.
     * Internal function that set the oldState to a clone of the current state.
     */
    public void setOldState() {
        this.oldState = getState();
    }

    /**
     * Setter for {@link #busSize}. It reset the current value held in the cable
     * bus.
     *
     * @param busSize The new bus size.
     * @throws BusSizeException Throw when the given size is equal or below 0, not a
     *                          power of 2, or greater than 32.
     */
    protected void setBusSize(int busSize) throws BusSizeException {
        if (BusSizeException.isBusSizeException(busSize)) {
            throw BusSizeException.fromName("bus size", busSize);
        }

        this.busSize = busSize;
    }
    // #endregion
}
