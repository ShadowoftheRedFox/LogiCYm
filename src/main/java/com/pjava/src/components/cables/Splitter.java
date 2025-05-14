package com.pjava.src.components.cables;

import java.util.BitSet;
import java.util.Collections;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.errors.BusSizeException;

/**
 * A cable splitter. Split a given cable in multiple cables of bus size equals
 * to 1. Since the output state is different for each cable,
 * {@link #getState(Cable)} should be used.
 */
public class Splitter extends Gate {
    /**
     * Create a new splitter with the given input bus size. Create the same number
     * of output cable, each of bus size equal to 1.
     *
     * @param input The size of the input bus.
     * @throws Exception Throws if {@link #setInputBus(int[])} throws.
     */
    public Splitter(int input) throws Exception {
        super(new int[] { input }, new int[0]);
        setInputBus(new int[] { input });
    }

    /**
     * A mirror of the input. Use {@link #getState(Cable)} or {@link #getState(int)}
     * instead.
     *
     * @deprecated
     */
    @Override
    public BitSet getState() {
        if (getInputCable().get(0) == null) {
            return null;
        }
        return getInputCable().get(0).getState();
    }

    /**
     * Return the correct state for the output cable provided, since each output
     * cable have a different output result than the normal {@link #getState()}
     * provided in {@link Gate}.
     *
     * @param cable One of the {@link #getOutputCable()} element.
     * @return The correct state for the given cable.
     * @throws NullPointerException Throws if cable is null.
     * @throws Exception            Throws if cable is not one of the element in
     *                              {@link #getOutputCable()}.
     * @see #getState(int)
     */
    public BitSet getState(Cable cable) throws NullPointerException, Exception {
        if (cable == null) {
            throw new NullPointerException("Expected cable to be an instance of Cable, received null");
        }
        if (!getOutputCable().contains(cable)) {
            throw new Exception("Provided cable is not an output of this gate");
        }

        BitSet result = new BitSet(1);

        for (int i = 0; i < getOutputCable().size(); i++) {
            if (cable.equals(getOutputCable().get(i))) {
                // get the state of the i th bit
                result.set(0, getInputCable().get(0).getState(i));
                return result;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc} Given array should be of length equal to 1.
     */
    @Override
    protected boolean setInputBus(int[] inputBus) throws BusSizeException, NullPointerException {
        if (inputBus != null && inputBus.length != 1) {
            throw new BusSizeException("Splitter can only have one input, received: " + inputBus.length);
        }
        setOutputBus(Collections.nCopies(inputBus[0], 1).stream().mapToInt(Integer::intValue).toArray());
        return super.setInputBus(inputBus);
    }

    /**
     * Use {@link #setInputBus(int[])} instead.
     *
     * @deprecated
     */
    @Override
    protected void removeInputBus(int index) throws IllegalArgumentException {
        throw new IllegalArgumentException("Splitter can only have one input");

    }

    /**
     * Use {@link #setInputBus(int[])} instead.
     *
     * @deprecated
     */
    @Override
    protected void addInputBus(int size) throws BusSizeException {
        throw new BusSizeException("Splitter can only have one input");
    }
}
