package com.pjava.src.components.cables;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

import com.pjava.src.components.Gate;
import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.Utils;

/**
 * Used to link multiple {@link Gate}s together.
 */
public class Cable extends BitSet {
    // unique identifier to distinguish cable
    /**
     * Unique identifier to distinguish cables, while keeping the
     * {@link java.util.BitSet#equals(Object)} available.
     */
    private final int uuid = Utils.runtimeID();
    // private final UUID uuid = UUID.randomUUID();

    /**
     * Indicate whether this cable is powered. An unpowered cable means either that
     * it doesn't have any input, or that a previous {@link Gate} is also unpowered.
     * An unpowered cable does not transmit nor emit updates.
     */
    private boolean powered = false;

    /**
     * The previous state of the gate. Should always be a clone.
     */
    private BitSet oldState = new BitSet();

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
     * This function is called when inputs state changes.
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
        // if multiple input, add (the "or" bitwise) the result
        // for the power, if one gate is powered, then the cable is powered
        int countPoweredGates = 0;
        this.clear();
        for (Gate gate : inputGate) {
            if (gate != null && gate.getPowered()) {
                countPoweredGates++;
                this.or(gate.getState());
            }
        }
        if (powered && countPoweredGates == 0) {
            System.out.println("Powering DOWN cable " + uuid);
        } else if (!powered && countPoweredGates != 0) {
            System.out.println("Powering UP cable " + uuid);
        }
        powered = countPoweredGates != 0;

        // then call all output
        if (outputGate.size() == 0 || !getPowered() || oldState.equals(this)) {
            // TODO special case for Input and Ouput
            System.out.println("Early return");
            return;
        }

        setOldState();
        if (propagate) {
            outputGate.forEach(gate -> {
                if (gate != null) {
                    gate.updateState();
                }
            });
        }
    }

    // #region Getters
    /**
     * Returns the unique id to distinguish this cable, while keeping the equals
     * from the BitSet.
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
    public Boolean getPowered() {
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
    private void setOldState() {
        this.oldState = BitSet.valueOf(this.toByteArray());
    }

    /**
     * Setter for {@link #busSize}. It reset the current value held in the cable
     * bus.
     *
     * @param busSize The new bus size.
     * @throws BusSizeException Throw when the given size is equal or below 0, not a
     *                          power of 2, or greater than 32.
     */
    public void setBusSize(int busSize) throws BusSizeException {
        if (busSize <= 0 || !Utils.isPower2(busSize)) {
            throw new BusSizeException("Expected a bus size between 1 and 32, received: " + busSize);
        }

        this.busSize = busSize;
        // grow the internal BitSet
        this.set(busSize - 1);
        this.clear(busSize - 1);
    }
    // #endregion
}
