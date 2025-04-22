package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.cables.Cable;

/**
 * The base class of any logic gate.
 */
public abstract class Gate {
    private Boolean powered = false;

    /**
     * Give the number and size of the available input ports.
     */
    private Integer[] busInput = new Integer[] { 1, 1 };

    /**
     * Give the number and size of the available output ports.
     */
    private Integer[] busOutput = new Integer[] { 1 };

    // TODO check correct size when adding input/output
    /**
     * The input cables.
     * All cables are unpowered by default.
     */
    private ArrayList<Cable> inputCable = new ArrayList<Cable>();

    /**
     * The output cables.
     */
    private ArrayList<Cable> outputCable = new ArrayList<Cable>();

    // This constructor enable implicit constructor in extending class.
    public Gate() {
    };

    // This constructor is the one if we want to precise the inputAlone variable.
    public Gate(Integer[] busInput, Integer[] busOutput) {
        setBusInput(busInput);
        setBusOutput(busOutput);
    }

    /**
     * This function is called when inputCable changes.
     */
    public void updateState() {
        getOutputCable().forEach(cable -> {
            if (cable != null) {
                cable.updateState();
            }
        });
    };

    /**
     * This function is called when inputCable changes.
     * It should updates the return the state accordingly to the inputs.
     */
    public abstract BitSet getState();

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
        return busInput.length;
    }

    public Integer getOutputNumber() {
        return busOutput.length;
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

    public void setBusInput(Integer[] busInput) {
        assert busInput.length >= 0;
        this.busInput = busInput;
        // TODO check already connected cable, and/or throw errors
    }

    public void setBusOutput(Integer[] busOutput) {
        assert busOutput.length >= 1;
        this.busOutput = busOutput;
        // TODO check already connected cable, and/or throw errors
    }

    public void setInputCable(ArrayList<Cable> inputCable) throws Error {
        if (inputCable.size() != busInput.length)
            throw new Error("input state size is different than input number");

        // TODO check for cable bus size
        // for (int i = 0; i < inputCable.size(); i++) {
        // if (inputCable.get(i).getBusSize())
        // }

        this.inputCable = inputCable;
    }

    public void setInputCable(Cable inputCable, int index) {
        // TODO
    }

    public void setOutputCable(ArrayList<Cable> outputCable) throws Error {
        if (outputCable.size() != busOutput.length)
            throw new Error("output state size is different than output number");
        this.outputCable = outputCable;
        // TODO if simulation enabled, call updateState
    }

    public void setOutputCable(Cable outputCable, int index) {
        // TODO
    }
    // #endregion
}
