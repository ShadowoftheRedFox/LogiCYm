package com.pjava.src.components.output;

import java.util.BitSet;

import com.pjava.src.components.Gate;

/**
 * An output gate.
 * Outputs only have inputs. Functions in {@link Gate} used to edit outputs
 * always do nothing.
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

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     *
     * @return Always false.
     */
    @Override
    protected final boolean setOutputBus(int[] busSizes) {
        return false;
    }

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     *
     * @return Always false.
     */
    @Override
    protected final boolean setOutputBus(int busSize, int index) {
        return false;
    }

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected final void addOutputBus(int size) {
    }

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected final void removeOutputBus(int index) {
    }

    /**
     * Since output have no outputs, getState should return null, since they process
     * nothing.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    public BitSet getState() {
        // no output
        return null;
    }

    /**
     * Since output have no outputs, getState should return null, since they process
     * nothing.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    public BitSet getState() {
        // no output
        return null;
    }
}
