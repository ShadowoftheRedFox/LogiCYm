package com.pjava.src.components.cables;

import java.util.BitSet;
import java.util.Collections;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.errors.BusSizeException;

/**
 * Merge multiple cable of the same bus size into one.
 */
public class NodeMerger extends Gate {

    /**
     * Create a new merger gate with buses of size 1 and with 2 inputs.
     *
     * @throws Exception Throw Exception from either {@link #setInputBus(int[])} or
     *                   {@link #setOutputBus(int[])}.
     * @see Gate
     */
    public NodeMerger() throws Exception {
        this(2, 1);
    }

    /**
     * Create a new merger gate with the given buses size and with 2 inputs.
     *
     * @param busSize The size of the input buses.
     * @throws Exception Throw Exception from either {@link #setInputBus(int[])} or
     *                   {@link #setOutputBus(int[])}.
     * @see Gate
     */
    public NodeMerger(int busSize) throws Exception {
        this(2, busSize);
    }

    /**
     * Create a new merger gate with the given buses size and number of inputs.
     *
     * @param busNumber The amount of inputs.
     * @param busSize   The size of the bus.
     * @throws Exception                Throw Exception from either
     *                                  {@link #setInputBus(int[])} or
     *                                  {@link #setOutputBus(int[])}.
     * @throws IllegalArgumentException Throw IllegalArgumentException if bus number
     *                                  is below 0.
     * @see Gate
     */
    public NodeMerger(int busNumber, int busSize) throws Exception {
        super(Collections.nCopies(busNumber, busSize).stream().mapToInt(Integer::intValue).toArray(),
                new int[] { busSize });
    }

    /**
     * Return the addition (bitwise or) of all input cables.
     * {@inheritDoc}
     */
    @Override
    public BitSet getState() {
        BitSet result = new BitSet();
        for (Cable cable : getInputCable()) {
            if (cable != null && cable.getPowered()) {
                result.or(cable.getState());
            }
        }
        return result;
    }

    /**
     * Add one input to the merger.
     * Equivalent of {@code addInput(1)} ({@link #addInput(int)}).
     */
    public void addInput() {
        addInput(1);
    }

    /**
     * Add n input to the merger.
     *
     * @param n The amount of input to add.
     * @throws IllegalArgumentException Throw when n is below or equal to 0.
     */
    public void addInput(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("Expected n to be greater than 0, received " + n);
        }

        try {
            for (int i = 0; i < n; i++) {
                addInputBus(getOutputBus()[0]);
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
