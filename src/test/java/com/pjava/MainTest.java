package com.pjava;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.gates.AND;
import com.pjava.src.components.gates.Clock;
import com.pjava.src.components.gates.Ground;
import com.pjava.src.components.gates.NOT;
import com.pjava.src.components.gates.OR;
import com.pjava.src.components.gates.Power;
import com.pjava.src.utils.Cyclic;
import com.pjava.src.utils.Utils;

/**
 * This a test class. Nothing more, nothing less.
 */
public class MainTest {

    @Test
    void SRFlipFlop() {
        // Clock start at 0
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

        R.updateState();
        S.updateState();

        boolean q = not1.getState(0);
        boolean oldq = q;

        Cyclic cycle = new Cyclic();
        assertEquals(false, cycle.isCyclic(R), () -> "R is not expected to be in a cycle");
        assertEquals(true, cycle.isCyclic(or1), () -> "or1 is expected to be in a cycle");

        for (int i = 1; i <= 4; ++i) {
            q = (S.getState(0) || (!R.getState(0) && oldq));
            oldq = q;

            // to fix warnings somehow :/
            final boolean Q = q;
            final boolean oldQ = oldq;
            final int I = i;

            if (S.getState(0) && R.getState(0)) {
                assertTrue(!not1.getState(0) && !not2.getState(0),
                        () -> "[CHECK " + I + "] INVALID\n" +
                                "\tInputs: " +
                                " \tS: " + S.getState(0) +
                                " \tR: " + R.getState(0) +

                                "\n\tExpected:" +
                                " \tQ: " + false +
                                " \t!Q: " + false +

                                "\n\tResult: " +
                                " \tQ: " + not1.getState(0) +
                                " \t!Q: " + not2.getState(0));
            } else {
                assertTrue(not1.getState(0) == q && not2.getState(0) == !q,
                        () -> "[CHECK " + I + "]\n" +
                                "\tInputs: " +
                                " \tS: " + S.getState(0) +
                                " \tR: " + R.getState(0) +
                                " \tQ: " + oldQ +

                                "\n\tExpected:" +
                                " \tQ: " + Q +
                                " \t!Q: " + !Q +

                                "\n\tResult: " +
                                " \tQ: " + not1.getState(0) +
                                " \t!Q: " + not2.getState(0));
            }

            if (Utils.isEven(I)) {
                R.instantCycle();
            }
            if (Utils.isOdd(I)) {
                S.instantCycle();
            }
        }
    }

    @Test
    void basic() {
        Power p1 = new Power();
        // Ground p1 = new Ground();
        // Power p2 = new Power();
        Ground p2 = new Ground();

        boolean a = p1.getState(0);
        boolean b = p2.getState(0);

        AND and = new AND();
        NOT not = new NOT();
        and.connect(not);

        assertNotNull(p1.connect(and), () -> "Connection failed with p1");
        assertNotNull(p2.connect(and), () -> "Connection failed with p2");

        p1.updateState();
        p2.updateState();
        System.out.println("Expected: \t" + (a && b) + "\nResult: \t" + and.getState(0));
        System.out.println("Expected: \t" + !and.getState(0) + "\nResult: \t" + not.getState(0));

        assertTrue((a && b) == and.getState(0) && !and.getState(0) == not.getState(0));
    }
}
