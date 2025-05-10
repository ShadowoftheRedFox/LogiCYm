package com.pjava.src.components;

import java.util.BitSet;
import java.util.Objects;

import com.pjava.src.utils.Utils;

/**
 * The class that should be implemented as a base for all components.
 * It should be able to old a state, and transmit it.
 * It should also have a power state, to represent if electricity is being
 * provided to the component.
 * It also should have inputs and outputs, to communicate with other components.
 *
 * @see Cable
 * @see Gate
 */
public abstract class Element {
    /**
     * A unique id to differentiate components between each others.
     */
    private int uuid = Utils.runtimeID();

    /**
     * A unique id to differentiate components between each others.
     *
     * @return The id.
     */
    public Integer uuid() {
        return uuid;
    };

    /**
     * Whether the component is powered or not. Tells if all inputs are filled and
     * are powered too.
     * An unpowered component should not transmit nor emit updates.
     */
    private boolean powered = false;

    /**
     * Getter for {@link #powered}.
     *
     * @return Whether this gate is powered or not.
     */
    public boolean getPowered() {
        return powered;
    };

    /**
     * Setter for {@link #powered}.
     *
     * @param powered True if powered, false if not.
     */
    protected void setPowered(boolean powered) {
        this.powered = powered;
    }

    /**
     * Should be called when inputs or outputs changes.
     * This is when power is being check recursively.
     */
    abstract void updatePower();

    protected BitSet state = new BitSet(1);

    /**
     * Return a clone of the current state
     *
     * @return The current state.
     */
    public BitSet getState() {
        return (BitSet) state.clone();
    };

    /**
     * Shorthand for {@code #getState().get(x)}.
     *
     * @param x The index of the xth byte.
     * @return The state of the xth bit of the state.
     * @throws Error                    Throws when #{@link #getState()} is null
     * @throws IllegalArgumentException Throws when x less than 0.
     */
    public boolean getState(int x) throws Error, IllegalArgumentException {
        if (x < 0) {
            throw new IllegalArgumentException("Expected x greater or equal than 0, received " + x);
        }
        BitSet state = getState();
        if (state == null) {
            throw new Error("getState returned null");
        }
        return state.get(x);
    };

    /**
     * This function is called when inputs state change.
     * Equivalent of {@code updateState(true)} ({@link #updateState(boolean)}).
     */
    public void updateState() {
        updateState(true);
    };

    /**
     * This function is called when inputs state changes.
     * It should updates the return the state accordingly to the inputs.
     * It should also return null if any of the inputs are null.
     *
     * @param propagate Whether or not to propagate the changes to the outputs.
     */
    abstract void updateState(boolean propagate);

    /**
     * Return the number of inputs.
     *
     * @return The number of inputs.
     */
    abstract Integer getInputNumber();

    /**
     * Return the number of ouputs.
     *
     * @return The number of output.
     */
    abstract Integer getOutputNumber();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Element) {
            return obj == this || ((Element) obj).uuid() == uuid();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + uuid();
    }
}
