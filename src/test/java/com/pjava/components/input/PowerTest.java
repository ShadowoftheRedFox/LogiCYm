package com.pjava.components.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.input.Power;

public class PowerTest {
    @Test
    void state() {
        Power p = new Power();
        assertTrue(p.getState(0));
    }
}
