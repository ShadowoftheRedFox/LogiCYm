package com.pjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.Cable;
import com.pjava.src.components.cables.NodeSplitter;
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

        NodeSplitter outSplitter = new NodeSplitter();
        Not notOut = new Not();

        R.connect(or1);
        S.connect(or2);

        or1.connect(not1);
        or2.connect(not2);

        not1.connect(outSplitter);
        outSplitter.connect(or2);
        not2.connect(or1);

        Cable out = outSplitter.connect(notOut);

        Cyclic cycle = new Cyclic();
        assertEquals(false, cycle.isCyclic(R),
                () -> "Cyclic.isCyclic(R) expected to be false, received true");
        final int Rsize = cycle.getElementInCyle().size();
        assertEquals(0, Rsize,
                () -> "Cycle.getElementInCyle().size() after Cyclic.isCyclic(R) expected to be 0, received "
                        + Rsize);
        assertEquals(true, cycle.isCyclic(or1),
                () -> "Cyclic.isCyclic(R) expected to be true, received false");
        final int Or1size = cycle.getElementInCyle().size();
        assertEquals(10, Or1size,
                () -> "Cycle.getElementInCyle().size() after Cyclic.isCyclic(R) expected to be 8, received "
                        + Or1size);
        final int Or1InputSize = cycle.getCycleInput().size();
        assertEquals(2, Or1InputSize,
                () -> "Cycle.getCycleInput().size() after Cyclic.isCyclic(R) expected to be 2, received "
                        + Or1InputSize);
        final int Or1OutputSize = cycle.getCycleOutput().size();
        assertEquals(1, Or1OutputSize,
                () -> "Cycle.getCycleOutput().size() after Cyclic.isCyclic(R) expected to be 1, received "
                        + cycle.getCycleOutput() + " " + out);
        assertEquals(out, cycle.getCycleOutput().get(0),
                () -> "Cycle.getCycleOutput().get(0) after Cyclic.isCyclic(R) expected to be Cable out, received "
                        + cycle.getCycleOutput().get(0));
    }

    @Test
    void isUnstable() throws Exception {
        class TestNot extends Not {
            @Override
            public void setPowered(boolean powered) {
                super.setPowered(powered);
            }
        }
        TestNot not1 = new TestNot();
        TestNot not2 = new TestNot();

        not1.connect(not2);
        not2.connect(not1);

        not1.setPowered(true);
        not2.setPowered(true);

        Cyclic cycle = new Cyclic();

        assertNull(cycle.getUnstable());
        assertTrue(cycle.isCyclic(not1));
        assertEquals(true, cycle.getUnstable());
    }
}
