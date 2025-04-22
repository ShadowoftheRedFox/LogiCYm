package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Gate;
import com.pjava.src.components.cables.Cable;

/**
 * The AND gate.
 * If any of the two inputs are unpowered, then the output is unpowered.
 * Otherwise, apply the "and" logic to the input and output the result.
 */
public class AND extends Gate {
    @Override
    public BitSet getState() {
        ArrayList<Cable> inputs = getInputCable();
        BitSet result = new BitSet(getOutputNumber());

        result.or(inputs.get(0));
        result.and(inputs.get(1));

        // return result;

        // if (inputs.get(0).get(0) == true &&
        // inputs.get(1).get(0) == true) {
        // result.set(0, true);
        // } else {
        // result.set(0, false);
        // }

        return result;
    }
}
