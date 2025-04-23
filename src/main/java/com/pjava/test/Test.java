package com.pjava.test;

import com.pjava.src.components.gates.AND;
import com.pjava.src.components.input_output.Ground;
import com.pjava.src.components.input_output.Power;

public class Test {
    public static void run() {
        Power p1 = new Power();
        boolean a = true;

        // Power p2 = new Power();
        // boolean b = true;

        // Ground p1 = new Ground();
        // boolean a = false;

        Ground p2 = new Ground();
        boolean b = false;

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
