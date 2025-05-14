package com.pjava.src.UI;

public enum Rotation {
    EAST(0),
    SOUTH(90),
    WEST(180),
    NORTH(-90);

    private final int rotation;

    private Rotation(int rotation) {
        this.rotation = rotation;
    }

    public int rotation() {
        return rotation;
    }
}
