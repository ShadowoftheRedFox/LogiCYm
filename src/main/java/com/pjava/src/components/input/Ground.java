package com.pjava.src.components.input;

import java.util.BitSet;

/**
 * Represent the ground gate. It always return a state of 0. It doesn't have
 * any inputs, and has only one output bus of size 1. It is always powered. It
 * ignores propagation check.
 */
public class Ground extends Input {
    /**
     * The constant value of 0.
     */
    private final BitSet value;

    /**
     * Create a new Ground gate. Default size is 1.
     */
    public Ground() {
        this(1);
    }

    /**
     * Create a new Ground gate with the given size.
     *
     * @param size The size of the output bus. Size must be a power of 2 between 1
     *             and 32 inclusive.
     */
    public Ground(int size) {
        super(new int[] { size });

        setPowered(true);
        value = new BitSet(size);
        value.clear();
    }


    @Override
    public BitSet getState() {
        // in case the internal values changed
        value.clear();
        return value;
    }

    /**
     * Create an exact copy of this ground instance in a new one. Connected
     * referenced cables are not cloned.
     *
     * @return The newly cloned ground.
     */
    @Override
    public Ground clone() {
        try {
            Ground clone = new Ground(getInputBus()[0]);
            clone.setInputCable(getInputCable());
            clone.setOutputCable(getOutputCable());
            clone.setPowered(getPowered());
            return clone;
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
