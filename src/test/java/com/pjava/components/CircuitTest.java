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
        System.out.println("\ntest 1 : toJson for a gate");
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
        System.out.println("\ntest 2 : add gate and custom label");
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

        System.out.println("\ntest 3 : save");
        Circuit circuit2 = new Circuit();
        Circuit circuit2_bis = new Circuit("bbonqjour");

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

            Circuit circuit = new Circuit("Bonjour");

            circuit.save();
            // Should do the same thing :
            circuit.save("");
            circuit.save("data");
            circuit.save("/data");
            circuit.save("data/");
            circuit.save("./data/");
            circuit.save("", "Bonjour");
            circuit.save("", "Bonjour.json");

        }catch(Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    void test4(){
        System.out.println("\ntest 4 : copy to himself");
        Circuit circuit3 = new Circuit();

        try {
            circuit3.addNewGate("Power", "p1");
            circuit3.addNewGate("Ground", "g1");
            circuit3.addNewGate("Lever", "l1");
            circuit3.addNewGate("Not", "n1");

            circuit3.connectGate("p1","n1", 0, 0);

            circuit3.save("test", "sirkui_3");

            System.out.println("Selection :");
            int j = 0;
            for(String i : circuit3.get_allGates().keySet()){
                System.out.println(String.format("%d : key = %s : GateJSON = %s", j, i, circuit3.get_allGates().get(i).toJson()));
                j++;
            }

            circuit3.addGatesFromJson(circuit3.toJson());

            System.out.println("\nResult :");
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
        System.out.println("\ntest 5 : loading circuit files");
        Circuit circuit4 = new Circuit();
        try{
            circuit4.addGatesFromFile("save4/circuit_1.json");
        }
        catch(Exception e){
            System.err.println(e);
        }

        System.out.println("\nLoading 'save4/circuit_1.json'..");
        System.out.println("numéro : clé : Gate");
        int j = 0;
        for(String i : circuit4.get_allGates().keySet()){
            System.out.println(String.format("%d : %s : %s", j, i, circuit4.get_allGates().get(i).toJson()));
            j++;
        }


        // from test 3 : [circuit2_bis] data/bbonqjour.json
        try{
            circuit4.addGatesFromFile("bbonqjour");
        }
        catch(Exception e){
            System.err.println(e);
        }

        System.out.println("\nLoading 'bbonqjour'..");
        System.out.println("numéro : clé : Gate");
        j = 0;
        for(String i : circuit4.get_allGates().keySet()){
            System.out.println(String.format("%d : %s : %s", j, i, circuit4.get_allGates().get(i).toJson()));
            j++;
        }


        // from test 4 : [circuit3] data/test/sirkui_3
        try{
            circuit4.addGatesFromFile("test/sirkui_3");
        }
        catch(Exception e){
            System.err.println(e);
        }

        System.out.println("\nLoading 'test/sirkuit_3'..");
        System.out.println("numéro : clé : Gate");
        j = 0;
        for(String i : circuit4.get_allGates().keySet()){
            System.out.println(String.format("%d : %s : %s", j, i, circuit4.get_allGates().get(i).toJson()));
            j++;
        }

    }

}
