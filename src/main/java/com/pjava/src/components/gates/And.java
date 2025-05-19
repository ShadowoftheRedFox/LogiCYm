package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;

/**
 * The And gate.
 * If any of the two inputs are unpowered, then the output is unpowered.
 * Otherwise, apply the "and" logic to the input and output the result.
 */
public class And extends Gate {
    /**
     * Create a new And gate with bus sizes of 1.
     *
     * @see Gate
     */
    public And() {
        this(1);
    }

    /**
     * Create a new And gate with bus sizes provided.
     *
     * @param busSize Size of the bus for all input and output pins.
     * @see Gate
     */
    public And(int busSize) {
        super(new int[] { busSize, busSize }, new int[] { busSize });
    }


    @Override
    public BitSet getState() {
        ArrayList<Cable> inputs = getInputCable();

        if (inputs.get(0) == null || inputs.get(1) == null) {
            return null;
        }

        BitSet result = inputs.get(0).getState();
        result.and(inputs.get(1).getState());

        return result;
    }

    /**
     * Create an exact copy of this and instance in a new one. Connected
     * referenced cables are not cloned.
     *
     * @return The newly cloned and.
     */
    @Override
    public And clone() {
        try {
            And clone = new And(getInputBus()[0]);
            clone.setInputCable(getInputCable());
            clone.setOutputCable(getOutputCable());
            clone.state = (BitSet) state.clone();
            clone.setPowered(getPowered());
            return clone;
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
