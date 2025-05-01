package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;

/**
 * The OR gate.
 * If any of the two inputs are unpowered, then the output is unpowered.
 * Otherwise, apply the "or" logic to the input and output the result.
 * Only has one input and one output, both of bus size of 1.
 */
public class OR extends Gate {
    /**
     * Create a new OR gate.
     */
    public OR() {
    }

    @Override
    public BitSet getState() {
        ArrayList<Cable> inputs = getInputCable();

        if (inputs.get(0) == null || inputs.get(1) == null) {
            return null;
        }

        BitSet result = inputs.get(0).getState();
        result.or(inputs.get(1));

        return result;
    }
}
