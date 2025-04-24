package com.pjava.src.components.cables;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

import com.pjava.src.components.Gate;
import com.pjava.src.utils.Utils;

public class Cable extends BitSet {
    // unique identifier to distinguish cable
    // private final UUID uuid = UUID.randomUUID();
    private final int uuid = Utils.runtimeID();

    private boolean powered = false;

    /**
     * The previous state of the gate. Should always be a clone.
     */
    private BitSet oldState = new BitSet();

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

    public Cable(Integer busSize) {
        super(busSize);
        this.busSize = busSize;
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

    public Boolean getPowered() {
        return powered;
    }

    public int getBusSize() {
        return busSize;
    }

    public ArrayList<? extends Gate> getInputGate() {
        return inputGate;
    }

    public ArrayList<? extends Gate> getOutputGate() {
        return outputGate;
    }
    // #endregion

    // #region Setters
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    /**
     * Internal function that set the oldState to a clone of the current state.
     */
    private void setOldState() {
        this.oldState = BitSet.valueOf(this.toByteArray());
    }

    public void setBusSize(int busSize) {
        this.busSize = busSize;
        // grow the internal BitSet
        this.set(busSize - 1);
        this.clear(busSize - 1);
    }
    // #endregion
}
