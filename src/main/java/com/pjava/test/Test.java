package com.pjava.test;

import com.pjava.src.components.cables.Cable;
import com.pjava.src.components.gates.AND;
import com.pjava.src.components.gates.Clock;
import com.pjava.src.components.gates.NOT;
import com.pjava.src.components.input_output.Ground;
import com.pjava.src.components.input_output.Power;

public class Test {
    public void run() {
        flipFlop();
        // basic();
    }

    public void flipFlop() {
        // Clock start at 0
        Clock S = new Clock();
        Clock R = new Clock();

        AND and1 = new AND();
        NOT not1 = new NOT();

        AND and2 = new AND();
        NOT not2 = new NOT();

        and1.connect(not1);
        and2.connect(not2);

        S.connect(and1);
        R.connect(and2);

        not1.connect(and2);
        Cable c5 = not2.connect(and1);
        c5.updateState(false);

        boolean q = false;

        S.updateState();
        R.updateState();

        S.instantCycle();
        System.out.println("not2: " + c5.getInputGate().get(0).getPowered() + "\n and1: "
                + c5.getOutputGate().get(0).getPowered());

        for (int i = 0; i < 2; i++) {
            q = (S.getState().get(0) || (!R.getState().get(0) && not1.getState().get(0)));

            if (!S.getState().get(0) && !R.getState().get(0)) {
                System.out.println("FlipFlop invalid (any result)");
            } else {
                System.out.println("Expected:" +
                        " Q: " + q +
                        " !Q: " + !q +
                        "\nResult: " +
                        " Q: " + not1.getState().get(0) +
                        " !Q: " + not2.getState().get(0));
            }

            if (i == 0) {
                R.instantCycle();
            }
            S.updateState();
            R.updateState();
        }
    }

    public void basic() {
        Power p1 = new Power();
        // Ground p1 = new Ground();
        // Power p2 = new Power();
        Ground p2 = new Ground();

        boolean a = p1.getState().get(0);
        boolean b = p2.getState().get(0);

        AND and = new AND();

        if (p1.connect(and) == null) {
            System.out.println("Connection failed with p1");
        }
        if (p2.connect(and) == null) {
            System.out.println("Connection failed with p2");
        }

        p1.updateState();
        p2.updateState();
        System.out.println("Expected: \t" + (a && b) + "\nResult: \t" + and.getState().get(0));
    }
}
