package com.pjava.components.input;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.input.Lever;

public class LeverTest {
    @Test
    void flip() {
        Lever lever = new Lever();
        assertFalse(lever.getState(0));
        lever.flip();
        assertTrue(lever.getState(0));
    }
}
