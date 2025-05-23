package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;

import org.json.JSONObject;

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
     * Interface used that is used to handle event relative to {@link Element}.
     */
    public interface ElementEvent {
        /**
         * Event to handle the updateState of the given element.
         *
         * @param element The element that has been updated.
         */
        default public void updateState(Element element) {
        }

        /**
         * Event to handle the updatePower of the given element.
         *
         * @param element The element that has been updated.
         */
        default public void updatePower(Element element) {
        }
    }

    /**
     * Array of listener to this gate events.
     */
    protected ArrayList<ElementEvent> updateEvent = new ArrayList<ElementEvent>();

    /**
     * Create a new element.
     */
    public Element() {
    }

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
        updateEvent.forEach(listener -> {
            listener.updatePower(this);
        });
    }

    /**
     * Should be called when inputs or outputs changes.
     * This is when power is being check recursively.
     */
    abstract void updatePower();

    /**
     * Internal state of the element.
     */
    private BitSet state = new BitSet(1);

    /**
     * Getter of {@link #state}.
     * This function is called when inputCable changes. It should updates the
     * return the state accordingly to the inputs. It should also return null if
     * any of the inputs are null.
     *
     * @return The clone of the current state.
     */
    public BitSet getState() {
        return (BitSet) state.clone();
    };

    /**
     * Setter for {@link #state}.
     *
     * @param state The new state.
     */
    protected void setState(BitSet state) {
        if (state == null) {
            return;
        }
        this.state = state;
        updateEvent.forEach(listener -> {
            listener.updateState(this);
        });
    }

    /**
     * Getter for {@link #updateEvent}.
     *
     * @return The array of event listening to this element.
     */
    public ArrayList<ElementEvent> getUpdateEvents() {
        return updateEvent;
    }

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
     * This function is called when inputs state changes.
     * It should updates the return the state accordingly to the inputs.
     * It should also return null if any of the inputs are null.
     * <p>
     * </p>
     * Each call should check if it needs to propagate the signal. If yes, it must
     * add itself to the {@link Synchronizer#addToCallStack(Element)}.
     */
    abstract void updateState();

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

    /**
     * Convert the main informations of an element to JSON
     *
     * @return JSONObject
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("uuid", uuid());
        json.put("powered", getPowered());
        json.put("type", getClass().getSimpleName());

        return json;
    }
}
