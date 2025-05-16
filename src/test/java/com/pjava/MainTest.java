package com.pjava;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.gates.Or;
import com.pjava.src.components.gates.Schema;
import com.pjava.src.components.input.Clock;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;
import com.pjava.src.components.output.Display;
import com.pjava.src.utils.Cyclic;
import com.pjava.src.utils.Utils;

/**
 * This a test class. Nothing more, nothing less.
 */
public class MainTest {

    @Test
    void basic() throws Exception {
        Power p1 = new Power(2);
        Power p3 = new Power(2);
        Power p4 = new Power(2);
        Not n1 = new Not(2);
        // Ground p1 = new Ground();
        // Power p2 = new Power();
        Ground p2 = new Ground(2);

        And and = new And(2);
        Or or = new Or(2);
        Not not = new Not(2);

        Display display = new Display(2, 2);
        Display display2 = new Display(2, 2);

        and.connect(not);
        p1.connect(n1);
        p2.connect(and);
        n1.connect(and);

        ArrayList<Gate> gates = new ArrayList<Gate>();
        ArrayList<Gate> gates2 = new ArrayList<Gate>();

        gates.add(n1);
        gates.add(p2);
        gates.add(and);
        gates.add(not);

        not.connect(display);

        Schema created = new Schema("test", gates);

        p4.connect(created);
        created.connect(or);
        p3.connect(or);
        or.connect(display2);

        System.out.println(created.getInputNumber() + " " + created.getInputCable());

        gates2.add(created);
        gates2.add(p3);
        gates2.add(or);


        Schema created2 = new Schema("test2", gates2);
        created2.exportSchema();

        System.out.println(created.getGates());
        System.out.println(created2.getGates());
}

    @Test
    void SRFlipFlop() throws Exception {
        // Clock start at 0
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
}
