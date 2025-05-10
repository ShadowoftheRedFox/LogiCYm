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
     * Create a new And gate.
     *
     * @see Gate
     */
    public And() {
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
}
