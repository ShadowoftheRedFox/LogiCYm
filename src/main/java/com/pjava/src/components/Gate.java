package com.pjava.src.components;

import java.util.ArrayList;

import com.pjava.src.components.cables.Cable;

/**
 * The base class of any logic gate.
 */
public abstract class Gate {
    private Boolean powered = false;

    private Integer inputNumber = 2;

    private Integer outputNumber = 1;

    /**
     * The input states cables.
     * The input state by default of all cables are unpowered.
     */
    private ArrayList<Cable> inputCable = new ArrayList<Cable>();

    /**
     * The output states cables.
     */
    private ArrayList<Cable> outputCable = new ArrayList<Cable>();

    // This constructor enable implicit constructor in extending class.
    public Gate() {
    };

    // This constructor is the one if we want to precise the inputAlone variable.
    public Gate(Integer inputNumber, Integer outputNumber) {
    }

    /**
     * This function is called when inputCable changes.
     */
    public void updateState() {
        getOutputCable().forEach(cable -> {
            cable.updateState();
        });
    };

    /**
     * This function is called when inputCable changes.
     * It should updates the return the state accordingly to the inputs.
     */
    public abstract int getState();

    /**
     * Allow to edit a cable input from a gate
     *
     * @param index
     * @param newState
     */
    public final void editInputCable(int index, Cable newState) {
        assert index >= 0;
        inputCable.set(index, newState);
    }

    public final void editOutputStat(int index, Cable newState) {
        assert index >= 0;
        outputCable.set(index, newState);
    }

    // #region Getters
    public Boolean getPowered() {
        return powered;
    }

    public Integer getInputNumber() {
        return inputNumber;
    }

    public Integer getOutputNumber() {
        return outputNumber;
    }

    public ArrayList<Cable> getInputCable() {
        return inputCable;
    }

    public ArrayList<Cable> getOutputCable() {
        return outputCable;
    }
    // #endregion

    // #region Setters
    public void setPowered(Boolean powered) {
        this.powered = powered;
    }

    public void setInputNumber(Integer inputNumber) {
        assert inputNumber >= 0;
        this.inputNumber = inputNumber;
    }

    public void setOutputNumber(Integer outputNumber) {
        assert outputNumber >= 1;
        this.outputNumber = outputNumber;
    }

    public void setInputCable(ArrayList<Cable> inputCable) throws Error {
        if (inputCable.size() != inputNumber)
            throw new Error("input state size is different than input number");

        // TODO check for cable bus size
        // for (int i = 0; i < inputCable.size(); i++) {
        // if (inputCable.get(i).getBusSize())
        // }

        this.inputCable = inputCable;
    }

    public void setOutputCable(ArrayList<Cable> outputCable) throws Error {
        if (outputCable.size() != outputNumber)
            throw new Error("output state size is different than output number");
        this.outputCable = outputCable;
        // TODO if simulation enabled, call updateState
    }
    // #endregion
}
