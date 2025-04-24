package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Gate;
import com.pjava.src.components.cables.Cable;

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
        BitSet result = new BitSet(getOutputNumber());

        if (inputs.get(0) == null || inputs.get(1) == null) {
            return null;
        }

        result.or(inputs.get(0));
        result.or(inputs.get(1));

        return result;
    }
}
