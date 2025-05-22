package com.pjava.src.components.cables;

import java.util.BitSet;
import java.util.Collections;

import com.pjava.src.components.Gate;
import com.pjava.src.errors.BusSizeException;

/**
 * Split a single input into multiple input of the same bus size. The
 * NodeMerger is not made since Or gate serve the same exact purpose.
 */
public class NodeSplitter extends Gate {
    /**
     * Create a new splitter gate with buses of size 1 and with 2 outputs.
     *
     * @throws Exception Throw exceptions from either {@link #setInputBus(int[])} or
     *                   {@link #setOutputBus(int[])}.
     * @see Gate
     */
    public NodeSplitter() throws Exception {
        this(2, 1);
    }

    /**
     * Create a new splitter gate with the given buses size and with 2
     * outputs.
     *
     * @param busSize The size of the output buses.
     * @throws Exception Throw exceptions from either {@link #setInputBus(int[])} or
     *                   {@link #setOutputBus(int[])}.
     * @see Gate
     */
    public NodeSplitter(int busSize) throws Exception {
        this(2, busSize);
    }

    /**
     * Create a new splitter gate with the given buses size and number of
     * outputs.
     *
     * @param busNumber The amount of outputs.
     * @param busSize   The size of the bus.
     * @throws Exception                Throw exceptions from either
     *                                  {@link #setInputBus(int[])} or
     *                                  {@link #setOutputBus(int[])}.
     * @throws IllegalArgumentException Throw when bus number is below 0.
     * @see Gate
     */
    public NodeSplitter(int busNumber, int busSize) throws Exception {
        super(new int[] { busSize },
                Collections.nCopies(busNumber, busSize).stream().mapToInt(Integer::intValue).toArray());
    }


    /**
     * Return a copy of the input cable state.
     * {@inheritDoc}
     */
    @Override
    public BitSet getState() {
        return getInputCable().get(0).getState();
    }

    /**
     * Add one output to the splitter.
     * Equivalent of {@code addOutput(1)} ({@link #addOutput(int)}).
     */
    public void addOutput() {
        addOutput(1);
    }

    /**
     * Add n output to the splitter.
     *
     * @param n The amount of output to add.
     * @throws IllegalArgumentException Throw when n is below or equal to 0.
     */
    public void addOutput(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("Expected n to be greater than 0, received " + n);
        }

        try {
            for (int i = 0; i < n; i++) {
                addOutputBus(getInputBus()[0]);
            }
        } catch (BusSizeException e) {
            throw new Error(e);
        }
    }

    /**
     * Change the size of the buses, input and output, to the new bus size.
     *
     * @param busSize The new bus size.
     * @throws BusSizeException Throw when the given busSize is equal or below
     *                          0, not a power of 2, or greater than 32.
     */
    public void changeBusSize(int busSize) throws BusSizeException {
        setInputBus(Collections.nCopies(getInputNumber(), busSize).stream().mapToInt(Integer::intValue).toArray());
        setOutputBus(Collections.nCopies(getOutputNumber(), busSize).stream().mapToInt(Integer::intValue).toArray());
    }
}
