package com.pjava.components.gates;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.gates.And;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;

public class AndTest {
    @Test
    void and() throws Exception {
        Power power1 = new Power();
        Power power2 = new Power();
        Power power3 = new Power();
        Ground ground1 = new Ground();
        Ground ground2 = new Ground();
        Ground ground3 = new Ground();
        And and1 = new And();
        And and2 = new And();
        And and3 = new And();

        power1.connect(and1);
        power2.connect(and1);
        assertTrue(and1.getState(0));

        power3.connect(and2);
        ground1.connect(and2);
        assertFalse(and2.getState(0));

        ground2.connect(and3);
        ground3.connect(and3);
        assertFalse(and3.getState(0));
    }
}
