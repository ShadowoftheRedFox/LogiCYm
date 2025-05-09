package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.Utils;

/**
 * Used to link multiple {@link Gate}s together.
 */
public class Cable extends BitSet implements Component {
    /**
     * A unique id to differentiate this instance from other components.
     */
    private Integer uuid = Utils.runtimeID();

    /**
     * Indicate whether this cable is powered. An unpowered cable means either that
     * it doesn't have any input, or that a previous {@link Gate} is also unpowered.
     * An unpowered cable does not transmit nor emit updates.
     */
    private boolean powered = true;

    /**
     * The previous state of the gate. Should always be a clone.
     */
    private BitSet oldState = new BitSet(1);

    /**
     * The size of the bus. It must be a power of 2.
     *
     * @see com.pjava.src.utils.Utils#isPower2(int)
     * @see com.pjava.src.utils.Utils#pow2(int)
     */
    private int busSize = 1;

    /**
     * The input gates.
     * TODO maybe pass this private later
     */
    public ArrayList<Gate> inputGate = new ArrayList<Gate>();

    /**
     * The output gates.
     * TODO maybe pass this private later
     */
    public ArrayList<Gate> outputGate = new ArrayList<Gate>();

    /**
     * Create a new cable with the specified bus size.
     *
     * @param busSize The size of the cable bus.
     * @throws BusSizeException Throws error from {@link #setBusSize(int)}.
     */
    public Cable(Integer busSize) throws BusSizeException {
        super(busSize);
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
            // TODO special case for Input and Ouput
            return;
        }

        // if multiple input, add (the "or" bitwise) the result
        this.clear();
        for (Gate gate : getInputGate()) {
            BitSet gateState = gate.getState();
            if (gate != null &&
                    gate.getPowered() &&
                    gateState != null) {
                this.or(gateState);
            }
        }

        // // another early return
        if (oldState.equals(this)) {
            // set to the old state
            this.clear();
            this.or(oldState);
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
        this.clear();
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
                if (gate != null) {
                    gate.updatePower();
                }
            }
        }

    }

    // #region Getters
    /**
     * Returns the unique id to distinguish this gate from other components.
     *
     * @return The unique id.
     */
    public final Integer uuid() {
        return uuid;
    }

    /**
     * Getter for {@link #powered}.
     *
     * @return If the cable is powered or not.
     */
    public boolean getPowered() {
        return powered;
    }

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

    /**
     * Get the bit set instance of this cable. This is a clone of the state.
     *
     * @return A new bit set, equals to this cable bit set.
     */
    public BitSet getState() {
        return BitSet.valueOf(this.toByteArray());
    }
    // #endregion

    // #region Setters
    /**
     * The setter for {@link #powered}.
     * Set whether the cable is powered or not.
     *
     * @param powered True if powered, false if not.
     */
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

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
        if (busSize <= 0 || !Utils.isPower2(busSize)) {
            throw BusSizeException.fromName("bus size", busSize);
        }

        this.busSize = busSize;
    }
    // #endregion

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cable) {
            return ((Cable) obj).uuid.equals(uuid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return uuid();
    }

    @Override
    public String toString() {
        return "Cable " + uuid.toString();
    }
}
