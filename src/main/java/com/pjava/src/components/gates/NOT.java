package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;

/**
 * The NOT gate.
 * If the input is unpowered, then the output is unpowered.
 * Otherwise, apply the "not" logic to the input and output the result.
 * Only has one input and one output, both of bus size of 1.
 */
public class NOT extends Gate {
    /**
     * Create a new NOT gate.
     */
    public NOT() {
        super(new Integer[] { 1 }, new Integer[] { 1 });
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
}
