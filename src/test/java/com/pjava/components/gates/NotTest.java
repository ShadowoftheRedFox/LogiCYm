package com.pjava.components.gates;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.input.Ground;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.input.Power;

public class NotTest {
    @Test
    void getState() throws NullPointerException, Exception {
        Power power = new Power();
        Not notFalse = new Not();
        power.connect(notFalse);

        assertFalse(notFalse.getState(0));

        Ground ground = new Ground();
        Not notTrue = new Not();
        ground.connect(notTrue);

        assertTrue(notTrue.getState(0));
    }
}
