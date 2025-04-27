package com.pjava.src.components;

import java.util.BitSet;
import com.pjava.src.components.cables.Cable;

/**
 * The interface that should be implemented as a base for all components.
 * It should be able to old a state, and transmit it.
 * It should also have a power state, to represent if electricity is being
 * provided to the component.
 * It also should have inputs and outputs, to communicate with other components.
 *
 * @see Cable
 * @see Gate
 */
public interface Component {
    /**
     * Whether the component is powered or not. Tells if all inputs are filled and
     * are powered too.
     * An unpowered component should not transmit nor emit updates.
     */
    boolean powered = false;

    /**
     * The previous state of the component. Should always be a clone.
     */
    BitSet oldState = new BitSet();

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
     * Setter for {@link #oldState}.
     * Internal function that set the oldState to a clone of the current state.
     * If {@link #getState()} returns null, does not update {@link #oldState}.
     */
    void setOldState();

    /**
     * This function is called when inputs state change.
     * It should updates the return the state accordingly to the inputs.
     * It should also return null if any of the inputs are null.
     *
     * @return The current state.
     */
    abstract BitSet getState();

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
