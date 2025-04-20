package com.pjava.src.components.gates;

import com.pjava.src.config.States;

/**
 * The AND gate.
 * If any of the two inputs are unpowered, then the output is unpowered.
 * Otherwise, apply the "and" logic to the input and output the result.
 */
public class AND extends Gate {
    @Override
    public void updateState() {
        States[] inputs = getInputState();

        if (inputs[0] == States.Unpowered || inputs[1] == States.Unpowered) {
            setOutputState(States.Unpowered);
        }

        if (inputs[0] == States.High && inputs[1] == States.High) {
            setOutputState(States.High);
        } else {
            setOutputState(States.Low);
        }
    }
}
