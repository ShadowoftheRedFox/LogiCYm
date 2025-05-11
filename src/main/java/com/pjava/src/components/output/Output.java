package com.pjava.src.components.output;

import com.pjava.src.components.Gate;

/**
 * An output gate.
 * Outputs only have inputs. Functions in {@link Gate} used to edit outputs
 * always throws error.
 */
public abstract class Output extends Gate {
    /**
     * Create a new Output with the given amount of outputs.
     *
     * @param busOutput The output buses array.
     */
    public Output(int[] busOutput) {
        super(busOutput, new int[] {});
    }

    @Override
    protected boolean setOutputBus(int[] busSizes) throws Error {
        throw new Error("Outputs can't be modified in an Output class");
    }

    @Override
    protected boolean setOutputBus(int busSize, int index) throws Error {
        throw new Error("Outputs can't be modified in an Output class");
    }

    @Override
    protected void addOutputBus(int size) throws Error {
        throw new Error("Outputs can't be modified in an Output class");
    }

    @Override
    protected void removeOutputBus(int index) throws Error {
        throw new Error("Outputs can't be modified in an Output class");
    }
}
