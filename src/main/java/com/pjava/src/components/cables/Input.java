package com.pjava.src.components.cables;

/**
 * An extends of cable because they are a particular case of cables.
 * Otherwise, it's in the Input/Output category.
 */
public class Input extends Cable {
    public Input(Integer busSize) {
        super(busSize);
    }
}
