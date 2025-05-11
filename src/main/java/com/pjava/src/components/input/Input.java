package com.pjava.src.components.input;

import com.pjava.src.components.Gate;

/**
 * An input gate.
 * Inputs only have outputs. Functions in {@link Gate} used to edit inputs
 * always throws error.
 */
public abstract class Input extends Gate {
    /**
     * Create a new Input with the given amount of outputs.
     *
     * @param busOutput The output buses array.
     */
    public Input(int[] busOutput) {
        super(new int[] {}, busOutput);
    }

    @Override
    protected boolean setInputBus(int[] busSizes) throws Error {
        throw new Error("Inputs can't be modified in an Input class");
    }

    @Override
    protected boolean setInputBus(int busSize, int index) throws Error {
        throw new Error("Inputs can't be modified in an Input class");
    }

    @Override
    protected void addInputBus(int size) throws Error {
        throw new Error("Inputs can't be modified in an Input class");
    }

    @Override
    protected void removeInputBus(int index) throws Error {
        throw new Error("Inputs can't be modified in an Input class");
    }
}
