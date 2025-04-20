package com.pjava.src.components.gates;

import java.util.ArrayList;

import com.pjava.src.components.Gate;
import com.pjava.src.components.cables.Cable;

/**
 * The NOT gate.
 * If the input is unpowered, then the output is unpowered.
 * Otherwise, apply the "not" logic to the input and output the result.
 */
public class NOT extends Gate {
    public NOT() {
        setInputNumber(1);
    }

    @Override
    public int getState() {
        ArrayList<Cable> inputs = getInputCable();

        return (byte) toggleByte(inputs.get(0).getState());
    }

    private int toggleByte(int value) {
        for (int i = 0; i < getInputCable().get(0).getBusSize(); i++) {
            value = value ^ (1 << i);
        }

        return value;
    }
}
