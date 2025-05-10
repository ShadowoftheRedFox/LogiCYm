package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.Gate;
import com.pjava.src.components.Cable;

public class Not extends Gate {
    /**
     * Create a new Not gate.
     */
    public Not() {
        super(new int[] { 1 }, new int[] { 1 });
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
