package com.pjava.components;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.pjava.src.components.*;
import com.pjava.src.components.Circuit;
import com.pjava.src.components.gates.*;
import com.pjava.src.utils.*;
public class CircuitTest{

    /**
     *  We try '.toJson()' with a gate
     *  We print the result
     */
    @Test
    void test1(){
        System.out.println("\ntest 1 :");
        And and = new And();
        System.out.println(and.toJson().toString());
    }

    /**
     *  We try adding gates in a 'circuit' instance
     *  Then we print the result
     */
    @Test
    void test2(){
        System.out.println("\ntest 2 :");
        Circuit circuit = new Circuit();


        circuit.addGate("And");
        circuit.addGate("Or", "or1");
        circuit.addGate("Not", "not1");
        circuit.addGate("Not", "not2");
        circuit.addGate("Not", "not3");
        circuit.addGate("Not");
        circuit.addGate("Power");
        circuit.addGate("Ground");


        System.out.println("circuit to JSON by hand :");
        int j = 0;
        for(String i : circuit.get_allGates().keySet()){
            System.out.println(String.format("%d : %s : %s", j, i, circuit.get_allGates().get(i).toJson()));
            j++;
        }

        System.out.println("\ncircuit to JSON with Circuit.toJSON :");
        System.out.println(circuit.toJson().toString(1));

    }

    @Test
    void test3(){
        System.out.println("\ntest 3 :");
        Circuit circuit2 = new Circuit();

        circuit2.addGate("And");
        circuit2.addGate("Or");
        circuit2.addGate("Not");
        circuit2.addGate("Power");
        circuit2.addGate("Ground");

        Circuit.saveCircuit(circuit2,"save/save1");
    }

}
