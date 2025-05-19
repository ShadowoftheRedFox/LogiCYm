package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;

/**
 * A not gate. Return the opposite state of its output.
 */
public class Not extends Gate {
    /**
     * Create a new Not gate with bus sizes of 1.
     */
    public Not() {
        this(1);
    }

    /**
     * Create a new Not gate with bus sizes provided.
     *
     * @param busSize Size of the bus for all input and output pins.
     * @see Gate
     */
    public Not(int busSize) {
        super(new int[] { busSize }, new int[] { busSize });
    }


    @Override
    public BitSet getState() {
        ArrayList<Cable> inputs = getInputCable();

        if (inputs.get(0) == null) {
            return null;
        }

        BitSet result = inputs.get(0).getState();
        result.flip(0);

        return result;
    }

    /**
     * Create an exact copy of this not instance in a new one. Connected
     * referenced cables are not cloned.
     *
     * @return The newly cloned not.
     */
    @Override
    public Not clone() {
        try {
            Not clone = new Not(getInputBus()[0]);
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
