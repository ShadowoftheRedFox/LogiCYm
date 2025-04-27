package com.pjava.test;

import com.pjava.src.components.gates.AND;
import com.pjava.src.components.gates.Clock;
import com.pjava.src.components.gates.NOT;
import com.pjava.src.components.input_output.Ground;
import com.pjava.src.components.input_output.Power;
import com.pjava.src.utils.Utils;

/**
 * This a test class. Nothing more, nothing less.
 */
public class Test {
    /**
     * Create a new Test, and execute the {@link #run()} function.
     */
    public Test() {
        run();
    }

    /**
     * The main entry point of the class. Launched when contructed.
     */
    public void run() {
        flipFlop();
        // basic();
    }

    /**
     * Run a SR flip flop and look for the correct result.
     */
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
        not2.connect(and1);

        boolean q = not1.getState().get(0);

        S.updateState();
        R.updateState();

        for (int i = 0; i < 5; i++) {
            q = (S.getState().get(0) || (!R.getState().get(0) && q));

            if (!S.getState().get(0) && !R.getState().get(0)) {
                System.out.println("[CHECK " + i + "]\n" +
                        "\tInputs: " +
                        " \tS: " + S.getState().get(0) +
                        " \tR: " + R.getState().get(0) +

                        "\n\tExpected:" +
                        " \tQ: " + true +
                        " \t!Q: " + true +

                        "\n\tResult: " +
                        " \tQ: " + not1.getState().get(0) +
                        " \t!Q: " + not2.getState().get(0));
            } else {
                System.out.println("[CHECK " + i + "]\n" +
                        "\tInputs: " +
                        " \tS: " + S.getState().get(0) +
                        " \tR: " + R.getState().get(0) +

                        "\n\tExpected:" +
                        " \tQ: " + q +
                        " \t!Q: " + !q +

                        "\n\tResult: " +
                        " \tQ: " + not1.getState().get(0) +
                        " \t!Q: " + not2.getState().get(0));
            }

            if (Utils.isEven(i)) {
                R.instantCycle();
            }
            if (Utils.isOdd(i)) {
                S.instantCycle();
            }
        }
    }

    /**
     * Run a basic AND gate and look for the correct result.
     */
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
