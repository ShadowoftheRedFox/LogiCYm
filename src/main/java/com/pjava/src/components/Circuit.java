package com.pjava.src.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.BitSet;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.Cyclic;

import com.pjava.src.components.gates.Or;
import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;

import com.pjava.src.components.input.Input;
import com.pjava.src.components.output.Output;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Circuit{

    //#region Attributes
    /**
    * List of all the gates of a circuit
    */
    //public ArrayList<Gate> allGates = new ArrayList<Gate>();
    private HashMap<String,Gate> allGates = new HashMap<String,Gate>();

    // /**
    // * List of all the input gates of a circuit
    // */
    // private ArrayList<Input> circuitInput = new ArrayList<Input>();

    // /**
    // * List of all the output gates of a circuit
    // */
    // private ArrayList<Output> circuitOutput = new ArrayList<Output>();

    //#endregion


    public Circuit(){
    }


    //#region Setter

    //#endregion


    //#region Getter
    public HashMap<String,Gate> get_allGates(){
        return this.allGates;
    }
    //#endregion


    /**
     *
     * @param type A string of the gate type to be created
     * @param label The key for this gate
     * @retrun The gate that has just been created
     */
    public Gate addGate(String type, String label) throws Error{

        Gate newGate;
        switch(type){
            case "Power":
            newGate = new Power();
            break;
            case "Ground":
            newGate = new Ground();
            break;
            case "And":
            newGate = new And();
            break;
            case "Not":
            newGate = new Not();
            break;
            case "Or":
            newGate = new Or();
            break;
            default:
                throw new Error(String.format("No match found for the string '%s'", type));
        }

        if(label == ""){
            label = newGate.uuid().toString();
        }

        if(allGates.containsKey(label)){
            throw new Error(String.format("This label is already taken : '%s' (creating gate : '%s')", label, type));
        }

        this.allGates.put(label,newGate);

        return newGate;
    }

    public Gate addGate(String type){
        return addGate(type, "");
    }



    // TODO : startSimulation
    public void startSimulation() {
    }


    public JSONObject toJson() {
        // adding gates within the circuit to a JSON array
        JSONArray gate_Array = new JSONArray();
        for(Gate gate : allGates.values()){
            gate_Array.put(gate.toJson());
        }

        // Creating the JSON object
        JSONObject circuit_Json = new JSONObject();
        circuit_Json.put("Gate",gate_Array);

        return circuit_Json;
    }


    public static void saveCircuit(Circuit circuit,String fileName){
        try (FileWriter writer = new FileWriter(fileName)){
            JSONObject circuit_Json = circuit.toJson();
            writer.write(circuit_Json.toString(1));
            System.out.println("Circuit save with succes in:" + fileName);

        }
        catch(IOException e){
            System.err.println("Error" + fileName +"can't be save" + e.getMessage());
        }
    }





    // public void load(JSONObject circuit_Json){
    //     try {
    //     Circuit circuit = new Circuit();
    //     JSONArray gate_Array = circuit_Json.getJSONArray("Gate");
    //     for (int i = 0; i< gate_Array.length(); i++){
    //         JSONObject gate_Json = gate_Array.getJSONObject(i);
    //         String type = gate_Json.getString("type");
    //         circuit.addGate(type);
    //         System.out.println("Circuit Load with succes");
    //     }
    //     } catch (JSONException e) {
    //         System.err.println("Error circuit can't be launch " + e.getMessage());
    //     }

    //     // 2 boucle, connection entre tous les gates
    // }

    // public void loadFromFile(String filePath){
    //     StringBuilder content = new StringBuilder();
    //     try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             content.append(line);
    //         }
    //         JSONObject circuit_Json = new JSONObject(content.toString());
    //         this.load(circuit_Json);
    //     }catch (IOException e){
    //         System.err.println("Error circuit can't be launch " + e.getMessage());
    //     }

    // }
}
