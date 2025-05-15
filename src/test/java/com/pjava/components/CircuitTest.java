package com.pjava.components;

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
     *  On test toJson() avec un gate
     *  On print le résultat
     */
    @Test
    void test1(){
        System.out.println("\ntest 1 :");
        And and = new And();
        System.out.println(and.toJson().toString());
    }

    @Test
    void test2(){
        System.out.println("\ntest 2 :");
        Circuit circuit = new Circuit();


        circuit.addGate("And");
        circuit.addGate("Or");
        circuit.addGate("Not");
        circuit.addGate("Power");
        circuit.addGate("Ground");

        int j = 0;
        for(Gate i : circuit.listGate){
            System.out.println(String.format("%d : %s", j, i.toJson().toString()));
            j++;
        }

    }

}
