package com.pjava.src.components.gates;

import com.pjava.src.config.States;

/**
 * The NOT gate.
 * If the input is unpowered, then the output is unpowered.
 * Otherwise, apply the "not" logic to the input and output the result.
 */
public class NOT extends Gate {
    NOT() {
        // tell the Gate class that this NOT gate has only one input.
        super(true);
    }

    @Override
    public void updateState() {
        States input = getInputState()[0];

        if (input == States.Unpowered) {
            setOutputState(States.Unpowered);
        }

        if (input == States.High) {
            setOutputState(States.Low);
        } else {
            setOutputState(States.High);
        }
    }
}
