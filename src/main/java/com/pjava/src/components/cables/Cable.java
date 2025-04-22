package com.pjava.src.components.cables;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Gate;

public class Cable extends BitSet {
    private Boolean powered = false;

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
    }

    public void updateState() {
        // if multiple input, add (the "or" bitwise) the result
        this.clear();
        inputGate.forEach(gate -> {
            if (gate != null) {
                if (!gate.getPowered()) {
                    setPowered(false);
                }
                this.or(gate.getState());
            }
        });

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
    public Boolean getPowered() {
        return powered;
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
    // #endregion
}
