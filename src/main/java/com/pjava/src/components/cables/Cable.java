package com.pjava.src.components.cables;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

import com.pjava.src.components.Gate;

public class Cable extends BitSet {
    // unique identifier to distinguish cable
    private final UUID uuid = UUID.randomUUID();

    private Boolean powered = false;

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

    public void updateState() {
        // if multiple input, add (the "or" bitwise) the result
        this.clear();
        for (Gate gate : inputGate) {
            if (gate != null) {
                if (!gate.getPowered()) {
                    setPowered(false);
                } else {
                    this.or(gate.getState());
                }
            }
        }

        // then call all output
        if (outputGate.size() == 0)
            return;
        outputGate.forEach(gate -> {
            if (gate != null) {
                gate.updateState();
            }
        });
    }

    // #region Getters
    /**
     * Returns the unique id to distinguish this cable, while keeping the equals from the BitSet.
     * @return The unique id.
     */
    public final UUID uuid() {
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
    public void setPowered(Boolean powered) {
        this.powered = powered;
    }

    public void setBusSize(int busSize) {
        this.busSize = busSize;
        // grow the internal BitSet
        this.set(busSize - 1);
        this.clear(busSize - 1);
    }
    // #endregion
}
