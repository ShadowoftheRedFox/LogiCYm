package com.pjava.components.input;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.input.Ground;

public class GroundTest {
    @Test
    void state() {
        Ground p = new Ground();
        assertFalse(p.getState(0));
    }
}
