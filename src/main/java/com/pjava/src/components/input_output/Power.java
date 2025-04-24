package com.pjava.src.components.input_output;

import java.util.BitSet;

import com.pjava.src.components.Gate;
import com.pjava.src.utils.Utils;

public class Power extends Gate {
    /**
     * The constant value of 0.
     */
    private BitSet value;

    /**
     * Create a new Power gate. Default size is 1.
     */
    public Power() {
        super(new Integer[] {}, new Integer[] { 1 });
        setPowered(true);
        setIgnorePropagationCheck(true);
        value = new BitSet(1);
        value.set(0);
    }

    /**
     * Create a new Power gate with the given size.
     *
     * @param size The size of the output bus. Size must be a power of 2 between 1
     *             and 32 inclusive.
     * @throws Error If size if invalid.
     */
    public Power(int size) throws Error {
        if (!Utils.isPower2(size)) {
            throw new Error(
                    "size must be a power of 2.\nExpected a value being a power of 2 between 1 and 32, got " + value);
        }

        setPowered(true);
        setIgnorePropagationCheck(true);
        value = new BitSet(size);
        value.set(0, value.size() - 1);
    }

    @Override
    public BitSet getState() {
        // in case the internal values changed
        value.set(0, value.size() - 1);
        return value;
    }
}
