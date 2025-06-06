package com.pjava.components;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.json.JSONObject;

import com.pjava.src.components.Gate;
import com.pjava.src.components.Circuit;
import com.pjava.src.components.gates.*;
import com.pjava.src.components.input.*;
import com.pjava.src.components.output.Display;
import com.pjava.src.components.output.Display;


// TODO : différent circuit pour verifier leur bonne creation et
//  si on obtient les bon résultat en sortie


public class CircuitTest {

    private String here = " /!\\=============/!\\ ";
    // here +
    // " + here +"
    // + here


    /**
     * We try '.toJson()' with a gate
     * We print the result
     */
    /*
    @Test
    void test1() {
        System.out.println("\ntest 1 : toJson for a gate");
        And and = new And();
        System.out.println(and.toJson().toString());
    }
    */

    /**
     * We try adding gates in a 'circuit' instance
     * Then we print the result
     */
    /*
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    void test2() {
        System.out.println("\ntest 2 : add gate and custom label");
        Circuit circuit = new Circuit();

        try {
            circuit.addNewGate("And");
            circuit.addNewGate("Or", "or1");

            Gate a = circuit.addNewGate("Not", "not1");
            Gate b = circuit.addGate(circuit.getAllGates().get("not1"), "not1_copy");

            Gate c = circuit.addNewGate("Not", "not2");
            Gate d = circuit.addGate(circuit.getAllGates().get("not2"));

            circuit.addNewGate("Not");
            circuit.addNewGate("Power");
            circuit.addNewGate("Ground");

            assertEquals(a, b);
            assertEquals(c, d);
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("circuit to JSON by hand :\nnuméro : clé : Gate");
        int j = 0;
        for (String i : circuit.getAllGates().keySet()) {
            System.out.println(String.format("%d : %s : %s", j, i, circuit.getAllGates().get(i).toJson()));
            j++;
        }

        System.out.println("\ncircuit to JSON with Circuit.toJSON :");
        System.out.println(circuit.toJson());
    }
    */

    /*
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    void test3() {

        System.out.println("\ntest 3 : save");
        Circuit circuit2 = new Circuit();
        Circuit circuit2_bis = new Circuit("bbonqjour");

        try {
            circuit2.addNewGate("And");
            circuit2.addNewGate("Or");
            circuit2.addNewGate("Not");
            circuit2.addNewGate("Power");
            circuit2.addNewGate("Ground");
            circuit2.addNewGate("Lever");
            circuit2.addNewGate("Button");
            circuit2.addNewGate("Clock");

            circuit2.save("save4", null);

            Circuit circuit = new Circuit("Bonjour");

            circuit.save(null);
            // Should do the same thing :
            circuit.save("", null);
            circuit.save("data", null);
            circuit.save("/data", null);
            circuit.save("data/", null);
            circuit.save("./data/", null);
            circuit.save("./data/Bonjour.json", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    */


    /*
    @Test
    void test4() {
        System.out.println("\n" + here +"test 4 : copy to himself default circuit" + here);
        System.out.println("\n" + here +"test 4 : copy to himself default circuit" + here);
        Circuit circuit = new Circuit("circuit_default");

        try {
            // all gates
            assertNotNull(circuit.addNewGate("Power", "p1"));
            assertNotNull(circuit.addNewGate("Ground", "g1"));
            assertNotNull(circuit.addNewGate("Not", "not1"));
            assertNotNull(circuit.addNewGate("And", "and1"));
            assertNotNull(circuit.addNewGate("And", "and2"));
            assertNotNull(circuit.addNewGate("Or", "or1"));
            assertNotNull(circuit.addNewGate("Lever", "lev1"));
            assertNotNull(circuit.addNewGate("Button", "but1"));
            assertNotNull(circuit.addNewGate("Numeric", "num1"));
            assertNotNull(circuit.addNewGate("Clock", "clo1"));
            assertNotNull(circuit.addNewGate("Display", "dis1"));
            assertNotNull(circuit.addNewGate("Display", "dis2"));
            assertNotNull(circuit.addNewGate("Display", "dis3"));
            assertNotNull(circuit.addNewGate("Display", "dis4"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split1"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split2"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split3"));
            assertNotNull(circuit.addNewGate("Splitter", "busSplit1"));
            assertNotNull(circuit.addNewGate("Merger", "busMerg1"));
            assertNotNull(circuit.addNewGate("Power", "p1"));
            assertNotNull(circuit.addNewGate("Ground", "g1"));
            assertNotNull(circuit.addNewGate("Not", "not1"));
            assertNotNull(circuit.addNewGate("And", "and1"));
            assertNotNull(circuit.addNewGate("And", "and2"));
            assertNotNull(circuit.addNewGate("Or", "or1"));
            assertNotNull(circuit.addNewGate("Lever", "lev1"));
            assertNotNull(circuit.addNewGate("Button", "but1"));
            assertNotNull(circuit.addNewGate("Numeric", "num1"));
            assertNotNull(circuit.addNewGate("Clock", "clo1"));
            assertNotNull(circuit.addNewGate("Display", "dis1"));
            assertNotNull(circuit.addNewGate("Display", "dis2"));
            assertNotNull(circuit.addNewGate("Display", "dis3"));
            assertNotNull(circuit.addNewGate("Display", "dis4"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split1"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split2"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split3"));
            assertNotNull(circuit.addNewGate("Splitter", "busSplit1"));
            assertNotNull(circuit.addNewGate("Merger", "busMerg1"));

            // connecting some
            assertNotNull(circuit.connectGate("p1", "split1", 0, 0));
            assertNotNull(circuit.connectGate("split1", "split2", 0, 0));
            assertNotNull(circuit.connectGate("split2", "split3", 0, 0));
            assertNotNull(circuit.connectGate("split1", "not1", 1, 0));
            assertNotNull(circuit.connectGate("not1", "and1", 0, 1));
            assertNotNull(circuit.connectGate("split2", "and1", 1, 0));
            assertNotNull(circuit.connectGate("split3", "dis1", 1, 0));
            assertNotNull(circuit.connectGate("and1", "busMerg1", 0, 0));
            assertNotNull(circuit.connectGate("g1", "busMerg1", 0, 1));
            assertNotNull(circuit.connectGate("busMerg1", "busSplit1", 0, 0));
            assertNotNull(circuit.connectGate("busSplit1", "dis2", 0, 0));
            assertNotNull(circuit.connectGate("busSplit1", "dis3", 1, 0));
            assertNotNull(circuit.connectGate("but1", "and2", 0, 0));
            assertNotNull(circuit.connectGate("clo1", "and2", 0, 1));
            assertNotNull(circuit.connectGate("and2", "dis4", 0, 0));

            System.out.println("dis 1 output : " + ((Display)circuit.getAllGates().get("dis1")).getOutput());



            // save
            circuit.save("./data/test/circuit_default.json");

            System.out.println("\nSelection :");
            System.out.println("\nSelection :");
            int j = 0;
            for (String i : circuit.getAllGates().keySet()) {
                System.out.println(
                        String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                        String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                j++;
            }

            circuit.addGatesFromJson(circuit.toJson());

            System.out.println("\nResult :");
            j = 0;
            for (String i : circuit.getAllGates().keySet()) {
                System.out.println(
                    String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                    String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                j++;
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }
    */


    /*
    @Test
    void test5() {
        System.out.println("\n" + here +"test 5 : copy to himself default circuit using a selection" + here);
        Circuit circuit = new Circuit("circuit_default");

        try {
            // all gates
            assertNotNull(circuit.addNewGate("Power", "p1"));
            assertNotNull(circuit.addNewGate("Ground", "g1"));
            assertNotNull(circuit.addNewGate("Not", "not1"));
            assertNotNull(circuit.addNewGate("And", "and1"));
            assertNotNull(circuit.addNewGate("And", "and2"));
            assertNotNull(circuit.addNewGate("Or", "or1"));
            assertNotNull(circuit.addNewGate("Lever", "lev1"));
            assertNotNull(circuit.addNewGate("Button", "but1"));
            assertNotNull(circuit.addNewGate("Numeric", "num1"));
            assertNotNull(circuit.addNewGate("Clock", "clo1"));
            assertNotNull(circuit.addNewGate("Display", "dis1"));
            assertNotNull(circuit.addNewGate("Display", "dis2"));
            assertNotNull(circuit.addNewGate("Display", "dis3"));
            assertNotNull(circuit.addNewGate("Display", "dis4"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split1"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split2"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split3"));
            assertNotNull(circuit.addNewGate("Splitter", "busSplit1"));
            assertNotNull(circuit.addNewGate("Merger", "busMerg1"));

            // connecting some
            assertNotNull(circuit.connectGate("lev1", "split1", 0, 0));
            assertNotNull(circuit.connectGate("split1", "split2", 0, 0));
            assertNotNull(circuit.connectGate("split2", "split3", 0, 0));
            assertNotNull(circuit.connectGate("split1", "not1", 1, 0));
            assertNotNull(circuit.connectGate("not1", "and1", 0, 1));
            assertNotNull(circuit.connectGate("split2", "and1", 1, 0));
            assertNotNull(circuit.connectGate("split3", "dis1", 1, 0));
            assertNotNull(circuit.connectGate("and1", "busMerg1", 0, 0));
            assertNotNull(circuit.connectGate("num1", "busMerg1", 0, 1));
            assertNotNull(circuit.connectGate("busMerg1", "busSplit1", 0, 0));
            assertNotNull(circuit.connectGate("busSplit1", "dis2", 0, 0));
            assertNotNull(circuit.connectGate("busSplit1", "dis3", 1, 0));
            assertNotNull(circuit.connectGate("but1", "and2", 0, 0));
            assertNotNull(circuit.connectGate("clo1", "and2", 0, 1));
            assertNotNull(circuit.connectGate("and2", "dis4", 0, 0));

            System.out.println("dis 1 output : " + ((Display)circuit.getAllGates().get("dis1")).getOutput());
            circuit.getLeverGates().get("lev1").flip();
            System.out.println("dis 1 output : " + ((Display)circuit.getAllGates().get("dis1")).getOutput());



            ArrayList<String> selectionId = new ArrayList<>();
            for(String key : circuit.getAllGates().keySet()){
                if(!circuit.getInputGates().containsKey(key) && !circuit.getOutputGates().containsKey(key))
                selectionId.add(key);
            }

            System.out.println("\nSelection (in circuit):");
            int j = 0;
            for (String i : selectionId) {
                System.out.println(
                        String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                j++;
            }

            System.out.println("\nSelection (json):" + circuit.selectGatesFromIdList(selectionId).toString(1));

            Circuit circSelection = new Circuit(circuit.selectGatesFromIdList(selectionId));
            System.out.println("\nSelection (in temp circuit):");
            j = 0;
            for (String i : circSelection.getAllGates().keySet()) {
                System.out.println(
                        String.format("%d : id = %d : key = %s : GateJSON = %s", j, circSelection.getAllGates().get(i).uuid(), i, circSelection.getAllGates().get(i).toJson()));
                j++;
            }

            circuit.addGatesFromJson(circuit.selectGatesFromIdList(selectionId));

            System.out.println("\nResult :");
            j = 0;
            for (String i : circuit.getAllGates().keySet()) {
                System.out.println(
                    String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                j++;
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }
     */


     /*

    @Test
    void test6() {
        System.out.println("\n" + here +"test 6 : Creating a schema from a selection" + here);
        Circuit circuit = new Circuit("circuit_default");

        try {
            // all gates
            assertNotNull(circuit.addNewGate("Power", "p1"));
            assertNotNull(circuit.addNewGate("Ground", "g1"));
            assertNotNull(circuit.addNewGate("Not", "not1"));
            assertNotNull(circuit.addNewGate("And", "and1"));
            assertNotNull(circuit.addNewGate("And", "and2"));
            assertNotNull(circuit.addNewGate("Or", "or1"));
            assertNotNull(circuit.addNewGate("Lever", "lev1"));
            assertNotNull(circuit.addNewGate("Button", "but1"));
            assertNotNull(circuit.addNewGate("Numeric", "num1"));
            assertNotNull(circuit.addNewGate("Clock", "clo1"));
            assertNotNull(circuit.addNewGate("Display", "dis1"));
            assertNotNull(circuit.addNewGate("Display", "dis2"));
            assertNotNull(circuit.addNewGate("Display", "dis3"));
            assertNotNull(circuit.addNewGate("Display", "dis4"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split1"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split2"));
            assertNotNull(circuit.addNewGate("NodeSplitter", "split3"));
            assertNotNull(circuit.addNewGate("Splitter", "busSplit1"));
            assertNotNull(circuit.addNewGate("Merger", "busMerg1"));

            // connecting some
            assertNotNull(circuit.connectGate("lev1", "split1", 0, 0));
            assertNotNull(circuit.connectGate("split1", "split2", 0, 0));
            assertNotNull(circuit.connectGate("split2", "split3", 0, 0));
            assertNotNull(circuit.connectGate("split1", "not1", 1, 0));
            assertNotNull(circuit.connectGate("not1", "and1", 0, 1));
            assertNotNull(circuit.connectGate("split2", "and1", 1, 0));
            assertNotNull(circuit.connectGate("split3", "dis1", 1, 0));
            assertNotNull(circuit.connectGate("and1", "busMerg1", 0, 0));
            assertNotNull(circuit.connectGate("num1", "busMerg1", 0, 1));
            assertNotNull(circuit.connectGate("busMerg1", "busSplit1", 0, 0));
            assertNotNull(circuit.connectGate("busSplit1", "dis2", 0, 0));
            assertNotNull(circuit.connectGate("busSplit1", "dis3", 1, 0));
            assertNotNull(circuit.connectGate("but1", "and2", 0, 0));
            assertNotNull(circuit.connectGate("clo1", "and2", 0, 1));
            assertNotNull(circuit.connectGate("and2", "dis4", 0, 0));



            ArrayList<String> selectionId = new ArrayList<>();
            selectionId.addAll(circuit.getAllGates().keySet());
            // for(String key : circuit.getAllGates().keySet()){
            //     if(!circuit.getInputGates().containsKey(key) && !circuit.getOutputGates().containsKey(key))
            //     selectionId.add(key);
            // }

            System.out.println("\nSelection (in circuit):");
            int j = 0;
            for (String i : selectionId) {
                System.out.println(
                        String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                j++;
            }

            // creating a schema in a new circuit:
            Circuit circuit2 = new Circuit();
System.err.println("----------");
            assertNotNull(circuit2.addNewGate("Schema", "sh1", circuit.selectGatesFromIdList(selectionId)));
            // assertNotNull(circuit2.addNewGate("power", "p1"));
            // assertNotNull(circuit2.addNewGate("Not", "not1"));
            // assertNotNull(circuit2.addNewGate("And", "and1"));

            System.out.println("\nResult :");
            j = 0;
            for (String i : ((Schema)circuit2.getAllGates().get("sh1")).getInnerCircuit().getAllGates().keySet()) {
                System.out.println(
                    String.format("%d : id = %d : key = %s : GateJSON = %s", j, circuit.getAllGates().get(i).uuid(), i, circuit.getAllGates().get(i).toJson()));
                j++;
            }

    //     } catch (Exception e) {
    //         System.err.println(e);
    //     }

    }
     */




    // @Test
    // void test4complet() {
    //     System.out.println("\ntest 4 complete : copy to himself complete circuit without schema");
    //     Circuit circuit3 = new Circuit("Circuit_test4complet");

    //     try {
    //         // all gates
    //         circuit3.addNewGate("Power", "p1", 4);
    //         circuit3.addNewGate("Ground", "g1", 2);
    //         circuit3.addNewGate("Not", "not1", 4);
    //         circuit3.addNewGate("And", "and1", 4);
    //         circuit3.addNewGate("Or", "or1", 3);
    //         circuit3.addNewGate("Lever", "lev1");
    //         circuit3.addNewGate("Button", "but1");
    //         ((Numeric)circuit3.addNewGate("Numeric", "num1", 4)).setInputBase(6);
    //         circuit3.addNewGate("Clock", "clo1");
    //         circuit3.addNewGate("Display", "dis1");
    //         circuit3.addNewGate("NodeSplitter", "cableSplit1");
    //         circuit3.addNewGate("Splitter", "busSplit1");
    //         circuit3.addNewGate("Merger", "busMerg1");

    //         // connecting some
    //         circuit3.connectGate("p1", "not1", 0, 0);

    //         // save
    //         circuit3.save();
    //         //circuit3.save("test", "sirkui_3");

    //         System.out.println("Selection :");
    //         int j = 0;
    //         for (String i : circuit3.getAllGates().keySet()) {
    //             System.out.println(
    //                     String.format("%d : key = %s : GateJSON = %s", j, i, circuit3.getAllGates().get(i).toJson()));
    //             j++;
    //         }

    //         circuit3.addGatesFromJson(circuit3.toJson());

    //         System.out.println("\nResult :");
    //         j = 0;
    //         for (String i : circuit3.getAllGates().keySet()) {
    //             System.out.println(
    //                     String.format("%d : key = %s : GateJSON = %s", j, i, circuit3.getAllGates().get(i).toJson()));
    //             j++;
    //         }

    //     } catch (Exception e) {
    //         System.err.println(e);
    //     }

    // }


    /*
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    void test5() {
        System.out.println("\ntest 5 : loading circuit files");
        Circuit circuit4 = new Circuit();
        try {
            circuit4.loadGatesFromFile("save4/circuit_1.json");
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("\nLoading 'save4/circuit_1.json'..");
        System.out.println("numéro : clé : Gate");
        int j = 0;
        for (String i : circuit4.getAllGates().keySet()) {
            System.out.println(String.format("%d : %s : %s", j, i, circuit4.getAllGates().get(i).toJson()));
            j++;
        }

        // from test 3 : [circuit2_bis] data/bbonqjour.json
        try {
            circuit4.loadGatesFromFile("bbonqjour");
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("\nLoading 'bbonqjour'..");
        System.out.println("numéro : clé : Gate");
        j = 0;
        for (String i : circuit4.getAllGates().keySet()) {
            System.out.println(String.format("%d : %s : %s", j, i, circuit4.getAllGates().get(i).toJson()));
            j++;
        }

        // from test 4 : [circuit3] data/test/sirkui_3
        try {
            circuit4.loadGatesFromFile("test/sirkui_3");
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("\nLoading 'test/sirkuit_3'..");
        System.out.println("numéro : clé : Gate");
        j = 0;
        for (String i : circuit4.getAllGates().keySet()) {
            System.out.println(String.format("%d : %s : %s", j, i, circuit4.getAllGates().get(i).toJson()));
            j++;
        }

    }
    */




}
