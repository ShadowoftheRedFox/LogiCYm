package com.pjava.src.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.gates.Or;
import com.pjava.src.components.gates.Schema;
import com.pjava.src.components.input.Button;
import com.pjava.src.components.input.Clock;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Input;
import com.pjava.src.components.input.Lever;
import com.pjava.src.components.input.Power;
import com.pjava.src.components.output.Output;





public class Circuit{

    //#region Attributes

        /**
         * Name of the circuit.
         * Will be save under the name "NAME-circuit.json".
         */
        private String name;

        /**
         * class variable that incrément for each new circuit created
         */
        private static int nbCircuit = 0;

        /**
        * Map of all the gates of a circuit
        * Key : uuid or custom label
        * Value : Gate
        */
        private HashMap<String,Gate> allGates = new HashMap<>();

        /**
        * List of all the input gates of a circuit
        */
        private HashMap<String,Input> inputGates = new HashMap<>();

        /**
        * List of all the output gates of a circuit
        */
        private HashMap<String,Output> outputGates = new HashMap<>();

    //#endregion

    public Circuit(){
        this(String.format("circuit_%d", nbCircuit));
    }

    public Circuit(String name){
        this.set_name(name);
        nbCircuit++;
    }

    //#region Setter

    public void set_name(String name) {
        if (name == null || name.isBlank()) {
            name = "Nom_Genere_Automatiquement_ahah";
        }
        this.name = name;
    }

    //#endregion

    //#region Getter

    public String get_name() {
        return this.name;
    }

    public HashMap<String,Gate> get_allGates(){
        return this.allGates;
    }

    //#endregion

    //#region .setSchemaInputPort

        public void setSchemaInput(String inputGateLabel, int port) throws Exception{
            if(port < 0 || this.inputGates.size() <= port){
                throw new Exception(String.format("Port value '%d' is out of boundaries : must be between 0 - %d", port, this.inputGates.size()));
            }

            for(String label : this.inputGates.keySet()){
                if(this.inputGates.get(label).getSchemaInputPort() == port){
                    throw new Exception(String.format("The input port '%d' is already used by the input gate '%s'", port, label));
                }
            }

            this.inputGates.get(inputGateLabel).setSchemaInputPort(port);
        }

    //#endregion

    //#region .setSchemaOutputPort

        public void setSchemaOutput(String outputGateLabel, int port) throws Exception{
            if(port < 0 || this.outputGates.size() <= port){
                throw new Exception(String.format("Port value '%d' is out of boundaries : must be between 0 - %d", port, this.outputGates.size()));
            }

            for(String label : this.outputGates.keySet()){
                if(this.outputGates.get(label).getSchemaOutputPort() == port){
                    throw new Exception(String.format("The output port '%d' is already used by the input gate '%s'", port, label));
                }
            }

            this.outputGates.get(outputGateLabel).setSchemaOutputPort(port);
        }

    //#endregion

    //#region .connectGate

        /**
         * @param fromGate The gate whose output port you want to connect
         * @param toGate The gate whose input port you want to connect
         * @param fromPort The index of the output port
         * @param toPort The index of the input port
         * @return The cable created
         */
        public Cable connectGate(String fromGate, String toGate, int fromPort, int toPort) throws Exception{
            if(!this.allGates.containsKey(fromGate)){
                throw new Exception(String.format("Key not found : %s", fromGate));
            }
            if(!this.allGates.containsKey(toGate)){
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

    //#region .addGate

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

        /**
         * Add a gate to the circuit
         *
         * @param gate
         * @param label
         * @return The gate added
         * @throws Exception
         */
        public Gate addGate(Gate gate, String label) throws Exception{

            if(label == null || label.isBlank()){
                throw new Exception(String.format("label can't be empty"));
            }

            if(allGates.containsKey(label)){
                throw new Exception(String.format("This label is already taken : '%s'", label));
            }

            this.allGates.put(label,gate);
            if (gate instanceof Input) {
                this.inputGates.put(label,((Input)gate));
            }
            else if(gate instanceof Output){
                this.outputGates.put(label,((Output)gate));
            }

            return gate;
        }

    //#endregion

    //#region .addNewGate
    // TODO : needs more parameters to customise gate creation (busSize, delay, etc..)


    /**
     * @param type
     * @return The gate created
     * @throws Exception
     */
    public Gate addNewGate(String type) throws Exception{
        return addNewGate(type, "");
    }

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
            case "Schema":
            newGate = new Schema(label);
            break;
            default:
                throw new Exception(String.format("No match found for the string '%s'", type));
        }

        if(label == null || label.isBlank()){
            this.addGate(newGate);
        }
        else{
            this.addGate(newGate, label);
        }

        return newGate;
    }

    //#endregion

    //#region .addGatesFromJson

    public void addGatesFromJson(JSONObject circuit_Json) throws Exception{
        Circuit tempCircuit = new Circuit();

        try {
            JSONArray gate_JsonArray = circuit_Json.getJSONArray("Gate");

            // 1 : We create new gates from those we find in the json Object and we set their old uuid as a label
            for (int i = 0; i < gate_JsonArray.length(); i++){
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String type = gate_Json.getString("type");
                String oldId = String.valueOf(gate_Json.getInt("uuid"));
                tempCircuit.addNewGate(type, oldId);
            }

            // 2 : Once all the gate are created, we connect them thanks to their old uuid
            // Though, cables will now use their new 'uuid'
            for (int i = 0; i < gate_JsonArray.length(); i++){
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String baseGateOldId = String.valueOf(gate_Json.getInt("uuid"));

                JSONArray output_JsonArray = gate_Json.getJSONArray("outputTo");
                for(int baseGateOutputIndex = 0; baseGateOutputIndex < output_JsonArray.length(); baseGateOutputIndex++){
                    String targetGateOldId = String.valueOf(output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(0));
                    int targetGateInputIndex = output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(1);

                    // the target gate is outside the selection_Json
                    if(targetGateOldId.equals("-1")) continue;

                    tempCircuit.connectGate(baseGateOldId, targetGateOldId, baseGateOutputIndex, targetGateInputIndex);
                }
            }

            // 3 : We fuse the temporary ciruit with the main one
            for(Gate gate : tempCircuit.allGates.values()){
                this.addGate(gate);
            }
        } catch (JSONException e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    public void addGatesFromJson(JSONObject circuit_Json, Schema schema) throws Exception{
        if(schema == null){
            throw new Exception("schema is null");
        }

        Circuit tempCircuit = new Circuit();

        try {
            JSONArray gate_JsonArray = circuit_Json.getJSONArray("Gate");

            // 1 : We create new gates from those we find in the json Object and we set their old uuid as a label
            for (int i = 0; i < gate_JsonArray.length(); i++){
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String type = gate_Json.getString("type");
                String oldId = String.valueOf(gate_Json.getInt("uuid"));
                tempCircuit.addNewGate(type, oldId);
            }

            // 2 : Once all the gate are created, we connect them thanks to their old uuid
            // Though, cables will now use their new 'uuid'

            // But as we are in a schema here, we want to start connecting the gates port possessing a schema port index
            HashMap<Integer, ArrayList<Integer>> innerInputGatesWithIndex = new HashMap<>();
            HashMap<Integer, ArrayList<Integer>> innerOuputGatesWithIndex = new HashMap<>();

            for (int i = 0; i < gate_JsonArray.length(); i++){
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String baseGateOldId = String.valueOf(gate_Json.getInt("uuid"));

                // inner input port detected
                JSONArray input_JsonArray = gate_Json.getJSONArray("inputFrom");
                for(int baseGateInputIndex = 0; baseGateInputIndex < input_JsonArray.length(); baseGateInputIndex++){
                    String targetGateOldId = String.valueOf(input_JsonArray.getJSONArray(baseGateInputIndex).getInt(0));
                    int targetGateOutputIndex = input_JsonArray.getJSONArray(baseGateInputIndex).getInt(1);

                    if(targetGateOldId.equals("-1")){
                        if(targetGateOutputIndex != -1){
                            schema.connectInnerInput(tempCircuit.get_allGates().get(baseGateOldId), baseGateInputIndex, targetGateOutputIndex);
                            if(!innerInputGatesWithIndex.containsKey(i)){
                                innerInputGatesWithIndex.put(i, new ArrayList<>());
                            }
                            innerInputGatesWithIndex.get(i).add(baseGateInputIndex);
                        }
                    }
                }

                // inner output port detected
                JSONArray output_JsonArray = gate_Json.getJSONArray("outputTo");
                for(int baseGateOutputIndex = 0; baseGateOutputIndex < output_JsonArray.length(); baseGateOutputIndex++){
                    String targetGateOldId = String.valueOf(output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(0));
                    int targetGateInputIndex = output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(1);

                    if(targetGateOldId.equals("-1")){
                        if(targetGateInputIndex != -1){
                            schema.connectInnerOutput(tempCircuit.get_allGates().get(baseGateOldId), baseGateOutputIndex, targetGateInputIndex);
                            if(!innerOuputGatesWithIndex.containsKey(i)){
                                innerOuputGatesWithIndex.put(i, new ArrayList<>());
                            }
                            innerOuputGatesWithIndex.get(i).add(baseGateOutputIndex);
                        }
                    }
                }
            }

            // Then we connect all the other gates an thoses with no inner input/output indication (-1)
            for (int i = 0; i < gate_JsonArray.length(); i++){
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String baseGateOldId = String.valueOf(gate_Json.getInt("uuid"));

                JSONArray output_JsonArray = gate_Json.getJSONArray("outputTo");
                for(int baseGateOutputIndex = 0; baseGateOutputIndex < output_JsonArray.length(); baseGateOutputIndex++){
                    // if the connection has already been made before we skip this port
                    if(innerOuputGatesWithIndex.containsKey(i) && innerOuputGatesWithIndex.get(i).contains(baseGateOutputIndex)) continue;

                    String targetGateOldId = String.valueOf(output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(0));
                    int targetGateInputIndex = output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(1);

                    // the target gate is outside the selection_Json
                    // // // we are in a schema and want to connect a cable between the inner circuit and the schema gate (output)
                    if(targetGateOldId.equals("-1")){
                        schema.connectInnerOutput(tempCircuit.get_allGates().get(baseGateOldId), baseGateOutputIndex, targetGateInputIndex);
                        continue;
                    }

                    tempCircuit.connectGate(baseGateOldId, targetGateOldId, baseGateOutputIndex, targetGateInputIndex);
                }

                // we are in a schema and want to connect a cable between the inner circuit and the schema gate (input this time)
                JSONArray input_JsonArray = gate_Json.getJSONArray("inputFrom");
                for(int baseGateInputIndex = 0; baseGateInputIndex < input_JsonArray.length(); baseGateInputIndex++){
                    // if the connection has already been made before we skip this port
                    if(innerInputGatesWithIndex.containsKey(i) && innerInputGatesWithIndex.get(i).contains(baseGateInputIndex)) continue;

                    String targetGateOldId = String.valueOf(input_JsonArray.getJSONArray(baseGateInputIndex).getInt(0));
                    int targetGateOutputIndex = input_JsonArray.getJSONArray(baseGateInputIndex).getInt(1);

                    // the target gate must be outside of the selection_Json
                    if(targetGateOldId.equals("-1")){
                        schema.connectInnerInput(tempCircuit.get_allGates().get(baseGateOldId), baseGateInputIndex, targetGateOutputIndex);
                    }
                }
            }

            // 3 : We fuse the temporary ciruit with the main one
            for(Gate gate : tempCircuit.allGates.values()){
                this.addGate(gate);
            }
        } catch (JSONException e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    //#endregion

    //#region .addGatesFromFile

    /**
     * Load the Json schema of the circuit at the given localisation from './data/'
     * It then calls {@link #addGatesFromJson(JSONObject)} to finish the job.
     *
     * @param filePath
     * @throws Exception
     */
    public void addGatesFromFile(String filePath) throws Exception{
        // We format filePath
        if((filePath != null) && (!filePath.isBlank())){
            filePath = filePath.replace(".", "");

            if(!filePath.startsWith("/")){
                filePath = "/" + filePath;
            }
        }
        else{
            throw new Exception("Empty filePath");
        }

        if(!filePath.startsWith("/data")){
            filePath = "/data" + filePath;
        }

        filePath = "." + filePath;

        if(filePath.endsWith("json")){
            filePath = filePath.substring(0, filePath.length()-4);
        }

        filePath = filePath + ".json";

        filePath = filePath.replace("/", File.separator);


        // We then read and create a json object from the file
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            JSONObject circuit_Json = new JSONObject(content.toString());
            this.addGatesFromJson(circuit_Json);
        } catch (IOException e){
            System.err.println(String.format("Problem while reading '%s' : ", filePath, e.getMessage()));
        }
    }

    //#endregion

    //#region .toJson

        public JSONObject toJson() {
            // adding gates within the circuit to a JSON array
            JSONArray gate_JsonArray = new JSONArray();
            for(Gate gate : allGates.values()){
                gate_JsonArray.put(gate.toJson());
            }

            // Creating the JSON object
            JSONObject circuit_Json = new JSONObject();
            circuit_Json.put("Gate", gate_JsonArray);

            return circuit_Json;
        }

    //#endregion

    //#region .save

        /**
         * Shorthand for {@link #save(String folderPath)}
         * @throws Exception
         */
        public void save() throws Exception{
            this.save("");
        }

        /**
         * Shorthand for {@link #save(String folderPath, String fileName)}
         * @throws Exception
         */
        public void save(String folderPath) throws Exception{
            this.save(folderPath, this.name);
        }

        /**
         * Save the Json schema of the circuit at the given localisation from './data/'
         * The file name will be the name of the circuit
         *
         * see below folderPath exemple
         * ""           -> "./data/"
         * file         -> "./data/file/"
         * /file        -> "./data/file/"
         * data/file    -> "./data/file/"
         * /data/file   -> "./data/file/"
         * ./data/file  -> "./data/file/"
         *
         * @param folderPath
         * @throws Exception
         */
        @SuppressWarnings("CallToPrintStackTrace")
        public void save(String folderPath, String fileName) throws Exception{
            // Formating folderPath
            if((folderPath != null) && (!folderPath.isBlank())){
                folderPath = folderPath.replace(".", "");

                if(!folderPath.startsWith("/")){
                    folderPath = "/" + folderPath;
                }
            }
            else{
                folderPath = "";
            }

            if(!folderPath.startsWith("/data")){
                folderPath = "/data" + folderPath;
            }

            if(!folderPath.endsWith("/")){
                folderPath = folderPath + "/";
            }

            folderPath = "." + folderPath;

            // Formating fileName
            if(fileName.endsWith(".json")){
                fileName = fileName.substring(0, fileName.length()-5);
            }

            // Creating filePath :
            String filePath = String.format("%s%s.json", folderPath, fileName) ;
            filePath = filePath.replace("/", File.separator);


            // We create the designed file
            try {
                File file = new File(filePath);
                if(file.getParentFile().mkdirs() || file.createNewFile()){
                    System.out.println(String.format("'%s' created", filePath));
                }
                else{
                    System.out.println(String.format("'%s' allready exist", filePath));
                }
            } catch (NullPointerException | IOException e){
                e.printStackTrace();
            }

            // We write in the designed file
            try{
                FileWriter writer = new FileWriter(filePath);
                writer.write(this.toJson().toString(1));
                writer.close();

                System.out.println("Circuit saved with success in: " + filePath);
            }
            catch(IOException e){
                System.err.println("Error " + filePath +" can't be saved : " + e.getMessage());
            }
        }

    //#endregion

}
