package com.pjava.src.components.cables;

import com.pjava.src.components.Cable;
import com.pjava.src.errors.BusSizeException;

/**
 * An extends of cable because they are a particular case of cables.
 * Represent the whole electrical component input. It's this input that will
 * appear on inception/custom components.
 */
public class Input extends Cable {
    /**
     * Create a new input with the given bus size.
     *
     * @param busSize The bus size of the input.
     * @throws BusSizeException Throw when the given size is equal or below 0, not a
     *                          power of 2, or greater than 32.
     * @see Cable
     */
    public Input(Integer busSize) throws BusSizeException {
        super(busSize);
    }
}
