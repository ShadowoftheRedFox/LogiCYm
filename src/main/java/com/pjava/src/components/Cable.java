package com.pjava.src.components;

import java.util.BitSet;

import com.pjava.src.components.cables.Splitter;
import com.pjava.src.components.gates.Schema;
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
    private Integer busSize = 1;

    /**
     * The input gate.
     */
    protected Gate inputGate = null;

    /**
     * The output gate.
     */
    protected Gate outputGate = null;

    /**
     * FIXME javadoc
     */
    protected int inputPort = -1;

    /**
     * FIXME javadoc
     */
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
     * This function is called when inputs state changes.
     */

    @Override
    public void updateState() {
        // early returns
        if (outputGate == null || getPowered() == false) {
            return;
        }

        // if multiple input, add (the "or" bitwise) the result
        BitSet state = new BitSet();
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
        } else  {
            BitSet gateState = inputGate.getState();
            if (gateState != null) {
                state.or(gateState);
            }
        }

        // another early return
        if (oldState.equals(state)) {
            return;
        }

        setState(state);
        setOldState();

        // DEBUG if anything break, it's here 200%

        // If the output gate is a Schema, we use it like a bridge to update the inner
        // or outer gate at the right port.
        if (outputGate instanceof Schema) {
            // correspondingCable will allow a seemless updade of state between the circuit
            // gates and the schema's inner circuit gates
            Cable correspondingCable = null;
            Schema schema = (Schema) outputGate;

            // we try to find out if this cable is an input cable or an inner output cable :
            if (this.equals(schema.getInputCable().get(outputPort))) {
                correspondingCable = schema.getInnerInputCable().get(outputPort);
            } else if (this.equals(schema.getInnerOutputCable().get(outputPort))) {
                correspondingCable = schema.getOutputCable().get(outputPort);
            }

            if (correspondingCable == null) {
                throw new Error(String.format("Cable not found in schema gate '%s', input or inner output port '%d'",
                        schema.getName(), outputPort));
            }

            if (correspondingCable.getBusSize() != this.busSize) {
                throw new Error("Shema's corresponding cable bus size should be the same as this cable");
            }

            // we directly pass our state to the corresponding cable
            correspondingCable.setState(getState());

            // We will then update the output gate of the corresponding cable
            if (correspondingCable.oldState.equals(correspondingCable.getState())) {
                return;
            }
            correspondingCable.setOldState();

            Synchronizer.addToCallStack(correspondingCable.outputGate);
        } else {
            // we update the output gate of this cable
        }
        Synchronizer.addToCallStack(outputGate);
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

    /**
     * Getter for {@link #inputPort}.
     *
     * @return The current input port.
     */
    public int getInputPort() {
        return inputPort;
    }

    /**
     * Getter for {@link #outputPort}.
     *
     * @return The current output port.
     */
    public int getOutputPort() {
        return outputPort;
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

    /**
     * FIXME javadoc
     * Sets the input port index for this cable connection
     * BUG no upper bound
     *
     * @param portIndex the index of the input port (must be >= 0)
     * @throws IndexOutOfBoundsException if portIndex is negative
     */
    public void setInputPort(int portIndex) throws IndexOutOfBoundsException {
        if (portIndex < 0) {
            throw new IndexOutOfBoundsException("negative index");
        }

        this.inputPort = portIndex;
    }

    /**
     * FIXME javadoc
     * Sets the output port index for this cable connection.
     * BUG no upper bound
     *
     * @param portIndex the index of the output port (must be >= 0)
     * @throws IndexOutOfBoundsException if portIndex is negative
     */

    public void setOutputPort(int portIndex) throws IndexOutOfBoundsException {
        if (portIndex < 0) {
            throw new IndexOutOfBoundsException("Negative index.");
        }

        this.outputPort = portIndex;
    }
    // #endregion
}
