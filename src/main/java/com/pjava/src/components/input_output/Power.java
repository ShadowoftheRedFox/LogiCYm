package com.pjava.src.components.input_output;

import com.pjava.src.components.Gate;

public class Power extends Gate {
    public Power() {
        setPowered(true);
    }

    @Override
    public void updateState() {
        getOutputCable().forEach(cable -> {
            cable.setState(1);
        });
    }

    @Override
    public int getState() {
        return 1;
    }
}
