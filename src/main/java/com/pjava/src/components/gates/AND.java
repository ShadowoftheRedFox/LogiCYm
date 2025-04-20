package com.pjava.src.components.gates;

import java.util.ArrayList;

import com.pjava.src.components.Gate;
import com.pjava.src.components.cables.Cable;

/**
 * The AND gate.
 * If any of the two inputs are unpowered, then the output is unpowered.
 * Otherwise, apply the "and" logic to the input and output the result.
 */
public class AND extends Gate {
    @Override
    public int getState() {
        ArrayList<Cable> inputs = getInputCable();

        if (inputs.get(0).getState(0) == 1 &&
                inputs.get(1).getState(0) == 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
