package com.pjava.src.components.output;

import java.util.BitSet;

public class Display extends Output {
    /**
     * The number base that is displayed: 2, 8, 10, 16.
     */
    private int base = 2;

    public Display(int inputSize) {
        super(new int[] { inputSize });
    }

    @Override
    public BitSet getState() {
        // no output
        return null;
    }
}
