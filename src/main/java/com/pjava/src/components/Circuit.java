package com.pjava.src.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.gates.Or;
import com.pjava.src.components.input.Button;
import com.pjava.src.components.input.Clock;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Lever;
import com.pjava.src.components.input.Power;


// TODO : startSimulation()


public class Circuit{

    //#region Attributes

    /**
    * List of all the gates of a circuit
    */
    //public ArrayList<Gate> allGates = new ArrayList<Gate>();
    private HashMap<String,Gate> allGates = new HashMap<>();

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


    //#region Getter

    public HashMap<String,Gate> get_allGates(){
        return this.allGates;
    }

    //#endregion


    //#region .addGate()

    /**
     * Add a gate to the circuit
     *
     * @param gate
     * @param label
     * @return The gate added
     * @throws Exception
     */
    public Gate addGate(Gate gate, String label) throws Exception{

        if(label.equals("")){
            throw new Exception(String.format("label can't be empty"));
        }

        if(allGates.containsKey(label)){
            throw new Exception(String.format("This label is already taken : '%s'", label));
        }

        this.allGates.put(label,gate);

        return gate;
    }

    /**
     * Set the label as Gate.uuid
     *
     * @param gate
     * @return The gate added
     * @throws Exception
     */
    public Gate addGate(Gate gate) throws Exception{
        return addGate(gate, gate.uuid().toString());
    }

    //#endregion

    //#region .addNewGate()

    /**
     * create a gate and add it to the circuit using {@code addGate()}
     *
     * @param type A string of the gate type to be created
     * @param label The key for this gate
     * @return The gate that has just been created
     */
    public Gate addNewGate(String type, String label) throws Exception{

        Gate newGate;
        switch(type){
            case "Power":
            newGate = new Power();
            break;
            case "Ground":
            newGate = new Ground();
            break;
            case "Not":
            newGate = new Not();
            break;
            case "And":
            newGate = new And();
            break;
            case "Or":
            newGate = new Or();
            break;
            case "Lever":
            newGate = new Lever();
            break;
            case "Button":
            newGate = new Button();
            break;
            case "Clock":
            newGate = new Clock();
            break;
            default:
                throw new Exception(String.format("No match found for the string '%s'", type));
        }

        if(label.equals("")){
            this.addGate(newGate);
        }
        else{
            this.addGate(newGate, label);
        }

        return newGate;
    }

    /**
     * @param type
     * @return The gate created
     * @throws Exception
     */
    public Gate addNewGate(String type) throws Exception{
        return addNewGate(type, "");
    }

    //#endregion

    //#region .connectGate()

    /**
     * @param fromGate The gate whose output port you want to connect
     * @param toGate The gate whose input port you want to connect
     * @param fromPort The index of the output port
     * @param toPort The index of the input port
     * @return The cable created
     */
    public Cable connectGate(String fromGate, String toGate, int fromPort, int toPort) throws Exception{
        if(this.allGates.containsKey(fromGate)){
            throw new Exception(String.format("Key not found : %s", fromGate));
        }
        if(this.allGates.containsKey(toGate)){
            throw new Exception(String.format("Key not found : %s", toGate));
        }

        Cable res = null;

        try{
            res = this.allGates.get(fromGate).connect(this.allGates.get(toGate), fromPort, toPort);
        }
        catch(Exception e){
            System.err.println(e);
        }

        return res;
    }

    //#endregion

    //#region .toJson()

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

    //#endregion

    //#region .save()

    public void save(String fileName) throws Error{

        fileName = "save/" + fileName;

        try(FileWriter writer = new FileWriter(fileName)){
            JSONObject circuit_Json = this.toJson();
            writer.write(circuit_Json.toString(1));
            System.out.println("Circuit save with succes in:" + fileName);
        }
        catch(IOException e){
            System.err.println("Error " + fileName +" can't be saved : " + e.getMessage());
        }
    }

    //#endregion

    //#region .load()

    public void load(JSONObject circuit_Json) throws Exception{
        Circuit tempCircuit = new Circuit();

        try {
            JSONArray gate_Array = circuit_Json.getJSONArray("Gate");

            // 1 : We create new gates from those we find in the json Object and we set their old uuid as a label
            for (int i = 0; i < gate_Array.length(); i++){
                JSONObject gate_Json = gate_Array.getJSONObject(i);

                String type = gate_Json.getString("type");
                String oldId = String.valueOf(gate_Json.getInt("uuid"));
                tempCircuit.addNewGate(type, oldId);
            }

            // 2 : Once all the gate are created, we connect them thanks to their old uuid
            // Though, cables will now use their new 'uuid'
            for (int i = 0; i < gate_Array.length(); i++){
                JSONObject gate_Json = gate_Array.getJSONObject(i);

                String baseOldId = String.valueOf(gate_Json.getInt("uuid"));

                JSONArray output_Array = gate_Json.getJSONArray("outputTo");
                for(int baseOutputIndex = 0; baseOutputIndex < output_Array.length(); baseOutputIndex++){
                    String targetOldId = String.valueOf(output_Array.getJSONArray(baseOutputIndex).getInt(0));
                    int targetInputIndex = output_Array.getJSONArray(baseOutputIndex).getInt(1);

                    tempCircuit.connectGate(baseOldId, targetOldId, baseOutputIndex, targetInputIndex);
                }
            }

            // 3 : We fuse the temporary ciruit with the main one
            for(Gate gate : tempCircuit.allGates.values()){
                this.addGate(gate);
            }

            // print the result for test purpose
            /*
            System.out.println("Circuit Loaded with succes:");
            int j = 0;
            for(String i : tempCircuit.get_allGates().keySet()){
                System.out.println(String.format("%d : key = %s : GateJSON = %s", j, i, tempCircuit.get_allGates().get(i).toJson()));
                j++;
            }
            */

        } catch (JSONException e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    //#endregion

    //#region .loadFromFile()

    public void loadFromFile(String filePath) throws Exception{
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            JSONObject circuit_Json = new JSONObject(content.toString());
            this.load(circuit_Json);
        }catch (IOException e){
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }

    }

    //#endregion

}
