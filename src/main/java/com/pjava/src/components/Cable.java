package com.pjava.src.components;

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
     * The input gate.
     */
    protected Gate inputGate = null;

    /**
     * The output gate.
     */
    protected Gate outputGate = null;

    protected int inputPort = -1;

    protected int outputPort = -1;

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
    @Override
    public void updateState() {
        updateState(true);
    }

    /**
     * This function is called when inputs state changes.
     *
     * @param propagate Whether or not to propagate the changes to the outputs.
     */
    @Override
    public void updateState(boolean propagate) {
        // early returns
        if (outputGate == null || getPowered() == false) {
            return;
        }

        // if multiple input, add (the "or" bitwise) the result
        state.clear();
        // special case if gate in a splitter
        if (inputGate instanceof Splitter) {
            BitSet gateState;
            try {
                // get the correct state specific to this cable
                gateState = ((Splitter) inputGate).getState(this);
            } catch (Exception e) {
                throw new Error(e);
            }
            if (gateState != null) {
                state.or(gateState);
            }
        } else {
            BitSet gateState = inputGate.getState();
            if (gateState != null) {
                state.or(gateState);
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
        if (propagate && outputGate != null) {
            outputGate.updateState();
        }
    }

    /**
     * Should be called when input/output changes.
     * Update the power of himself and its output accordingly.
     */
    @Override
    public void updatePower() {
        // send update to output when powered changed
        if ((inputGate == null && getPowered()) ||
                (inputGate != null && getPowered() != inputGate.getPowered())) {

            setPowered(inputGate != null && inputGate.getPowered());

            if (outputGate != null) {
                outputGate.updatePower();
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
     * Getter for {@link #inputGate}.
     *
     * @return The input gate.
     */
    public Gate getInputGate() {
        return inputGate;
    }

    /**
     * Getter for {@link #outputGate}.
     *
     * @return The output gate.
     */
    public Gate getOutputGate() {
        return outputGate;
    }

    public int getInputPort() {
        return inputPort;
    }

    public int getOutputPort() {
        return inputPort;
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

    public void setInputPort(int portIndex) throws IndexOutOfBoundsException{
        if(portIndex < 0){
            throw  new IndexOutOfBoundsException("negative index");
        }

        this.inputPort = portIndex;
    }

    public void setOutputPort(int portIndex) throws IndexOutOfBoundsException {
        if (portIndex < 0) {
            throw new IndexOutOfBoundsException("Negative index.");
        }

        this.outputPort = portIndex;
    }
    // #endregion
}
