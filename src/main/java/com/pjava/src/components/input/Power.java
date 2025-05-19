package com.pjava.src.components.input;

import java.util.BitSet;

/**
 * Represent the power gate. It always return a state of 1. It doesn't have
 * any inputs, and has only one output bus of size 1. It is always powered. It
 * ignores propagation check.
 */
public class Power extends Input {
    /**
     * The constant value of 0.
     */
    private final BitSet value;

    /**
     * Create a new Power gate. Default size is 1.
     */
    public Power() {
        this(1);
    }

    /**
     * Create a new Power gate with the given size.
     *
     * @param size The size of the output bus. Size must be a power of 2 between 1
     *             and 32 inclusive.
     */
    public Power(int size) {
        super(new int[] { size });

        setPowered(true);
        value = new BitSet(size);
        value.set(0);
    }


    @Override
    public BitSet getState() {
        // in case the internal values changed
        value.set(0);
        return value;
    }

    /**
     * Create an exact copy of this power instance in a new one. Connected
     * referenced cables are not cloned.
     *
     * @return The newly cloned power.
     */
    @Override
    public Power clone() {
        try {
            Power clone = new Power(getInputBus()[0]);
            clone.setInputCable(getInputCable());
            clone.setOutputCable(getOutputCable());
            clone.setPowered(getPowered());
            return clone;
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
