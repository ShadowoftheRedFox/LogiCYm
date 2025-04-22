package com.pjava.test;

import java.util.ArrayList;

import com.pjava.src.components.cables.Cable;
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

        Cable c1 = new Cable(1);
        Cable c2 = new Cable(1);

        ArrayList<Cable> cl_in = new ArrayList<Cable>();
        cl_in.add(c1);
        cl_in.add(c2);

        ArrayList<Cable> cl_out_1 = new ArrayList<Cable>();
        cl_out_1.add(c1);
        ArrayList<Cable> cl_out_2 = new ArrayList<Cable>();
        cl_out_2.add(c2);

        p1.setOutputCable(cl_out_1);
        p2.setOutputCable(cl_out_2);
        and.setInputCable(cl_in);

        c1.inputGate.add(p1);
        c1.outputGate.add(and);

        c2.inputGate.add(p2);
        c2.outputGate.add(and);

        p1.updateState();
        p2.updateState();
        System.out.println("Expected: \t" + (a && b) + "\nResult: \t" + and.getState().get(0));
    }
}
