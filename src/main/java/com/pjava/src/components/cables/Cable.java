package com.pjava.src.components.cables;

import java.util.ArrayList;

import com.pjava.src.config.States;

public class Cable {
    /**
     * The "size" of the cable.
     */
    private Integer busSize = 1;

    /**
     * State of the cable.
     */
    private ArrayList<States> state = new ArrayList<States>();

    // TODO input/output should be bus of same size
    // thought th problem lie within that cable can connect between cables and gates
    // maybe if we connect to cable, we implicitly add a cable connector

    // or even better, we don't need to make the code like a real cable
    // a cable can just be something that links many class between them
    // it's the front that will manage the visuals

    public Cable() {
        // the initial value
        state.add(States.Unpowered);
    }

    public Cable(Integer busSize) {
        setBusSize(busSize);
    }

    // #region Getters
    public Integer getBusSize() {
        return busSize;
    }

    public ArrayList<States> getState() {
        return state;
    }
    // #endregion

    // #region Setters
    public void setBusSize(Integer busSize) {
        if (busSize <= 0)
            throw new Error("busSize is invalid");
        // check if the weakest bit is 1, if yes, that means the number is not pair
        if ((busSize & 1) != 0)
            throw new Error("busSize is not pair");
        if (busSize > 32)
            throw new Error("busSize too large, max 32");

        this.busSize = busSize;

        // change the state array
        if (state.size() > busSize) {
            for (int i = 0; i < state.size() - busSize; i++) {
                state.removeLast();
            }
        } else if (state.size() < busSize) {
            for (int i = 0; i < busSize - state.size(); i++) {
                state.add(States.Unpowered);
            }
        }
    }
    // #endregion
}
