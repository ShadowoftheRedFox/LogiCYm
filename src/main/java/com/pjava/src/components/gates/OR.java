package com.pjava.src.components.gates;

import java.util.ArrayList;

import com.pjava.src.components.Gate;
import com.pjava.src.components.cables.Cable;

/**
 * The OR gate.
 * If any of the two inputs are unpowered, then the output is unpowered.
 * Otherwise, apply the "or" logic to the input and output the result.
 */
public class OR extends Gate {
    @Override
    public int getState() {
        ArrayList<Cable> inputs = getInputCable();

        if (inputs.get(0).getState(0) == 1 ||
                inputs.get(1).getState(0) == 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
