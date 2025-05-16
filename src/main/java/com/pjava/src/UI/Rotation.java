package com.pjava.src.UI;

/**
 * The rotation of a UIElement. Each element have an int value, corresponding of
 * the correct rotation to apply.
 */
public enum Rotation {
    /**
     * Gate outputs face east. This is also the default rotation.
     */
    EAST(0),
    /**
     * Gate outputs face south.
     */
    SOUTH(90),
    /**
     * Gate outputs face west.
     */
    WEST(180),
    /**
     * Gate outputs face north.
     */
    NORTH(-90);

    /**
     * Internal int value of the rotation.
     */
    private final int rotation;

    /**
     * Contructor when getting the enum.
     *
     * @param rotation The rotation value.
     */
    private Rotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * Getter for {@link #rotation}.
     *
     * @return The rotation value, between -179 to 180 included.
     */
    public int rotation() {
        return rotation;
    }
}
