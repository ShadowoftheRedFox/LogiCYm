package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.Cyclic;

/**
 * The base class of any logic gate. It has inputs, outputs, and gives a result
 * depending of its type.
 */
public abstract class Gate extends Element {
    /**
     * Give the number and size of the available input ports.
     */
    private int[] inputBus = new int[] {};

    /**
     * Give the number and size of the available output ports.
     */
    private int[] outputBus = new int[] {};

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

    /**
     * Create a new default gate, with two input with 1 as bus size, and one bus
     * output of the same size.
     */
    public Gate() {
        this(new int[] { 1, 1 }, new int[] { 1 });
    };

    /**
     * Create a new gate with customisable bus input and output.
     *
     * @param busInput  The list of input bus with their corresponding sizes.
     * @param busOutput The list of output bus with their corresponding sizes.
     * @throws Error Throw errors from either {@link #setInputBus(int[])} or
     *               {@link #setOutputBus(int[])}.
     */
    public Gate(int[] busInput, int[] busOutput) throws Error {
        try {
            setInputBus(busInput);
            setOutputBus(busOutput);
        } catch (NullPointerException | BusSizeException e) {
            throw new Error(e);
        }
    }

    /**
     * This function is called when input cables states changes.
     *
     * @param propagate Whether or not to propagate the changes to the outputs.
     */
    public void updateState(boolean propagate) {
        // if same state or unpowered, do not send update
        if (!getPowered()) {
            return;
        }

        // update the state otherwise
        if (propagate) {
            getOutputCable().forEach(cable -> {
                // we do not check if cable is null here
                // it should be checked with updatePower
                // it should crash if it isn't, or if updatePower doesn't work as expected
                cable.updateState();
            });
        }
    }

    /**
     * Should be called when input/output changes.
     * Update the power of himself and its output accordingly.
     */
    public void updatePower() {
        // if all cable are powered (and not null), then the gate is powered
        int countPoweredCables = 0;
        for (Cable cable : getInputCable()) {
            if (cable != null && cable.getPowered()) {
                countPoweredCables++;
            }
        }

        // send update to output when powered changed
        final int cableAmount = getInputCable().size();
        if ((getPowered() && countPoweredCables != cableAmount) ||
                (!getPowered() && countPoweredCables == cableAmount)) {

            setPowered(countPoweredCables == cableAmount);

            for (Cable cable : getOutputCable()) {
                if (cable != null && cable.getPowered() != getPowered()) {
                    cable.updatePower();
                }
            }
        } else {
            // check cyclic connections, if cycle detected, set powered all component
            Cyclic cycle = new Cyclic();
            if (cycle.isCyclic(this)) {
                System.out.println("No powered change, but in cycle");
                /*
                 * TODO:
                 * we should check if, for all inputs in the cycle, without elements in the
                 * cycle, they are all powered
                 * if yes, power up the cycle, then update power all elements of the cycle
                 * if no, the inverse
                 */
                for (Element element : cycle.getElementInCyle()) {
                    element.setPowered(true);
                }
            }
        }
    }

    /**
     * This function is called when inputCable changes.
     * It should updates the return the state accordingly to the inputs.
     * It should also return null if any of the inputs are null.
     *
     * @return The current state.
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

        // placeholder result
        Cable result = null;

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
                            return thisOutputCable;
                        }
                        // TODO maybe try to merge and not throw error?
                        matchedButFull = true;
                    } else

                    // if both empty, create a new cable and connect
                    if (thisOutputCable == null && arg0InputCable == null) {
                        try {
                            result = new Cable(thisoutputBusSize);
                        } catch (BusSizeException e) {
                            e.printStackTrace();
                        }
                        result.inputGate.add(this);
                        result.outputGate.add(arg0);

                        this.outputCable.set(i, result);
                        arg0.inputCable.set(j, result);

                        result.updatePower();
                        result.updateState();
                        arg0.updateState();
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
                    if (thisOutputCable != null && arg0InputCable == null) {
                        thisOutputCable.outputGate.add(arg0);
                        arg0.inputCable.set(j, thisOutputCable);

                        thisOutputCable.updatePower();
                        thisOutputCable.updateState();
                        arg0.updateState();
                        return thisOutputCable;
                    } else if (thisOutputCable == null && arg0InputCable != null) {
                        arg0InputCable.inputGate.add(this);
                        this.outputCable.set(i, arg0InputCable);

                        arg0InputCable.updatePower();
                        arg0InputCable.updateState();
                        arg0.updateState();
                        return arg0InputCable;
                    } else {
                        // should never fall here
                        throw new Error("Unknown result, lost in the code... :d");
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
     * @throws BusSizeException          Thrown when the given idenxes correspond to
     *                                   different sizes of bus.
     */
    public Cable connect(Gate arg0, int thisOutputIndex, int arg0InputIndex)
            throws Error, NullPointerException, IndexOutOfBoundsException, BusSizeException {
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
            throw new BusSizeException("Expected bus size to be the same, received "
                    + this.outputBus[thisOutputIndex] + " and " + arg0.inputBus[arg0InputIndex]);
        }

        // check if both gate are already linked
        Cable thisOutputCable = this.outputCable.get(thisOutputIndex);
        Cable arg0InputCable = arg0.inputCable.get(arg0InputIndex);
        if (thisOutputCable != null && arg0InputCable != null) {
            if (thisOutputCable.uuid().equals(arg0InputCable.uuid())) {
                return thisOutputCable;
            } else if (thisOutputCable.getBusSize() != arg0InputCable.getBusSize()) {
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

            result.updatePower();
            result.updateState();
            return result;
        } else
        // if either is null
        if (thisOutputCable != null && arg0InputCable == null) {
            thisOutputCable.outputGate.add(arg0);
            arg0.inputCable.set(arg0InputIndex, thisOutputCable);

            arg0.updatePower();
            arg0.updateState();
            return thisOutputCable;
        } else if (thisOutputCable == null && arg0InputCable != null) {
            arg0InputCable.inputGate.add(this);
            this.outputCable.set(thisOutputIndex, arg0InputCable);

            arg0InputCable.updatePower();
            arg0InputCable.updateState();
            return arg0InputCable;
        }

        return null;
    }

    /**
     * Get the cable between this and arg0, or return null if no connection exists.
     * The check direction is from this.output to arg0.input.
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
     * Get the cable between this and arg0, or return null if no connection exists.
     * The check direction if from this.output to arg0.input.
     *
     * @param arg0    The gate we want to check if there is a cable connecting us.
     * @param busSize The specific bus size of the want the return cable to have.
     * @return The Cable if it exists, null otherwise.
     */
    public Cable getCableWith(Gate arg0, int busSize) {
        if (arg0 == null || BusSizeException.isBusSizeException(busSize)) {
            return null;
        }

        for (Cable thisCable : outputCable) {
            for (Cable arg0Cable : arg0.getInputCable()) {
                if (thisCable != null &&
                        arg0Cable != null &&
                        thisCable.uuid().equals(arg0Cable.uuid()) &&
                        thisCable.getBusSize() == busSize) {
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

    /**
     * Add an input bus with the given size.
     *
     * @param size The size of the new bus.
     * @throws BusSizeException Throw when the given size is equal or below 0, not a
     *                          power of 2, or greater than 32.
     */
    protected void addInputBus(int size) throws BusSizeException {
        if (BusSizeException.isBusSizeException(size)) {
            throw BusSizeException.fromName("size", size);
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : getInputBus()) {
            list.add(Integer.valueOf(i));
        }

        list.add(size);
        setInputBus(list.stream().mapToInt(Integer::intValue).toArray());
    }

    /**
     * Remove an input bus at the given index if possible.
     *
     * @param index The index of the bus to remove.
     * @throws IllegalArgumentException Throw when index is below 0.
     */
    protected void removeInputBus(int index) throws IllegalArgumentException {
        if (index < 0) {
            throw new IllegalArgumentException("Expected index to be greater than 0, received " + index);
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : getInputBus()) {
            list.add(Integer.valueOf(i));
        }

        if (index > list.size()) {
            return;
        }

        list.remove(index);
        try {
            setInputBus(list.stream().mapToInt(Integer::intValue).toArray());
        } catch (NullPointerException | BusSizeException e) {
            throw new Error(e);
        }
    }

    /**
     * Add an output bus with the given size.
     *
     * @param size The size of the new bus.
     * @throws BusSizeException Throw when the given size is equal or below 0, not a
     *                          power of 2, or greater than 32.
     */
    protected void addOutputBus(int size) throws BusSizeException {
        if (BusSizeException.isBusSizeException(size)) {
            throw BusSizeException.fromName("size", size);
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : getOutputBus()) {
            list.add(Integer.valueOf(i));
        }

        list.add(size);
        setOutputBus(list.stream().mapToInt(Integer::intValue).toArray());
    }

    /**
     * Remove an output bus at the given index if possible.
     *
     * @param index The index of the bus to remove.
     * @throws IllegalArgumentException Throw when index is below 0.
     */
    protected void removeOutputBus(int index) throws IllegalArgumentException {
        if (index < 0) {
            throw new IllegalArgumentException("Expected index to be greater than 0, received " + index);
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : getOutputBus()) {
            list.add(Integer.valueOf(i));
        }

        if (index > list.size()) {
            return;
        }

        list.remove(index);
        try {
            setOutputBus(list.stream().mapToInt(Integer::intValue).toArray());
        } catch (NullPointerException | BusSizeException e) {
            throw new Error(e);
        }
    }

    // #region Getters
    /**
     * Getter for {@link #inputBus}.length.
     *
     * @return The number of input buses.
     */
    public Integer getInputNumber() {
        return inputBus.length;
    }

    /**
     * Getter for {@link #inputBus}.
     *
     * @return The input buses.
     */
    public int[] getInputBus() {
        return inputBus;
    }

    /**
     * Getter for {@link #outputBus}.length.
     *
     * @return The number of output buses.
     */
    public Integer getOutputNumber() {
        return outputBus.length;
    }

    /**
     * Getter for {@link #outputBus}.
     *
     * @return The output buses.
     */
    public int[] getOutputBus() {
        return outputBus;
    }

    /**
     * Getter for {@link #inputCable}.
     *
     * @return The list of input cables.
     */
    public ArrayList<Cable> getInputCable() {
        return inputCable;
    }

    /**
     * Getter for {@link #outputCable}.
     *
     * @return The list of output cables.
     */
    public ArrayList<Cable> getOutputCable() {
        return outputCable;
    }
    // #endregion

    // #region Setters
    /**
     * Setter for {@link #inputBus}.
     *
     * @param inputBus The new bus input sizes array.
     * @return Return if true if all existing cables have valid bus size.
     * @throws BusSizeException     Throw when the given size is equal or below
     *                              0, not a power of 2, or greater than 32.
     * @throws NullPointerException Throw when inputBus is null or contains a null.
     */
    protected boolean setInputBus(int[] inputBus) throws BusSizeException, NullPointerException {
        if (inputBus == null) {
            throw new NullPointerException("Expected busSizes[] to be int[], received a null");
        }
        for (int busSize : inputBus) {
            if (BusSizeException.isBusSizeException(busSize)) {
                throw BusSizeException.fromName("bus size", busSize);
            }
        }

        this.inputBus = inputBus;

        ensureCapacity();

        // since it replace all existing cable, only check if size is compatible with
        // already connected cable
        boolean valid = this.inputCable.size() <= this.inputBus.length;
        for (int i = 0; i < this.inputCable.size() && i < this.inputBus.length; i++) {
            Cable cable = this.inputCable.get(i);
            if (cable != null && cable.getBusSize() != this.inputBus[i]) {
                valid = false;
                System.out.println("busInput change triggered this warning. Invalid size at index " + i);
            }
        }

        // TODO check if input and output sizes are both > 0 (gate without ports at all)

        return valid;
    }

    /**
     * Set a specific {@link #inputBus} at the given index.
     *
     * @param busSize The size of the bus.
     * @param index   The index of the new busSize. Should be a valid index, lower
     *                than {@link #inputBus}.length.
     * @return Return if true if all existing cables have valid bus size.
     * @throws BusSizeException          Throw when the given size is equal or below
     *                                   0, not a power of 2, or greater than 32.
     * @throws IndexOutOfBoundsException Throw when index is lower than 0, or equal
     *                                   or greater than {@link #inputBus}.length.
     */
    protected boolean setInputBus(int busSize, int index)
            throws BusSizeException, IndexOutOfBoundsException {
        if (BusSizeException.isBusSizeException(busSize)) {
            throw BusSizeException.fromName("bus size", busSize);
        }
        if (index < 0 || index >= inputBus.length) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= index < " + inputBus.length + ", received " + index);
        }

        inputBus[index] = busSize;

        return inputCable.get(index) == null || busSize == inputCable.get(index).getBusSize();
    }

    /**
     * Setter for {@link #outputBus}.
     *
     * @param outputBus The new bus output sizes array.
     * @return Return if true if all existing cables have valid bus size.
     * @throws BusSizeException     Throw when the given size is equal or below
     *                              0, not a power of 2, or greater than 32.
     * @throws NullPointerException Throw when inputBus is null or contains a null.
     */
    protected boolean setOutputBus(int[] outputBus) throws BusSizeException, NullPointerException {
        if (outputBus == null) {
            throw new NullPointerException("Expected busSizes[] to be int[], received a null");
        }

        for (int busSize : outputBus) {
            if (BusSizeException.isBusSizeException(busSize)) {
                throw BusSizeException.fromName("bus size", busSize);
            }
        }

        this.outputBus = outputBus;

        ensureCapacity();

        // since it replace all existing cable, only check if size is compatible with
        // already connected cable
        boolean valid = this.outputCable.size() <= this.outputBus.length;
        for (int i = 0; i < this.outputCable.size() && i < this.outputBus.length; i++) {
            Cable cable = this.outputCable.get(i);
            if (cable != null && cable.getBusSize() != this.outputBus[i]) {
                valid = false;
                System.out.println("busOutput change triggered this warning. Invalid size at index " + i);
            }
        }

        return valid;
    }

    /**
     * Set a specific {@link #outputBus} at the given index.
     *
     * @param busSize The size of the bus.
     * @param index   The index of the new busSize. Should be a valid index, lower
     *                than {@link #outputBus}.length.
     * @return Return if true if all existing cables have valid bus size.
     * @throws BusSizeException          Throw when the given size is equal or below
     *                                   0, not a power of 2, or greater than 32.
     * @throws IndexOutOfBoundsException Throw when index is lower than 0, or equal
     *                                   or greater than {@link #outputBus}.length.
     */
    protected boolean setOutputBus(int busSize, int index)
            throws BusSizeException, IndexOutOfBoundsException {
        if (BusSizeException.isBusSizeException(busSize)) {
            throw BusSizeException.fromName("bus size", busSize);
        }
        if (index < 0 || index >= outputBus.length) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= index < " + outputBus.length + ", received " + index);
        }

        outputBus[index] = busSize;

        return outputCable.get(index) == null || busSize == outputCable.get(index).getBusSize();
    }
    // #endregion

    /*
     * TODO add them if needed
     * protected void setInputCable(ArrayList<Cable> inputCable) throws Error {}
     * protected void setInputCable(Cable inputCable, int index) throws Error {}
     * protected void setOutputCable(ArrayList<Cable> outputCable) throws Error {}
     * protected void setOutputCable(Cable outputCable, int index) throws Error {}
     */
}
