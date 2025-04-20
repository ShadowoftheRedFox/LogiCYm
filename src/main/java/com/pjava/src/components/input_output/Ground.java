package com.pjava.src.components.input_output;

import com.pjava.src.components.Gate;

public class Ground extends Gate {
    public Ground() {
        setPowered(true);
    }

    @Override
    public void updateState() {
        getOutputCable().forEach(cable -> {
            cable.setState(0);
        });
    }

    @Override
    public int getState() {
        return 0;
    }
}
