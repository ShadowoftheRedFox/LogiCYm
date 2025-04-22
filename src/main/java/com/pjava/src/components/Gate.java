package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.cables.Cable;

/**
 * The base class of any logic gate.
 */
public abstract class Gate {
    private boolean powered = false;

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
        // check for any null, and unpower the gate, or power it if none null
        int nullAmount = 0;
        for (Cable cable : getOutputCable()) {
            if (cable == null) {
                nullAmount++;
            }
        }
        if (nullAmount > 0) {
            setPowered(false);
            return;
        }
        setPowered(true);

        // otherwise, update
        getOutputCable().forEach(cable -> {
            if (cable != null) {
                cable.updateState();
            }
        });
    };

    /**
     * {@html
     * <p>
     * This function is called when inputCable changes.
     * </p>
     * <p>
     * It should updates the return the state accordingly to the inputs.
     * </p>
     * <p>
     * It should also return null if any of the inputs are null.
     * </p>
     * }
     */
    public abstract BitSet getState();

    public Cable connect(Gate arg0) throws Error {
        if (arg0 == null) {
            return null;
        }

        // TODO below:
        // first, get the first empty output, then find an input on arg0
        // of the same size
        // if no same size found, throw Error

        // if all output taken, get the first empty input of arg0 and try to connect the
        // same way as before

        // if both full check if no relation between both Gate and connect

        return null;
    }

    public Cable connect(Gate arg0, int thisOutputIndex, int arg0InputIndex) throws Error {
        if (arg0 == null) {
            return null;
        }

        // TODO below:
        // if both cable are the same size, do a connection (check if "port" empty, if
        // not but same cable, return the cable)
        // if not throw Error

        // if either of both full, new cable

        return null;
    }

    // #region Getters
    public boolean getPowered() {
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
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public void setBusInput(Integer[] busInput) throws Error {
        assert busInput.length >= 0;
        this.busInput = busInput;
        // TODO check already connected cable, and/or throw errors
    }

    public void setBusInput(Integer busInput, int index) throws Error {
        // TODO check already connected cable, and/or throw errors
    }

    public void setBusOutput(Integer[] busOutput) throws Error {
        assert busOutput.length >= 1;
        this.busOutput = busOutput;
        // TODO check already connected cable, and/or throw errors
    }

    public void setBusOutput(Integer busOutput, int index) throws Error {
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

    public void setInputCable(Cable inputCable, int index) throws Error {
        // TODO
    }

    public void setOutputCable(ArrayList<Cable> outputCable) throws Error {
        if (outputCable.size() != busOutput.length)
            throw new Error("output state size is different than output number");
        this.outputCable = outputCable;
        // TODO if simulation enabled, call updateState
    }

    public void setOutputCable(Cable outputCable, int index) throws Error {
        // TODO
    }
    // #endregion
}
