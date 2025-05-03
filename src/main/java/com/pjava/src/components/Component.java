
package com.pjava.src.components;

import java.util.BitSet;

/**
 * The interface that should be implemented as a base for all components.
 * It should be able to old a state, and transmit it.
 * It should also have a power state, to represent if electricity is being
 * provided to the component.
 * It also should have inputs and outputs, to communicate with other components.
 *
 * Don't forget to also override equals, toString and hashCode for
 * compatibility.
 *
 * @see Cable
 * @see Gate
 */
public interface Component {
    /**
     * A unique id to differentiate components between each others.
     *
     * @return The id.
     */
    Integer uuid();

    /**
     * Whether the component is powered or not. Tells if all inputs are filled and
     * are powered too.
     * An unpowered component should not transmit nor emit updates.
     */
    boolean powered = false;

    /**
     * Getter for {@link #powered}.
     *
     * @return Whether this gate is powered or not.
     */
    boolean getPowered();

    /**
     * Setter for {@link #powered}.
     *
     * @param powered True if powered, false if not.
     */
    void setPowered(boolean powered);

    /**
     * Should be called when inputs or outputs changes.
     * This is when power is being check recursively.
     */
    void updatePower();

    /**
     * This function is called when inputs state change.
     * It should updates the return the state accordingly to the inputs.
     * It should also return null if any of the inputs are null.
     *
     * @return The current state.
     */
    abstract BitSet getState();

    /**
     * Shorthand for {@code #getState().get(x)}.
     *
     * @param x The index of the xth byte.
     * @return The state of the xth bit of the state.
     * @throws IllegalArgumentException Throws when x less than 0.
     */
    default boolean getState(int x) throws IllegalArgumentException {
        if (x < 0) {
            throw new IllegalArgumentException("Expected x greater or equal than 0, received " + x);
        }
        return getState().get(x);
    };

    /**
     * This function is called when inputs state change.
     * Equivalent of {@code updateState(true)} ({@link #updateState(boolean)}).
     */
    void updateState();

    /**
     * This function is called when inputs state changes.
     *
     * @param propagate Whether or not to propagate the changes to the outputs.
     */
    void updateState(boolean propagate);

    /**
     * Return the number of inputs.
     *
     * @return The number of inputs.
     */
    Integer getInputNumber();

    /**
     * Return the number of ouputs.
     *
     * @return The number of output.
     */
    Integer getOutputNumber();
}
