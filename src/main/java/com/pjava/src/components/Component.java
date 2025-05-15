
package com.pjava.src.components;

import java.util.BitSet;
<<<<<<< Updated upstream:src/main/java/com/pjava/src/components/Component.java
=======
import java.util.Objects;

import org.json.JSONObject;

import com.pjava.src.utils.Utils;
>>>>>>> Stashed changes:src/main/java/com/pjava/src/components/Element.java

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
     * @throws Error                    Throws when #{@link #getState()} is null
     * @throws IllegalArgumentException Throws when x less than 0.
     */
    default boolean getState(int x) throws Error, IllegalArgumentException {
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
<<<<<<< Updated upstream:src/main/java/com/pjava/src/components/Component.java
    Integer getOutputNumber();
=======
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

    // TODO JavaDoc
    /**
     * Convertit l'élément en objet JSON.
     * @return Un JSONObject représentant cet élément
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid());
        json.put("powered", getPowered());
        json.put("type", getClass().getSimpleName());
        return json;
    }
>>>>>>> Stashed changes:src/main/java/com/pjava/src/components/Element.java
}

