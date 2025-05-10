package com.pjava.src.components.ouptut;

import java.util.BitSet;

import com.pjava.src.components.Gate;

public class Display extends Gate {
    @Override
    public BitSet getState() {
        // no output
        return null;
    }

    /**
     * The number base that is displayed: 2, 8, 10, 16.
     */
    private int base = 2;
}
