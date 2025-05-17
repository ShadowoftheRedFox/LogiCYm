package com.pjava.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;

import com.pjava.src.components.Gate;
import com.pjava.src.components.Circuit;
import com.pjava.src.components.gates.*;
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
    @SuppressWarnings("CallToPrintStackTrace")
    void test2(){
        System.out.println("\ntest 2 :");
        Circuit circuit = new Circuit();

        try {
            circuit.addNewGate("And");
            circuit.addNewGate("Or", "or1");

            Gate a = circuit.addNewGate("Not", "not1");
            Gate b = circuit.addGate(circuit.get_allGates().get("not1"),"not1_copy");

            Gate c = circuit.addNewGate("Not", "not2");
            Gate d = circuit.addGate(circuit.get_allGates().get("not2"));

            circuit.addNewGate("Not");
            circuit.addNewGate("Power");
            circuit.addNewGate("Ground");

            assertEquals(a, b);
            assertEquals(c, d);
        } catch (Exception e){
            System.err.println(e);
        }



        System.out.println("circuit to JSON by hand :\nnuméro : clé : Gate");
        int j = 0;
        for(String i : circuit.get_allGates().keySet()){
            System.out.println(String.format("%d : %s : %s", j, i, circuit.get_allGates().get(i).toJson()));
            j++;
        }

        System.out.println("\ncircuit to JSON with Circuit.toJSON :");
        System.out.println(circuit.toJson());
    }

    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    void test3(){
        System.out.println("\ntest 3 :");
        Circuit circuit2 = new Circuit();

        try{
            circuit2.addNewGate("And");
            circuit2.addNewGate("Or");
            circuit2.addNewGate("Not");
            circuit2.addNewGate("Power");
            circuit2.addNewGate("Ground");
            circuit2.addNewGate("Lever");
            circuit2.addNewGate("Button");
            circuit2.addNewGate("Clock");

            circuit2.save("save4");
        }catch(Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    void test4(){
        System.out.println("\ntest 4 :");
        Circuit circuit3 = new Circuit();

        try {
            circuit3.addNewGate("Power", "p1");
            circuit3.addNewGate("Ground", "g1");
            circuit3.addNewGate("Lever", "l1");
            circuit3.addNewGate("Not", "n1");

            int j = 0;
            for(String i : circuit3.get_allGates().keySet()){
                System.out.println(String.format("%d : key = %s : GateJSON = %s", j, i, circuit3.get_allGates().get(i).toJson()));
                j++;
            }

            circuit3.connectGate("p1","n1", 0, 0);

            System.out.println("Expected :");

            j = 0;
            for(String i : circuit3.get_allGates().keySet()){
                System.out.println(String.format("%d : key = %s : GateJSON = %s", j, i, circuit3.get_allGates().get(i).toJson()));
                j++;
            }

            System.out.println("\nResult :");
            circuit3.load(circuit3.toJson());
            j = 0;
            for(String i : circuit3.get_allGates().keySet()){
                System.out.println(String.format("%d : key = %s : GateJSON = %s", j, i, circuit3.get_allGates().get(i).toJson()));
                j++;
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }


    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    void test5(){
        System.out.println("\ntest 5 :");
        Circuit circuit4 = new Circuit();
        try{
            circuit4.loadFromFile("save/save3");
        }
        catch(Exception e){
            System.err.println(e);
        }

        int j = 0;
        for(String i : circuit4.get_allGates().keySet()){
            System.out.println(String.format("%d : %s : %s", j, i, circuit4.get_allGates().get(i).toJson()));
            j++;
        }

    }

}
