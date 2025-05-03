package com.pjava.test;

import java.util.BitSet;
import java.util.Objects;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.components.gates.AND;
import com.pjava.src.components.gates.Clock;
import com.pjava.src.components.gates.NOT;
import com.pjava.src.components.gates.OR;
import com.pjava.src.components.input_output.Ground;
import com.pjava.src.components.input_output.Power;
import com.pjava.src.utils.Cyclic;
import com.pjava.src.utils.Utils;

/**
 * This a test class. Nothing more, nothing less.
 * TODO replace this class with unit test.
 */
public class Test {
    /**
     * Create a new Test, and execute the {@link #run()} function.
     */
    public Test() {
        try {
            preflight();
        } catch (Exception e) {
            System.out.println("Preflight failed: " + e.getMessage());
            e.printStackTrace();
            setResume(false);
        } finally {
            if (getResume()) {
                System.out.println("==============================\n"
                        + "|      [ PREFLIGHT OK ]      |\n"
                        + "==============================");
            } else {
                System.out.println("==============================\n"
                        + "|    [ PREFLIGHT NOT OK ]    |\n"
                        + "==============================");
            }
        }
        if (getResume()) {
            run();
        }
    }

    /**
     * This check all class and check main features.
     * If any fails, return false with a log in the console.
     *
     * @return True if test succeeded, false otherwise.
     */
    private boolean preflight() {
        class G extends Gate {
            @Override
            public BitSet getState() {
                return null;
            }
        }

        G g = new G();
        if (Objects.equals(g, g) != true) {
            System.out.println("Error with Objects.equals Gate");
            return false;
        }
        if (g.equals(g) != true) {
            System.out.println("Error with Gate.equals(Gate)");
            return false;
        }
        if (g.hashCode() != g.hashCode()) {
            System.out.println("Error with hashCode Gate");
            return false;
        }

        AND a = new AND();
        NOT n = new NOT();

        if (Objects.equals(a, n) ||
                a.equals(n) || n.equals(a) ||
                n.hashCode() == a.hashCode()) {
            System.out.println("Error with euqals between different subtypes of Gate");
            return false;
        }

        Cyclic cycle = new Cyclic();
        if (cycle.isCyclic(null) != false) {
            System.out.println("Error with null cycle");
            return false;
        }

        if (cycle.isCyclic(g) != false) {
            System.out.println("Error with self cycle");
            return false;
        }

        Cable c = n.connect(a);
        if (c == null) {
            System.out.println("Error with connect: cable is null");
            return false;
        }

        a.connect(n);
        if (cycle.isCyclic(n) != true) {
            System.out.println("Error with cycle detection");
            return false;
        }

        return true;
    }

    /**
     * The main entry point of the class. Launched when contructed.
     */
    public void run() {
        basic();
        flipFlop();
    }

    /**
     * Run a SR flip flop and look for the correct result.
     */
    public void flipFlop() {
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
        if (cycle.isCyclic(R)) {
            System.out.println("Is R in a cycle? Expected: false -> true");
        }
        if (!cycle.isCyclic(or1)) {
            System.out.println("Is or1 in a cycle? Expected: true -> " + cycle.isCyclic(or1));
        }

        boolean result = true;
        for (int i = 1; i <= 4; i++) {
            q = (S.getState(0) ||
                    (!R.getState(0) && oldq));
            oldq = q;

            if (S.getState(0) && R.getState(0)) {
                System.out.println("[CHECK " + i + "] INVALID\n" +
                        "\tInputs: " +
                        " \tS: " + S.getState(0) +
                        " \tR: " + R.getState(0) +

                        "\n\tExpected:" +
                        " \tQ: " + false +
                        " \t!Q: " + false +

                        "\n\tResult: " +
                        " \tQ: " + not1.getState(0) +
                        " \t!Q: " + not2.getState(0));
                result = result && (!not1.getState(0) && !not2.getState(0));
            } else {
                System.out.println("[CHECK " + i + "]\n" +
                        "\tInputs: " +
                        " \tS: " + S.getState(0) +
                        " \tR: " + R.getState(0) +
                        " \tQ: " + oldq +

                        "\n\tExpected:" +
                        " \tQ: " + q +
                        " \t!Q: " + !q +

                        "\n\tResult: " +
                        " \tQ: " + not1.getState(0) +
                        " \t!Q: " + not2.getState(0));
                result = result && (not1.getState(0) == q && not2.getState(0) == !q);
            }

            if (Utils.isEven(i)) {
                R.instantCycle();
            }
            if (Utils.isOdd(i)) {
                S.instantCycle();
            }
        }

        setResume(result);
        if (result) {
            System.out.println("\n\t]======SR Flip Flop Success======[\n");
        } else {
            System.out.println("\n\t]======SR Flip Flop Failure======[\n");
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

        boolean a = p1.getState(0);
        boolean b = p2.getState(0);

        AND and = new AND();
        NOT not = new NOT();
        and.connect(not);

        if (p1.connect(and) == null) {
            System.out.println("Connection failed with p1");
        }
        if (p2.connect(and) == null) {
            System.out.println("Connection failed with p2");
        }

        p1.updateState();
        p2.updateState();
        System.out.println("Expected: \t" + (a && b) + "\nResult: \t" + and.getState(0));
        System.out.println("Expected: \t" + !and.getState(0) + "\nResult: \t" + not.getState(0));

        boolean result = (a && b) == and.getState(0) && !and.getState(0) == not.getState(0);
        setResume(result);
        if (result) {
            System.out.println("\n\t]======Basic Success======[\n");
        } else {
            System.out.println("\n\t]======Basic Failure======[\n");
        }
    }

    /**
     * Tells others via {@link #getResume()} if the test succeeded and application
     * should resume.
     */
    private boolean resume = true;

    /**
     * Tell if normal application can resume after the test.
     *
     * @return True if application should continue, false otherwise.
     */
    public boolean getResume() {
        return resume;
    }

    /**
     * Set if the application should resume after the end of the run.
     *
     * @param resume True if application should continue, false otherwise.
     */
    private void setResume(boolean resume) {
        this.resume = resume;
    }
}
