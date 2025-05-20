package com.pjava.components.gates;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.gates.Or;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;

public class OrTest {
    @Test
    void or() throws Exception {
        Power power1 = new Power();
        Power power2 = new Power();
        Power power3 = new Power();
        Ground ground1 = new Ground();
        Ground ground2 = new Ground();
        Ground ground3 = new Ground();
        Or or1 = new Or();
        Or or2 = new Or();
        Or or3 = new Or();

        power1.connect(or1);
        power2.connect(or1);
        assertTrue(or1.getState(0));

        power3.connect(or2);
        ground1.connect(or2);
        assertTrue(or2.getState(0));

        ground2.connect(or3);
        ground3.connect(or3);
        assertFalse(or3.getState(0));
    }
}
