package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Gate;
import com.pjava.src.components.cables.Cable;

/**
 * The NOT gate.
 * If the input is unpowered, then the output is unpowered.
 * Otherwise, apply the "not" logic to the input and output the result.
 */
public class NOT extends Gate {
    public NOT() {
        setBusInput(new Integer[] { 1 });
    }

    @Override
    public BitSet getState() {
        ArrayList<Cable> inputs = getInputCable();
        BitSet result = new BitSet(getOutputNumber());

        if (inputs.get(0) == null) {
            setPowered(false);
            return null;
        }

        result.or(inputs.get(0));
        result.flip(0, result.length());

        return result;
    }
}
