package com.pjava;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.gates.Not;
import com.pjava.src.components.gates.Or;
import com.pjava.src.components.input.Lever;
import com.pjava.src.utils.Cyclic;
import com.pjava.src.utils.Utils;

/**
 * This a test class. Nothing more, nothing less.
 */
public class MainTest {

    @Test
    void SRFlipFlop() throws Exception {
        // lever start at 0
        Lever R = new Lever();
        Lever S = new Lever();

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
                R.flip();
            }
            if (Utils.isOdd(I)) {
                S.flip();
            }
        }
    }

}
