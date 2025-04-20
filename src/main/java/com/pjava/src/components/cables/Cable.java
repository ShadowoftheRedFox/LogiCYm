package com.pjava.src.components.cables;

import java.util.ArrayList;

import com.pjava.src.components.Gate;

public class Cable {
    private Boolean powered = false;

    /**
     * The "size" of the cable, the number of bit remembered.
     */
    private Integer busSize = 1;

    /**
     * State of the cable.
     */
    private int state = 0;

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
        setBusSize(busSize);
    }

    public void updateState() {
        state = inputGate.get(0).getState();
        // if multiple input, add (the "or" bitwise) the result
        inputGate.forEach(gate -> {
            if (!gate.getPowered()) {
                setPowered(false);
            }
            state |= gate.getState();
        });

        // then call all output
        if (outputGate.size() == 0)
            return;
        outputGate.forEach(gate -> {
            gate.updateState();
        });
    }

    // #region Getters
    public Boolean getPowered() {
        return powered;
    }

    public Integer getBusSize() {
        return busSize;
    }

    public int getState() {
        return state;
    }

    /**
     * Get the value of the index th bit in the state.
     *
     * @param index The index to get the bit, 0 being the right most bit. Min value
     *              is 0, max is {@link #busSize}-1.
     * @return The value, 0 or 1, of the given index. Or -1 if index is invalid.
     */
    public int getState(int index) {
        if (index < 0 || index >= busSize)
            return -1;
        return (state >> index) & 1;
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

    /**
     * Change the state of the cable. Also propagate teh change.
     *
     * @param state The new state of the cable.
     */
    public void setState(int state) {
        this.state = state;
        // TODO update state if simulation running
        updateState();
    }

    /**
     * Set the bit at index to state.
     *
     * @param index The index to get the bit, 0 being the right most bit. Min value
     *              is 0, max is {@link #busSize}-1.
     * @param state The value of the bit, 0 or 1.
     */
    public void setState(int index, int state) {
        assert state == 0 || state == 1;
        if (index >= 0 && index < busSize) {
            if (state == 0) {
                // << move the bit 0 for index times, the ~ is the bitwise not,
                // and & is the bitwise &
                // shorthand for this.state = this.state & ~(1 << index)
                this.state &= ~(1 << index);
            } else if (state == 1) {
                // << move the bit 1 for index times, and the | is the bitwise or
                // shorthand for this.state = this.state | (1 << index)
                this.state |= 1 << index;
            }
        }
    }

    /**
     * Toggle (invert) the bit at the index place.
     *
     * @param index The index to get the bit, 0 being the right most bit. Min value
     *              is 0, max is {@link #busSize}-1.
     */
    public void toggleState(int index) {
        if (index >= 0 && index < busSize) {
            // shorthand for state = state ^ (1 << getState(index))
            state ^= 1 << getState(index);
        }
    }

    public void setBusSize(Integer busSize) {
        if (busSize <= 0)
            throw new Error("busSize is invalid");
        // check if the weakest bit is 1, if yes, that means the number is not pair
        if ((busSize & 1) != 0 && busSize != 1)
            throw new Error("busSize is not pair or 1");
        // restrain the max size, because we use an integer as our bus state
        if (busSize > 32)
            throw new Error("busSize is too big");

        this.busSize = busSize;
    }
    // #endregion
}
