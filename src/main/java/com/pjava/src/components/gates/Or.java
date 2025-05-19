package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;

/**
 * The Or gate.
 * If any of the two inputs are unpowered, then the output is unpowered.
 * Otherwise, apply the "or" logic to the input and output the result.
 * Only has one input and one output, both of bus size of 1.
 */
public class Or extends Gate {
    /**
     * Create a new OR gate with bus sizes of 1.
     */
    public Or() {
        this(1);
    }

    /**
     * Create a new Or gate with bus sizes provided.
     *
     * @param busSize Size of the bus for all input and output pins.
     * @see Gate
     */
    public Or(int busSize) {
        super(new int[] { busSize, busSize }, new int[] { busSize });
    }


    @Override
    public BitSet getState() {
        ArrayList<Cable> inputs = getInputCable();

        if (inputs.get(0) == null || inputs.get(1) == null) {
            return null;
        }

        BitSet result = inputs.get(0).getState();
        result.or(inputs.get(1).getState());

        return result;
    }

    /**
     * Create an exact copy of this or instance in a new one. Connected
     * referenced cables are not cloned.
     *
     * @return The newly cloned or.
     */
    @Override
    public Or clone() {
        try {
            Or clone = new Or(getInputBus()[0]);
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
