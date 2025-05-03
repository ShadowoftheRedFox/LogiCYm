package com.pjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.gates.Clock;
import com.pjava.src.components.gates.NOT;
import com.pjava.src.components.gates.OR;
import com.pjava.src.utils.Cyclic;

public class CycliTest {
    @Test
    void isCyclic() {
        Clock R = new Clock();
        Clock S = new Clock();

        OR or1 = new OR();
        OR or2 = new OR();

        NOT not1 = new NOT();
        NOT not2 = new NOT();

        R.connect(or1);
        S.connect(or2);

        or1.connect(not1);
        or2.connect(not2);

        not1.connect(or2);
        not2.connect(or1);

        Cyclic cycle = new Cyclic();
        assertEquals(false, cycle.isCyclic(R),
                () -> "Cyclic.isCyclic(R) expected to be false, received true");
        int Rsize = cycle.getComponentInCyle().size();
        assertEquals(0, Rsize,
                () -> "Cycle.getComponentInCyle().size() after Cyclic.isCyclic(R) expected to be 0, received " + Rsize);
        assertEquals(true, cycle.isCyclic(or1),
                () -> "Cyclic.isCyclic(R) expected to be true, received false");
        int Or1size = cycle.getComponentInCyle().size();
        assertEquals(8, Or1size,
                () -> "Cycle.getComponentInCyle().size() after Cyclic.isCyclic(R) expected to be 0, received "
                        + Or1size);
    }
}
