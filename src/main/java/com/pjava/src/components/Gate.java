package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import org.json.JSONObject;
import org.json.JSONArray;

import com.pjava.src.components.gates.Schema;

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
    protected int[] inputBus = new int[] {};

    /**
     * Give the number and size of the available output ports.
     */
    protected int[] outputBus = new int[] {};

    /**
     * The input cables. Must have the same length as {@link #inputBus}.
     */
    protected ArrayList<Cable> inputCable = new ArrayList<Cable>();

    /**
     * The output cables. Must have the same length as {@link #outputBus}.
     */
    protected ArrayList<Cable> outputCable = new ArrayList<Cable>();

    /**
     * Create a new default gate, with two input with 1 as bus size, and one bus
     * output of the same size.
     */
    public Gate() {
        this(new int[] { 1, 1 }, new int[] { 1 });
    }

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
                if (cable != null) {
                    cable.updateState();
                }
            });
        }
    }

    /**
     * Should be called when input/output changes. Update the power of himself
     * and its output accordingly.
     */
    public void updatePower() {
        // if all cable are powered (and not null), then the gate is powered
        int poweredCablesCount = 0;
        for (Cable cable : getInputCable()) {
            if (cable != null && cable.getPowered()) {
                poweredCablesCount++;
            }
        }

        final int cableAmount = getInputCable().size();

        // if all cable are powered, power the gate
        if (poweredCablesCount == cableAmount) {
            // send update only if state changes
            if (!getPowered()) {
                setPowered(true);
                cableUpdatePower();
            }
            return;
        }

        // if not all cable are powered, maybe it's because we're in a cycle that should
        // be powered
        Cyclic cycle = new Cyclic();
        if (cycle.isCyclic(this)) {
            // we are in a cycle, now check if inputs are all powered
            boolean cycleInputAllPowered = true;
            for (Cable cable : cycle.getCycleInput()) {
                cycleInputAllPowered = cycleInputAllPowered && cable.getPowered();
            }

            // if all powered, then power up the cycle, and call the cycle outputs
            for (Element elementInCyle : cycle.getElementInCyle()) {
                elementInCyle.setPowered(true);
            }

            for (Cable cycleOutput : cycle.getCycleOutput()) {
                cycleOutput.setPowered(true);
                cycleOutput.updatePower();
            }
            return;
        }

        // and if not in cycle and not all cable powered, unpower
        if (poweredCablesCount != cableAmount) {
            // send update only if state changes
            if (getPowered()) {
                setPowered(false);
                cableUpdatePower();
            }
            return;
        }
    }

    /**
     * Used to call {@link Cable#updatePower()} on all cable linked to this
     * gate.
     */
    private void cableUpdatePower() {
        for (Cable cable : getOutputCable()) {
            if (cable != null && cable.getPowered() != getPowered()) {
                cable.updatePower();
            }
        }
    }

    /**
     * This function is called when inputCable changes. It should updates the
     * return the state accordingly to the inputs. It should also return null if
     * any of the inputs are null.
     *
     * @return The current state.
     */
    public abstract BitSet getState();

    /**
     * Connect this gate with the arg0 gate. Depending of the case, it either
     * get an existing Cable or create a new one. It also make the necessary
     * internal modification to both gate and cable. It will take the first
     * available spot. So if you want to connect to the second available, use
     * {@link #connect(Gate, int, int)}.
     *
     * @param arg0 The gate input you want to connect this output.
     * @return The Cable making the connection, or null if the connection is
     *         impossible (incompatible bus size).
     * @throws Exception            Throw an exception if the connection is possible
     *                              but
     *                              different Cable are already connected to both.
     * @throws NullPointerException When arg0 is null.
     */
    public Cable connect(Gate arg0) throws Exception, NullPointerException {
        if (arg0 == null) {
            throw new NullPointerException("Expected arg0 to be an instance of Gate, received null");
        }

        // placeholder result
        Cable result = null;

        // for the exception throw later
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
                        // if we do not found a correct cable in this loop, tis flag will throw the
                        // error later on
                        matchedButFull = true;
                    } else // if both empty, create a new cable and connect
                    if (thisOutputCable == null && arg0InputCable == null) {
                        try {
                            result = new Cable(thisoutputBusSize);
                        } catch (BusSizeException e) {
                            throw new Error(e);
                        }
                        result.inputGate = this;
                        result.outputGate = arg0;

                        this.outputCable.set(i, result);
                        arg0.inputCable.set(j, result);

                        result.updatePower();
                        arg0.updatePower();
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
                        if (thisOutputCable.outputGate != null) {
                            throw new Exception("connection possible but bus already full");
                        }
                        thisOutputCable.outputGate = arg0;
                        arg0.inputCable.set(j, thisOutputCable);
                        thisOutputCable.setOutputPort(j);

                        thisOutputCable.updatePower();
                        arg0.updatePower();
                        thisOutputCable.updateState();
                        arg0.updateState();
                        return thisOutputCable;
                    } else if (thisOutputCable == null && arg0InputCable != null) {
                        if (arg0InputCable.inputGate != null) {
                            throw new Exception("connection possible but bus already full");
                        }
                        arg0InputCable.inputGate = this;
                        this.outputCable.set(i, arg0InputCable);
                        arg0InputCable.setOutputPort(i);

                        arg0InputCable.updatePower();
                        arg0.updatePower();
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
            throw new Exception("connection possible but bus already full");
        }

        return null;
    }

    /**
     * Connect this gate with the arg0 gate, at the given index. Depending of
     * the case, it either get an existing Cable or create a new one. It also
     * make the necessary internal modification to both gate and cable.
     *
     * @param arg0            The gate input you want to connect this output.
     * @param thisOutputIndex The index in this.outputCable ArrayList.
     * @param arg0InputIndex  The index in arg0.inputCable ArrayList.
     * @return The cable making the connection, or null if the connection is
     *         impossible (incompatible bus size).
     * @throws Exception                 Throw an exception if the connection is
     *                                   possible but
     *                                   different Cable are already connected to
     *                                   both.
     * @throws NullPointerException      When arg0 is null.
     * @throws IndexOutOfBoundsException When thisOutputIndex and/or
     *                                   arg0InputIndex are below 0, or above or
     *                                   equal to
     *                                   this.{@link #outputBus}.length and
     *                                   arg0.{@link #inputBus}.length
     *                                   respectively.
     * @throws BusSizeException          Thrown when the given idenxes correspond to
     *                                   different sizes of bus.
     */
    public Cable connect(Gate arg0, int thisOutputIndex, int arg0InputIndex)
            throws Exception, NullPointerException, IndexOutOfBoundsException, BusSizeException {
        if (arg0 == null) {
            throw new NullPointerException("Expected arg0 to be an instance of Gate, received null");
        }

        if (thisOutputIndex < 0 || this.outputBus.length <= thisOutputIndex) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= thisOutputIndex < " + this.outputBus.length + ", received " + thisOutputIndex);
        }
        if (arg0InputIndex < 0 || arg0.outputBus.length <= arg0InputIndex) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= arg0InputIndex < " + arg0.outputBus.length + ", received " + arg0InputIndex);
        }

        if (this.outputBus[thisOutputIndex] != arg0.inputBus[arg0InputIndex]) {
            throw new BusSizeException("Expected bus size to be the same, received "
                    + this.outputBus[thisOutputIndex] + " and " + arg0.inputBus[arg0InputIndex]);
        }

        // check if both gate are already linked
        Cable thisOutputCable = this.outputCable.get(thisOutputIndex);
        Cable arg0InputCable = arg0.inputCable.get(arg0InputIndex);
        if (thisOutputCable != null && arg0InputCable != null) {
            if (thisOutputCable.equals(arg0InputCable)) {
                return thisOutputCable;
            } else if (thisOutputCable.getBusSize() != arg0InputCable.getBusSize()) {
                // incompatible sizes
                return null;
            } else {
                throw new Exception("connection possible but bus already full");
            }
        } else // check if both cable are empty
        if (thisOutputCable == null && arg0InputCable == null) {
            Cable result = new Cable(this.outputBus[thisOutputIndex]);
            result.inputGate = this;
            result.outputGate = arg0;
            result.setInputPort(thisOutputIndex);
            result.setOutputPort(arg0InputIndex);

            this.outputCable.set(thisOutputIndex, result);
            arg0.inputCable.set(arg0InputIndex, result);

            result.updatePower();
            result.updateState();
            return result;
        } else // if either is null
        if (thisOutputCable != null && arg0InputCable == null) {
            if (thisOutputCable.outputGate != null) {
                throw new Exception("connection possible but bus already full");
            }
            thisOutputCable.outputGate = arg0;
            arg0.inputCable.set(arg0InputIndex, thisOutputCable);

            arg0.updatePower();
            arg0.updateState();
            return thisOutputCable;
        } else if (thisOutputCable == null && arg0InputCable != null) {
            if (arg0InputCable.inputGate != null) {
                throw new Exception("connection possible but bus already full");
            }
            arg0InputCable.inputGate = this;
            this.outputCable.set(thisOutputIndex, arg0InputCable);

            arg0InputCable.updatePower();
            arg0InputCable.updateState();
            return arg0InputCable;
        }

        return null;
    }

    // #region connectInnerInputGate

    /**
     * FIXME javadoc
     * Connects an inner input of a schema to a specific input port of a gate.
     * This method establishes a cable connection between the schema's inner input port
     * and the specified gate's input port.
     * 
     * @param schema the schema containing the inner input to connect
     * @param schemaInnerInputIndex the index of the schema's inner input port to connect,
     *                             or -1 to automatically find/create an available port
     * @param gate the target gate to connect to
     * @param gateInputIndex the index of the gate's input port to connect to

     * @return the Cable object representing the established connection, or null if
     *         connection failed due to incompatible bus sizes
     * @throws Exception if connection is possible but bus is already full
     * @throws NullPointerException if the gate parameter is null
     * @throws IndexOutOfBoundsException if gateInputIndex is outside valid range
     * @throws BusSizeException if there are bus size compatibility issues
     */
    public static Cable connectInnerInputGate(Schema schema, int schemaInnerInputIndex, Gate gate, int gateInputIndex)
            throws Exception, NullPointerException, IndexOutOfBoundsException, BusSizeException {
        // verifications
        if (gate == null) {
            throw new NullPointerException(
                    "Expected arg0 to be an instance of Gate, received null");
        }
        if (gateInputIndex < 0 || gate.getInputBus().length <= gateInputIndex) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= gateInputIndex < " + gate.getInputBus().length + ", received " + gateInputIndex);
        }

        // set the input bus size at the right port
        if (schemaInnerInputIndex == -1) {
            // if the port index is not specified we try to find an unused index
            int indexNotUsed = schema.getInnerInputCable().indexOf(null);
            if (indexNotUsed != -1) {
                schemaInnerInputIndex = indexNotUsed;
            } else {
                // if we dont find an unused index, we will put it at the end of the ArrayList
                schemaInnerInputIndex = schema.getInnerInputCable().size();
            }
        }
        // The port is now precised, but we need 'schema.inputBus' to have enough place
        // to put the value at the right index
        while (schema.inputBus.length <= schemaInnerInputIndex) {
            // FIXME : might cause a problem if done wrong (too few or too many index
            // created)
            // we put unused bus size and cable index (hopefully they will be used, they
            // need to be)
            schema.addInputBus(1);
            schema.getInnerInputCable().add(null);
            schema.inputCable.add(null);

            assert (schema.inputBus.length == schema.getInnerInputCable().size()
                    && schema.inputBus.length == schema.inputCable.size());
        }

        // we set the bus size at the right index 'schemaInnerInputIndex'
        schema.inputBus[schemaInnerInputIndex] = gate.getInputBus()[gateInputIndex];

        // We now create a Cable between the schema and the inner gate
        // check if both gate are already linked
        Cable thisInnerInputCable = schema.getInnerInputCable().get(schemaInnerInputIndex);
        Cable gateInputCable = gate.getInputCable().get(gateInputIndex);
        if (thisInnerInputCable != null && gateInputCable != null) {
            if (thisInnerInputCable.equals(gateInputCable)) {
                return thisInnerInputCable;
            } else if (thisInnerInputCable.getBusSize() != gateInputCable.getBusSize()) {
                // incompatible sizes
                return null;
            } else {
                throw new Exception("connection possible but bus allready full");
            }
        } else // check if both cable are empty
        if (thisInnerInputCable == null && gateInputCable == null) {
            Cable result = new Cable(schema.outputBus[schemaInnerInputIndex]);
            result.inputGate = schema;
            result.outputGate = gate;
            result.setInputPort(schemaInnerInputIndex);
            result.setOutputPort(gateInputIndex);

            schema.getInnerInputCable().set(schemaInnerInputIndex, result);
            gate.getInputCable().set(gateInputIndex, result);

            result.updatePower();
            result.updateState();
            return result;
        } else // if either is null
        if (thisInnerInputCable != null && gateInputCable == null) {
            if (thisInnerInputCable.getOutputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            thisInnerInputCable.outputGate = gate;
            gate.getInputCable().set(gateInputIndex, thisInnerInputCable);

            gate.updatePower();
            gate.updateState();
            return thisInnerInputCable;
        } else if (thisInnerInputCable == null && gateInputCable != null) {
            if (gateInputCable.getInputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            gateInputCable.inputGate = schema;
            schema.getInnerInputCable().set(schemaInnerInputIndex, gateInputCable);

            gateInputCable.updatePower();
            gateInputCable.updateState();
            return gateInputCable;
        }

        return null;
    }

    // #endregion

    // #region connectInnerOutputGate
    // TODO : verif that schema's attributes are accessible

    /**
     * FIXME javadoc
     * Connects an inner output of a schema to a specific output port of a gate.
     * This method establishes a cable connection between the gate's output port
     * and the schema's inner output port, allowing the gate's output to be 
     * accessible through the schema's interface.
     * 
     * @param schema the schema containing the inner output to connect
     * @param gate the source gate to connect from
     * @param gateOutputIndex the index of the gate's output port to connect from
     * @param schemaInnerOutputIndex the index of the schema's inner output port to connect,
     *                              or -1 to automatically find/create an available port
     * 
     * @return the Cable object representing the established connection, or null if
     *         connection failed due to incompatible bus sizes
     * @throws Exception if connection is possible but bus is already full
     * @throws NullPointerException if the gate parameter is null
     * @throws IndexOutOfBoundsException if gateOutputIndex is outside valid range
     * @throws BusSizeException if there are bus size compatibility issues
    */
    public static Cable connectInnerOutputGate(Schema schema, int schemaInnerOutputIndex, Gate gate, int gateOutputIndex)
            throws Exception, NullPointerException, IndexOutOfBoundsException, BusSizeException {
        // verifications
        if (gate == null) {
            throw new NullPointerException(
                    "Expected arg0 to be an instance of Gate, received null");
        }
        if (gateOutputIndex < 0 || gate.getOutputBus().length <= gateOutputIndex) {
            throw new IndexOutOfBoundsException(
                    "Expected 0 <= gateOutputIndex < " + gate.getOutputBus().length + ", received " + gateOutputIndex);
        }

        // set the output bus size at the right port
        if (schemaInnerOutputIndex == -1) {
            // if the port index is not specified we try to find an unused index
            int indexNotUsed = schema.getInnerOutputCable().indexOf(null);
            if (indexNotUsed != -1) {
                schemaInnerOutputIndex = indexNotUsed;
            } else {
                // if we dont find an unused index, we will put it at the end of the ArrayList
                schemaInnerOutputIndex = schema.getInnerOutputCable().size();
            }
        }
        // The port is now precised, but we need 'schema.outputBus' to have enough place
        // to put the value at the right index
        while (schema.outputBus.length <= schemaInnerOutputIndex) {
            // FIXME : might cause a problem if done wrong (too few or too many index
            // created)
            // we put unused bus size and cable index (hopefully they will be used, they
            // need to be)
            schema.addOutputBus(1);
            schema.getInnerOutputCable().add(null);
            schema.outputCable.add(null);

            assert (schema.outputBus.length == schema.getInnerOutputCable().size()
                    && schema.outputBus.length == schema.outputCable.size());
        }

        // we set the bus size at the right index 'schemaInnerOutputIndex'
        schema.outputBus[schemaInnerOutputIndex] = gate.getOutputBus()[gateOutputIndex];

        // We now create a Cable between the schema and the inner gate
        // check if both gate are already linked
        Cable thisInnerOutputCable = schema.getInnerOutputCable().get(schemaInnerOutputIndex);
        Cable gateOutputCable = gate.getOutputCable().get(gateOutputIndex);
        if (thisInnerOutputCable != null && gateOutputCable != null) {
            if (thisInnerOutputCable.equals(gateOutputCable)) {
                return thisInnerOutputCable;
            } else if (thisInnerOutputCable.getBusSize() != gateOutputCable.getBusSize()) {
                // incompatible sizes
                return null;
            } else {
                throw new Exception("connection possible but bus allready full");
            }
        } else // check if both cable are empty
        if (thisInnerOutputCable == null && gateOutputCable == null) {
            Cable result = new Cable(schema.outputBus[schemaInnerOutputIndex]);
            result.inputGate = gate;
            result.outputGate = schema;
            result.setInputPort(gateOutputIndex);
            result.setOutputPort(schemaInnerOutputIndex);

            schema.getInnerOutputCable().set(schemaInnerOutputIndex, result);
            gate.getOutputCable().set(gateOutputIndex, result);

            result.updatePower();
            result.updateState();
            return result;
        } else // if either is null
        if (thisInnerOutputCable != null && gateOutputCable == null) {
            if (thisInnerOutputCable.getInputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            thisInnerOutputCable.inputGate = gate;
            gate.getOutputCable().set(gateOutputIndex, thisInnerOutputCable);

            gate.updatePower();
            gate.updateState();
            return thisInnerOutputCable;
        } else if (thisInnerOutputCable == null && gateOutputCable != null) {
            if (gateOutputCable.getOutputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            gateOutputCable.outputGate = schema;
            schema.getInnerOutputCable().set(schemaInnerOutputIndex, gateOutputCable);

            gateOutputCable.updatePower();
            gateOutputCable.updateState();
            return gateOutputCable;
        }

        return null;
    }

    // #endregion


    /**
     * Disconnect a cable from any input or output of this gate, also removing
     * this gate from the cable POV.
     *
     * @param cable The cable to disconnect.
     */
    public void disconnect(Cable cable) {
        // cable if null
        if (cable == null) {
            return;
        }

        // disconnect it from the input
        if (getInputCable().contains(cable)) {
            // disconnect this gate from the cable
            cable.outputGate = null;
            // disconnect the cable from this gate
            for (int i = 0; i < getInputCable().size(); i++) {
                if (cable.equals(getInputCable().get(i))) {
                    inputCable.set(i, null);
                    updatePower();
                    updateState();
                }
            }
        }

        // disconnect it from the output
        if (getOutputCable().contains(cable)) {
            // disconnect this gate from the cable
            cable.inputGate = null;
            cable.updatePower();
            cable.updateState();

            // disconnect the cable from this gate
            for (int i = 0; i < getOutputCable().size(); i++) {
                if (cable.equals(getOutputCable().get(i))) {
                    outputCable.set(i, null);
                }
            }
        }
    }

    /**
     * Disconnect this gate from all its input and output cable.
     */
    public void disconnect() {
        inputCable.forEach(cable -> {
            disconnect(cable);
        });
        outputCable.forEach(cable -> {
            disconnect(cable);
        });
    }

    /**
     * Get the cable between this and arg0, or return null if no connection
     * exists. The check direction is from this.output to arg0.input.
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
                if (thisCable != null
                        && arg0Cable != null
                        && thisCable.uuid().equals(arg0Cable.uuid())) {
                    return thisCable;
                }
            }
        }

        return null;
    }

    /**
     * Get the cable between this and arg0, or return null if no connection
     * exists. The check direction if from this.output to arg0.input.
     *
     * @param arg0    The gate we want to check if there is a cable connecting us.
     * @param busSize The specific bus size of the want the return cable to
     *                have.
     * @return The Cable if it exists, null otherwise.
     */
    public Cable getCableWith(Gate arg0, int busSize) {
        if (arg0 == null || BusSizeException.isBusSizeException(busSize)) {
            return null;
        }

        for (Cable thisCable : outputCable) {
            for (Cable arg0Cable : arg0.getInputCable()) {
                if (thisCable != null
                        && arg0Cable != null
                        && thisCable.uuid().equals(arg0Cable.uuid())
                        && thisCable.getBusSize() == busSize) {
                    return thisCable;
                }
            }
        }

        return null;
    }

    /**
     * This function make sure that input and output cable ArrayList can be
     * add/set to their input and output bus sizes. It fill them with null.
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
     * @throws BusSizeException Throw when the given size is equal or below 0,
     *                          not a power of 2, or greater than 32.
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
        disconnect(getInputCable().get(index));
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
     * @throws BusSizeException Throw when the given size is equal or below 0,
     *                          not a power of 2, or greater than 32.
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
        disconnect(getOutputCable().get(index));
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
     * @return Return if true if all existing cables have valid bus size. If
     *         false, some cable have been disconnected.
     * @throws BusSizeException     Throw when the given size is equal or below 0,
     *                              not a power of 2, or greater than 32.
     * @throws NullPointerException Throw when inputBus is null or contains a
     *                              null.
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

        if (inputBus.length == 0 && getOutputNumber() == 0) {
            throw new BusSizeException(
                    "Standalone gate: Given inputBus array is empty, but outputBus array is also empty");
        }

        this.inputBus = inputBus;

        ensureCapacity();

        // since it replace all existing cable, only check if size is compatible with
        // already connected cable
        boolean valid = this.inputCable.size() <= this.inputBus.length;
        for (int i = 0; i < this.inputCable.size() && i < this.inputBus.length; i++) {
            Cable cable = this.inputCable.get(i);
            if (cable != null && cable.getBusSize() != this.inputBus[i]) {
                disconnect(cable);
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Set a specific {@link #inputBus} at the given index.
     *
     * @param busSize The size of the bus.
     * @param index   The index of the new busSize. Should be a valid index, lower
     *                than {@link #inputBus}.length.
     * @return Return if true if all existing cables have valid bus size. If
     *         false, some cable have been disconnected.
     * @throws BusSizeException          Throw when the given size is equal or below
     *                                   0,
     *                                   not a power of 2, or greater than 32.
     * @throws IndexOutOfBoundsException Throw when index is lower than 0, or
     *                                   equal or greater than
     *                                   {@link #inputBus}.length.
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

        final boolean result = getInputCable().get(index) == null
                || busSize == getInputCable().get(index).getBusSize();

        if (!result) {
            disconnect(getInputCable().get(index));
        }

        return result;
    }

    /**
     * Setter for {@link #outputBus}.
     *
     * @param outputBus The new bus output sizes array.
     * @return Return if true if all existing cables have valid bus size. If
     *         false, some cable have been disconnected.
     * @throws BusSizeException     Throw when the given size is equal or below 0,
     *                              not a power of 2, or greater than 32.
     * @throws NullPointerException Throw when inputBus is null or contains a
     *                              null.
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
                disconnect(cable);
                valid = false;
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
     * @return Return if true if all existing cables have valid bus size. If
     *         false, some cable have been disconnected.
     * @throws BusSizeException          Throw when the given size is equal or below
     *                                   0,
     *                                   not a power of 2, or greater than 32.
     * @throws IndexOutOfBoundsException Throw when index is lower than 0, or
     *                                   equal or greater than
     *                                   {@link #outputBus}.length.
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

        final boolean result = getOutputCable().get(index) == null
                || busSize == getOutputCable().get(index).getBusSize();

        if (!result) {
            disconnect(getOutputCable().get(index));
        }

        return result;
    }
    // #endregion

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        // Save size of bus
        // input
        JSONArray inputBusArray = new JSONArray();
        for (int size : getInputBus()) {
            inputBusArray.put(size);
        }
        json.put("inputBus", inputBusArray);

        // output
        JSONArray outputBusArray = new JSONArray();
        for (int size : getOutputBus()) {
            outputBusArray.put(size);
        }
        json.put("outputBus", outputBusArray);

        // Save gate connections with other gates
        // e.g. [[GATE, portIndexFromGATE], ["AnotherGATE"], portIndexFromAnotherGATE]
        // From output port
        JSONArray outputGatesArray = new JSONArray();
        JSONArray rowOutput = new JSONArray();
        for (int indexPortOutput = 0; indexPortOutput < this.getOutputCable().size(); indexPortOutput++) {
            if (this.getOutputCable().get(indexPortOutput) == null)
                continue;

            rowOutput.clear();
            // id of the target Gate
            rowOutput.put(0, this.getOutputCable().get(indexPortOutput).getOutputGate().uuid());
            // index of the port of the target Gate
            rowOutput.put(1, this.getOutputCable().get(indexPortOutput).getOutputPort());

            outputGatesArray.put(rowOutput);
        }
        json.put("outputTo", outputGatesArray);

        // From input port
        JSONArray inputGatesArray = new JSONArray();
        JSONArray rowInput = new JSONArray();
        for (int indexPortInput = 0; indexPortInput < this.getInputCable().size(); indexPortInput++) {
            if (this.getInputCable().get(indexPortInput) == null)
                continue;

            rowInput.clear();
            // id of the target Gate
            rowInput.put(0, this.getInputCable().get(indexPortInput).getInputGate().uuid());
            // index of the port of the target Gate
            rowInput.put(1, this.getInputCable().get(indexPortInput).getInputPort());

            inputGatesArray.put(rowInput);
        }
        json.put("inputFrom", inputGatesArray);

        return json;
    }

    /*
     * TODO add them if needed
     * protected void setInputCable(ArrayList<Cable> inputCable) throws Error {}
     * protected void setInputCable(Cable inputCable, int index) throws Error {}
     * protected void setOutputCable(ArrayList<Cable> outputCable) throws Error {}
     * protected void setOutputCable(Cable outputCable, int index) throws Error {}
     */
}
