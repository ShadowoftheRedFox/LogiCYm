package com.pjava.src.components.cables;

import com.pjava.src.errors.BusSizeException;

/**
 * An extends of cable because they are a particular case of cables.
 * Represent the whole electrical component output. It's this output that will
 * appear on inception/custom components.
 */
public class Output extends Cable {
    /**
     * Create a new output with the given bus size.
     *
     * @param busSize The bus size of the output.
     * @throws BusSizeException Throw when the given size is equal or below 0, not a
     *                          power of 2, or greater than 32.
     * @see Cable
     */
    public Output(Integer busSize) throws BusSizeException {
        super(busSize);
    }
}