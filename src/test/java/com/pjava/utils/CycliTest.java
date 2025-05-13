package com.pjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.gates.Not;
import com.pjava.src.components.gates.Or;
import com.pjava.src.components.input.Clock;
import com.pjava.src.utils.Cyclic;

public class CycliTest {
    @Test
    void isCyclic() throws Exception {
        Clock R = new Clock();
        Clock S = new Clock();

        Or or1 = new Or();
        Or or2 = new Or();

        Not not1 = new Not();
        Not not2 = new Not();

        R.connect(or1);
        S.connect(or2);

        or1.connect(not1);
        or2.connect(not2);

        not1.connect(or2);
        not2.connect(or1);

        Cyclic cycle = new Cyclic();
        assertEquals(false, cycle.isCyclic(R),
                () -> "Cyclic.isCyclic(R) expected to be false, received true");
        int Rsize = cycle.getElementInCyle().size();
        assertEquals(0, Rsize,
                () -> "Cycle.getElementInCyle().size() after Cyclic.isCyclic(R) expected to be 0, received "
                        + Rsize);
        assertEquals(true, cycle.isCyclic(or1),
                () -> "Cyclic.isCyclic(R) expected to be true, received false");
        int Or1size = cycle.getElementInCyle().size();
        assertEquals(8, Or1size,
                () -> "Cycle.getElementInCyle().size() after Cyclic.isCyclic(R) expected to be 0, received "
                        + Or1size);
    }
}
