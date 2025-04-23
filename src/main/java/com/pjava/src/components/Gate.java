package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.components.cables.Cable;
import com.pjava.src.errors.BusDifferentSizeException;

/**
 * The base class of any logic gate.
 */
public abstract class Gate {
    private boolean powered = false;

    /**
     * Give the number and size of the available input ports.
     */
    private Integer[] inputBus = new Integer[] { 1, 1 };

    /**
     * Give the number and size of the available output ports.
     */
    private Integer[] outputBus = new Integer[] { 1 };

    /**
     * The input cables.
     * Must have the same length as {@link #inputBus}.
     */
    private ArrayList<Cable> inputCable = new ArrayList<Cable>();

    /**
     * The output cables.
     * Must have the same length as {@link #outputBus}.
     */
    private ArrayList<Cable> outputCable = new ArrayList<Cable>();

    // This constructor enable implicit constructor in extending class.
    public Gate() {
        ensureCapacity();
    };

    // This constructor is the one if we want to precise the inputAlone variable.
    public Gate(Integer[] busInput, Integer[] busOutput) {
        setInputBus(busInput);
        setOutputBus(busOutput);
    }

    /**
     * This function is called when inputCable changes.
     */
    public void updateState() {
        // unpower the gate if an input is null or unpowered
        int nullAmount = 0;
        for (Cable cable : getInputCable()) {
            if (cable == null || !cable.getPowered()) {
                nullAmount++;
            }
        }
        // the gate need to have input for this check to happen
        if (nullAmount > 0 && inputBus.length != 0) {
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

    /**
     * Connect this gate with the arg0 gate.
     * Depending of the case, it either get an existing Cable or create a new one.
     * It also make the necessary internal modification to both gate and cable.
     * It will take the first available spot. So if you want to connect to the
     * second available, use {@link #connect(Gate, int, int)}.
     *
     * @param arg0 The gate input you want to connect this output.
     * @return The Cable making the connection, or null if the connection is
     *         impossible (incompatible bus size).
     * @throws Error                Throw an error if the connection is possible but
     *                              different Cable are already connected to both.
     * @throws NullPointerException When arg0 is null.
     */
    public Cable connect(Gate arg0) throws Error, NullPointerException {
        if (arg0 == null) {
            throw new NullPointerException("Expected arg0 to be an instance of Gate, received null");
        }

        // check if both gate are already linked
        Cable result = getCableWith(arg0);
        if (result != null) {
            return result;
        }

        // for the error throw later
        boolean matchedButFull = false;

        // look through all the this.outputBus and arg0.inputBus, and get each match
        for (int i = 0; i < this.outputBus.length; i++) {
            for (int j = 0; j < arg0.inputBus.length; j++) {
                // get bus sizes
                int thisoutputBusSize = this.outputBus[i];
                int arg0InputBusSize = arg0.inputBus[j];

                // if match
                if (thisoutputBusSize == arg0InputBusSize) {
                    // check if both have cable
                    Cable thisOutputCable = this.getOutputCable().get(i);
                    Cable arg0InputCable = arg0.getInputCable().get(j);

                    if (thisOutputCable != null && arg0InputCable != null) {
                        // check if it's the same cable, and return it
                        if (thisOutputCable.uuid().equals(arg0InputCable.uuid())) {
                            return this.getOutputCable().get(thisoutputBusSize);
                        }
                        // TODO maybe try to merge and not throw error?
                        matchedButFull = true;
                    } else

                    // if both empty, create a new cable and connect
                    if (thisOutputCable == null && arg0InputCable == null) {
                        result = new Cable(thisoutputBusSize);
                        result.inputGate.add(this);
                        result.outputGate.add(arg0);

                        this.outputCable.set(i, result);
                        arg0.inputCable.set(j, result);

                        return result;
                    }
                }
            }
        }

        // if all full pair and empty pair have been made, look for partial pairs
        // link the this.output first
        for (int i = 0; i < this.outputBus.length; i++) {
            for (int j = 0; j < arg0.inputBus.length; j++) {
                // get bus sizes
                int thisoutputBusSize = this.outputBus[i];
                int arg0InputBusSize = arg0.inputBus[j];
                // if match
                if (thisoutputBusSize == arg0InputBusSize) {
                    // check if both have cable
                    Cable thisOutputCable = this.getOutputCable().get(i);
                    Cable arg0InputCable = arg0.getInputCable().get(j);
                    // one of them is a Cable, the other is null, then get the Cable and connect
                    {
                        if (thisOutputCable != null && arg0InputCable == null) {
                            thisOutputCable.outputGate.add(arg0);
                            arg0.inputCable.set(j, thisOutputCable);
                            return thisOutputCable;
                        } else if (thisOutputCable == null && arg0InputCable != null) {
                            arg0InputCable.inputGate.add(this);
                            this.outputCable.set(i, arg0InputCable);
                            return arg0InputCable;
                        } else {
                            // should never fall here
                            throw new Error("Unknown result, lost in the code... :d");
                        }
                    }
                }
            }
        }

        // if we're here, either no match or connection impossible

        if (matchedButFull) {
            throw new Error("connection possible but bus allready full");
        }

        return null;
    }

    /**
     * Connect this gate with the arg0 gate, at the given index.
     * Depending of the case, it either get an existing Cable or create a new one.
     * It also make the necessary internal modification to both gate and cable.
     *
     * @param arg0            The gate input you want to connect this output.
     * @param thisOutputIndex The index in this.outputCable ArrayList.
     * @param arg0InputIndex  The index in arg0.inputCable ArrayList.
     * @return The cable making the connection, or null if the connection is
     *         impossible (incompatible bus size).
     * @throws Error                     Throw an error if the connection is
     *                                   possible but different Cable are already
     *                                   connected to both.
     * @throws NullPointerException      When arg0 is null.
     * @throws IndexOutOfBoundsException When thisOutputIndex and/or arg0InputIndex
     *                                   are below 0, or above or equal to
     *                                   this.{@link #outputBus}.length
     *                                   and arg0.{@link #inputBus}.length
     *                                   respectively.
     * @throws BusDifferentSizeException Thrown when the given idenxes correspond to
     *                                   different sizes of bus.
     */
    public Cable connect(Gate arg0, int thisOutputIndex, int arg0InputIndex)
            throws Error, NullPointerException, IndexOutOfBoundsException, BusDifferentSizeException {
        if (arg0 == null) {
            throw new NullPointerException("Expected arg0 to be an instance of Gate, received null");
        }

        if (thisOutputIndex < 0 || this.outputBus.length <= thisOutputIndex) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= thisOutputIndex < " + this.outputBus.length + ", received " + thisOutputIndex);
        }
        if (arg0InputIndex < 0 || this.outputBus.length <= arg0InputIndex) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= arg0InputIndex < " + this.outputBus.length + ", received " + arg0InputIndex);
        }

        if (this.outputBus[thisOutputIndex] != arg0.inputBus[arg0InputIndex]) {
            throw new BusDifferentSizeException("Expected bus size to be the same");
        }

        // check if both gate are already linked
        Cable thisOutputCable = this.outputCable.get(thisOutputIndex);
        Cable arg0InputCable = arg0.inputCable.get(arg0InputIndex);
        if (thisOutputCable != null && arg0InputCable != null) {
            if (thisOutputCable.uuid().equals(arg0InputCable.uuid())) {
                return thisOutputCable;
            } else if (thisOutputCable.size() != arg0InputCable.size()) {
                // incompatible sizes
                return null;
            } else {
                throw new Error("connection possible but bus allready full");
            }
        } else
        // check if both cable are empty
        if (thisOutputCable == null && arg0InputCable == null) {
            Cable result = new Cable(this.outputBus[thisOutputIndex]);
            result.inputGate.add(this);
            result.outputGate.add(arg0);

            this.outputCable.set(thisOutputIndex, result);
            arg0.inputCable.set(arg0InputIndex, result);

            return result;
        } else
        // if either is null
        if (thisOutputCable != null && arg0InputCable == null) {
            thisOutputCable.outputGate.add(arg0);
            arg0.inputCable.set(arg0InputIndex, thisOutputCable);
            return thisOutputCable;
        } else if (thisOutputCable == null && arg0InputCable != null) {
            arg0InputCable.inputGate.add(this);
            this.outputCable.set(thisOutputIndex, arg0InputCable);
            return arg0InputCable;
        }

        return null;
    }

    /**
     * Get the cable between this and arg0, or return null if no connection exists.
     * We check the direction this.output to arg0.input.
     *
     * @param arg0 The gate we want to check if there is a cable connecting us.
     * @return The Cable if it exists, null otherwise.
     */
    public Cable getCableWith(Gate arg0) {
        if (arg0 == null) {
            return null;
        }

        for (Cable thisCable : outputCable) {
            for (Cable arg0Cable : arg0.getInputCable()) {
                if (thisCable != null &&
                        arg0Cable != null &&
                        thisCable.uuid().equals(arg0Cable.uuid())) {
                    return thisCable;
                }
            }
        }

        return null;
    }

    /**
     * This function make sure that input and output cable ArrayList can be add/set
     * to their input and output bus sizes.
     * It fill them with null.
     */
    private void ensureCapacity() {
        if (inputCable.size() < inputBus.length) {
            inputCable.ensureCapacity(inputBus.length);
            for (int i = inputCable.size(); i < inputBus.length; i++) {
                inputCable.add(null);
            }
        }
        if (outputCable.size() < outputBus.length) {
            outputCable.ensureCapacity(outputBus.length);
            for (int i = outputCable.size(); i < outputBus.length; i++) {
                outputCable.add(null);
            }
        }
    }

    // #region Getters
    public boolean getPowered() {
        return powered;
    }

    public Integer getInputNumber() {
        return inputBus.length;
    }

    public Integer[] getInputBus() {
        return inputBus;
    }

    public Integer getOutputNumber() {
        return outputBus.length;
    }

    public Integer[] getOutputBus() {
        return outputBus;
    }

    public ArrayList<Cable> getInputCable() {
        return inputCable;
    }

    public ArrayList<Cable> getOutputCable() {
        return outputCable;
    }
    // #endregion

    // #region Setters
    /**
     * Set whether the cable is powered or not. An unpowered cable does not transmit
     * nor emit updates.
     *
     * @param powered True if powered, false if not.
     */
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public boolean setInputBus(Integer[] busInput) throws Error {
        assert busInput.length >= 0;
        this.inputBus = busInput;

        ensureCapacity();

        // since it replace all existing cable, only check if size is compatible with
        // already connected cable
        boolean valid = true;
        for (int i = 0; i < this.inputCable.size(); i++) {
            Cable cable = this.inputCable.get(i);
            if (cable != null && cable.getBusSize() != this.inputBus[i]) {
                valid = false;
                System.out.println("busInput change triggered this warning. Invalid size at index " + i);
            }
        }

        return valid;
    }

    public void setBusInput(Integer busInput, int index) throws Error {
        // TODO check already connected cable, and/or throw errors
    }

    public boolean setOutputBus(Integer[] busOutput) throws Error {
        assert busOutput.length >= 1;
        this.outputBus = busOutput;

        ensureCapacity();

        // since it replace all existing cable, only check if size is compatible with
        // already connected cable
        boolean valid = true;
        for (int i = 0; i < this.outputCable.size(); i++) {
            Cable cable = this.outputCable.get(i);
            if (cable != null && cable.getBusSize() != this.outputBus[i]) {
                valid = false;
                System.out.println("busOutput change triggered this warning. Invalid size at index " + i);
            }
        }

        return valid;
    }

    public void setBusOutput(Integer busOutput, int index) throws Error {
        // TODO check already connected cable, and/or throw errors
    }

    public void setInputCable(ArrayList<Cable> inputCable) throws Error {
        if (inputCable.size() != inputBus.length)
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
        if (outputCable.size() != outputBus.length)
            throw new Error("output state size is different than output number");
        this.outputCable = outputCable;
        // TODO if simulation enabled, call updateState
    }

    public void setOutputCable(Cable outputCable, int index) throws Error {
        // TODO
    }
    // #endregion
}
