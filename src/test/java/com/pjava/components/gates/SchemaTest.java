package com.pjava.components.gates;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.Gate;
import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.gates.Or;
import com.pjava.src.components.gates.Schema;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;
import com.pjava.src.components.output.Display;

public class SchemaTest {

    @Test
    void basic() throws Exception {

        // TODO : update the test to fit the new schema implementation
        /*
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
        */
    }
}
