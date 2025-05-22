package com.pjava.src.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pjava.src.components.cables.Merger;
import com.pjava.src.components.cables.NodeSplitter;
import com.pjava.src.components.cables.Splitter;
import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.gates.Or;
import com.pjava.src.components.gates.Schema;
import com.pjava.src.components.input.Button;
import com.pjava.src.components.input.Clock;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Input;
import com.pjava.src.components.input.Lever;
import com.pjava.src.components.input.Numeric;
import com.pjava.src.components.input.Power;
import com.pjava.src.components.output.Display;
import com.pjava.src.components.output.Output;
import com.pjava.src.utils.UtilsSave;


// TODO : connect back and front
// TODO : UI : Input gates who can't be accessible outside shouldn't have a schema port (not assignable by the user)

public class Circuit {

    // #region Attributes

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
    private HashMap<String, Gate> allGates = new HashMap<>();

    /**
     * List of all the input gates of a circuit
     */
    private HashMap<String, Input> inputGates = new HashMap<>();

    /**
     * List of all the clock gates of a circuit
     */
    private HashMap<String, Clock> clockGates = new HashMap<>();

    /**
     * List of all the button gates of a circuit
     */
    private HashMap<String, Button> buttonGates = new HashMap<>();

    /**
     * List of all the output gates of a circuit
     */
    private HashMap<String, Output> outputGates = new HashMap<>();

    /**
     * List of all the output gates of a circuit
     */
    private HashMap<String, Schema> schemaGates = new HashMap<>();

    // #endregion

    // #region Constructor

    /**
     * Shorthand for {@link #Circuit(String name)}
     */
    public Circuit() {
        this(String.format("circuit_%d", nbCircuit+1));
    }

    /**
     * Create an empty circuit with just a name.
     *
     * @param name name of the circuit
     */
    public Circuit(String name) {
        this.setName(name);
        nbCircuit++;
    }

    /**
     * Shorthand for {@link #Circuit(String name, JSONObject selection)}
     * Generate a circuit name.
     *
     * @param selection Json of a circuit
     */
    public Circuit(JSONObject selection) {
        this(String.format("circuit_%d", nbCircuit+1), selection);
    }

    /**
     * Create a circuit from a selection.
     *
     * @param name name of the circuit
     * @param selection Json of a circuit
     */
    public Circuit(String name, JSONObject selection) {
        this.setName(name);
        nbCircuit++;
        try {
            this.addGatesFromJson(selection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This will create a new circuit from a file
     * If name is set to 'null' then the circuit will take the saved name
     *
     * @param name Can be 'null' to take the save name
     * @param filePath Complete starting by './data/'
     */
    public Circuit(String name, String filePath) {
        nbCircuit++;
        try {
            this.loadGatesFromFile(filePath);
        } catch (Exception e) {
            System.err.println(e);
        }

        if(name != null){
            this.setName(name);
        }
    }

    // #endregion

    // #region Getters

    /**
     * get the name
     * @return the name
     */

    public String getName() {
        return this.name;
    }
    /**
     * get an HashMap of all gates
     * @return all gates
     */
    public HashMap<String, Gate> getAllGates() {
        return this.allGates;
    }
    /**
     * get input gates
     * @return the input of gate
     */
    public HashMap<String, Input> getInputGates() {
        return this.inputGates;
    }
    /**
     * get the clock
     * @return the clock
     */
    public HashMap<String, Clock> getClockGates() {
        return this.clockGates;
    }
    /**
     * get the button
     * @return the button
     */

    public HashMap<String, Button> getButtonGates() {
        return this.buttonGates;
    }
    /**
     * get output gates
     * @return the output gates
     */
    public HashMap<String, Output> getOutputGates() {
        return this.outputGates;
    }
/**
     * get the shema
     * @return the shema
     */
    public HashMap<String, Schema> getSchemaGates() {
        return this.schemaGates;
    }

    /**
     * Recursively finds all clock contained in this circuit and schemas
     *
     * @return The list of all the clocks.
     */
    public ArrayList<Clock> getAllClock() {
        ArrayList<Clock> res = new ArrayList<>();

        // get all clocks in this circuit
        res.addAll(this.getClockGates().values());

        // recursively get all Clocks in shemas
        for (Schema schemaGate : this.getSchemaGates().values()) {
            res.addAll(schemaGate.getInnerCircuit().getAllClock());
        }

        return res;
    }

    // #endregion

    // #region Setters

    public final void setName(String name) {
        if (name == null || name.isBlank()) {
            name = "Name_Generated_Automatically_ahah";
        }
        this.name = name;
    }

    // #endregion


    // #region setSchemaInputGatePort

    /**
     * set the schéma input index of an Input gate
     * verify if the port is in boundaries an is not taken
     *
     * @param inputGateLabel the label/identifier of the input gate whose port should be set
     * @param targetPort the desired port index to assign (must be between 0 and inputGates.size()-1)
     * @return the actual port assigned to the input gate - either the new targetPort if
     *         assignment was successful, or the existing port if targetPort was unavailable
     * @throws Exception if targetPort is outside the valid range (negative or >= inputGates.size())
        */
    public int setSchemaInputGatePort(String inputGateLabel, int targetPort) throws Exception {
        if (targetPort == -1){
            return -1;
        }
        if (targetPort < 0 || this.inputGates.size() <= targetPort) {
            throw new Exception(String.format("Port value '%d' is out of boundaries : must be between 0 - %d", targetPort,
                    this.inputGates.size()));
        }

        // assigned port of the given input gate
        int assignedPort = this.inputGates.get(inputGateLabel).getSchemaInputPort();

        if(assignedPort != targetPort){
            // check if targetPort is allready taken
            for (String label : this.inputGates.keySet()) {
                if (this.inputGates.get(label).getSchemaInputPort() == targetPort) {
                    // TODO : popup message ?
                    System.out.println(String.format("The input port '%d' is already used by the input gate '%s'", targetPort, label));
                    return assignedPort;
                }
            }

            // The target port is not taken, we can get it
            this.inputGates.get(inputGateLabel).setSchemaInputPort(targetPort);
            assignedPort = targetPort;
        }

        return assignedPort;
    }

    // #endregion

    // #region setSchemaOutputGatePort

    /**
     * set the schéma output index of an Output gate
     * verify if the port is in boundaries an is not taken
     *
     * @param outputGateLabel the label/identifier of the output gate whose port should be set
     * @param targetPort the desired port index to assign (must be between 0 and outputGates.size()-1)
     * @return the actual port assigned to the output gate - either the new targetPort if
     *         assignment was successful, or the existing port if targetPort was unavailable
     * @throws Exception if targetPort is outside the valid range (negative or >= outputGates.size())
     */
    public int setSchemaOutputGatePort(String outputGateLabel, int targetPort) throws Exception {
        if (targetPort == -1){
            return -1;
        }
        if (targetPort < 0 || this.outputGates.size() <= targetPort) {
            throw new Exception(String.format("Port value '%d' is out of boundaries : must be between 0 - %d", targetPort,
                    this.outputGates.size()));
        }

        // assigned port of the given input gate
        int assignedPort = this.outputGates.get(outputGateLabel).getSchemaOutputPort();

        if(assignedPort != targetPort){
            // check if targetPort is allready taken
            for (String label : this.outputGates.keySet()) {
                if (this.outputGates.get(label).getSchemaOutputPort() == targetPort) {
                    // TODO : popup message ?
                    System.out.println(String.format("The output port '%d' is already used by the output gate '%s'", targetPort, label));
                    return assignedPort;
                }
            }

            // The target port is not taken, we can get it
            this.outputGates.get(outputGateLabel).setSchemaOutputPort(targetPort);
            assignedPort = targetPort;
        }

        return assignedPort;
    }

    // #endregion



    // #region addGate

    /**
     * Adds a gate to the circuit using the gate's UUID as the label.
     *
     * @param gate the gate to add to the circuit (must not be null)
     * @return the gate that was added to the circuit
     * @throws Exception if the gate cannot be added or if UUID-based label conflicts occur
     */
    public Gate addGate(Gate gate) throws Exception {
        return addGate(gate, gate.uuid().toString());
    }

    /**
     * Adds a gate to the circuit with a specified label
     *
     * @param gate the gate to add to the circuit (must not be null)
     * @param label the unique identifier for the gate (must not be null, blank, or already taken)
     * @return the gate that was added to the circuit
     * @throws Exception if the label is null/blank, already exists, or if the gate cannot be added
     */
    public Gate addGate(Gate gate, String label) throws Exception {

        if (label == null || label.isBlank()) {
            throw new Exception(String.format("label can't be empty"));
        }

        if (allGates.containsKey(label)) {
            throw new Exception(String.format("This label is already taken : '%s'", label));
        }

        this.allGates.put(label, gate);
        if (gate instanceof Input) {
            this.inputGates.put(label, ((Input) gate));

            if (gate instanceof Clock) {
                this.getClockGates().put(label, ((Clock) gate));
            } else if (gate instanceof Button) {
                this.getButtonGates().put(label, ((Button) gate));
            }
        } else if (gate instanceof Schema) {
            this.schemaGates.put(label, ((Schema) gate));
        } else if (gate instanceof Output) {
            this.outputGates.put(label, ((Output) gate));
        }

        return gate;
    }

    // #endregion

    // #region addNewGate

    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * The default size of the input/output bus of the gate will be used.
     * The label will be set to the new gate uuid.
     *
     * Creates and adds a new gate with default configuration
     *
     * @param type type of the gate to create
     * @return The newly created and added gate
     * @throws Exception if the gate type doesn't exist or creation fails
     */
    public Gate addNewGate(String type) throws Exception {
        return addNewGate(type, "");
    }

    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * Setting custom base parameters
     * The label will be set to the new gate uuid.
     *
     * Creates and adds a new gate with custom bus sizes.
     *
     * @param type the type of gate to create
     * @param sizeBusInput array specifying the size of each input bus
     * @param sizeBusOutput array specifying the size of each output bus
     * @return the newly created and added gate
     * @throws Exception if the gate type doesn't exist or creation fails
     */
    public Gate addNewGate(String type, int[] sizeBusInput, int[] sizeBusOutput) throws Exception {
        return addNewGate(type, "", sizeBusInput, sizeBusOutput);
    }

    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * When loading a schema from a file.
     * The label will be set to the new gate uuid.
     *
     * Creates and adds a new schema gate loaded from a file.
     *
     * @param type the type of gate to create
     * @param schemaFile the path to the schema definition file
     * @param unused_int_to_create_this_shorthand unused parameter for method signature differentiation
     * @return the newly created and added schema gate
     * @throws Exception if the file cannot be loaded or gate creation fails
     */
    public Gate addNewGate(String type, String schemaFile, int unused_int_to_create_this_shorthand) throws Exception {
        return addNewGate(type, "", schemaFile);
    }

    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * When loading a schema from a json selection.
     * The label will be set to the new gate uuid.
     *
     * Creates and adds a new schema gate from a JSON configuration.
     *
     * @param type the type of gate to create
     * @param schemaJson the JSON object containing the schema definition
     * @return the newly created and added schema gate
     * @throws Exception if the JSON is invalid or gate creation fails
     */
    public Gate addNewGate(String type, JSONObject schemaJson) throws Exception {
        return addNewGate(type, "", schemaJson);
    }


    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * The default size of the input/output bus of the gate will be used.
     *
     * Creates and adds a new gate with a custom label.
     *
     * @param type the type of gate to create
     * @param label the custom label for the gate (if empty, UUID will be used)
     * @return the newly created and added gate
     * @throws Exception if the gate type doesn't exist, label is already taken, or creation fails
     */
    public Gate addNewGate(String type, String label) throws Exception {
        return addNewGate(type, label, null, null, null, null);
    }

    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * Setting custom base parameters
     * The default size of the input/output bus of the gate will be used.
     *
     * Creates and adds a new gate with custom label and bus sizes
     *
     * @param type the type of gate to create
     * @param label the custom label for the gate (if empty, UUID will be used)
     * @param sizeBusInput array specifying the size of each input bus
     * @param sizeBusOutput array specifying the size of each output bus
     * @return the newly created and added gate
     * @throws Exception if the gate type doesn't exist, label is already taken, or creation fails
     */
    public Gate addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput) throws Exception {
        return addNewGate(type, label, sizeBusInput, sizeBusOutput, null, null);
    }

    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * When loading a schema from a file.
     *
     *  Creates and adds a new schema gate from a file with a custom label.
     *
     * @param type the type of gate to create
     * @param label the custom label for the gate (if empty, UUID will be used)
     * @param schemaFile the path to the schema definition file
     * @return the newly created and added schema gate
     * @throws Exception if the file cannot be loaded, label is already taken, or gate creation fails
     */
    public Gate addNewGate(String type, String label, String schemaFile) throws Exception {
        return addNewGate(type, label, null, null, schemaFile, null);
    }

    /**
     * shorthand for {@link #addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson)}
     * When loading a schema from a json selection.
     *
     * Creates and adds a new schema gate from JSON with a custom label.
     *
     * @param type the type of gate to create
     * @param label the custom label for the gate (if empty, UUID will be used)
     * @param schemaJson the JSON object containing the schema definition
     * @return the newly created and added schema gate
     * @throws Exception if the JSON is invalid, label is already taken, or gate creation fails
     */
    public Gate addNewGate(String type, String label, JSONObject schemaJson) throws Exception {
        return addNewGate(type, label, null, null, null, schemaJson);
    }


    /**
     * create a gate and add it to the circuit using {@code addGate()}
     * The base informations can be sent and will be picked depending on the gate type.
     * If set to 'null', the default version of the gate will be created.
     *
     * Please note that a schema needs eather a file or a json selection to load from.
     *
     * @param type the type of gate to create
     * @param label the label for the gate (can be null)
     * @param sizeBusInput input bus sizes (can be null for default)
     * @param sizeBusOutput output bus sizes (can be null for default)
     * @param schemaFile file path for schema gates (can be null)
     * @param schemaJson JSON object for schema gates (can be null)
     * @return the created gate
     * @throws Exception if gate creation fails
     */
    public Gate addNewGate(String type, String label, int[] sizeBusInput, int[] sizeBusOutput, String schemaFile, JSONObject schemaJson) throws Exception {

        // TODO : debug
        // debug
        ArrayList<Integer> printSizeBusInput = new ArrayList<>();
        if(sizeBusInput != null){
            for(int val : sizeBusInput){
                printSizeBusInput.add(val);
            }
        }
        ArrayList<Integer> printSizeBusOutput = new ArrayList<>();
        if(sizeBusOutput != null){
            for(int val : sizeBusOutput){
                printSizeBusOutput.add(val);
            }
        }

        System.err.println(String.format("type : '%s' label : '%s' sizeInput : %s sizeOutput : %s schemaFile '%s' schemaJson %s",
            type, label, printSizeBusInput, printSizeBusOutput, schemaFile, schemaJson));
        // --- end debug

        Gate newGate;
        switch (type) {
            // Input
            case "Power":
                if(sizeBusOutput != null && sizeBusOutput.length > 0){
                    newGate = new Power(sizeBusOutput[0]);
                }
                else{
                    newGate = new Power();
                }
                break;
            case "Ground":
                if(sizeBusOutput != null && sizeBusOutput.length > 0){
                    newGate = new Ground(sizeBusOutput[0]);
                }
                else{
                    newGate = new Ground();
                }
                break;
            case "Lever":
                newGate = new Lever();
                break;
            case "Numeric":
                if(sizeBusOutput != null && sizeBusOutput.length > 0){
                    newGate = new Numeric(sizeBusOutput[0]);
                }
                else{
                    newGate = new Numeric();
                }
                break;
            case "Button":
                newGate = new Button();
                break;
            case "Clock":
                newGate = new Clock();
                break;

            // Output
            case "Display":
                if(sizeBusInput != null && sizeBusInput.length > 0){
                    newGate = new Display(sizeBusInput[0]);
                }
                else{
                    newGate = new Display();
                }
                break;

            // Gate
            case "Not":
                if(sizeBusOutput != null && sizeBusOutput.length > 0){
                    newGate = new Not(sizeBusOutput[0]);
                }
                else{
                    newGate = new Not();
                }
                break;
            case "And":
                if(sizeBusOutput != null && sizeBusOutput.length > 0){
                    newGate = new And(sizeBusOutput[0]);
                }
                else{
                    newGate = new And();
                }
                break;
            case "Or":
                if(sizeBusOutput != null && sizeBusOutput.length > 0){
                    newGate = new Or(sizeBusOutput[0]);
                }
                else{
                    newGate = new Or();
                }
                break;
            case "Schema":
                if(schemaFile != null && !schemaFile.isBlank()){
                    newGate = new Schema(schemaFile);
                }
                else if(schemaJson != null){
                    newGate = new Schema(schemaJson);
                }
                else{
                    throw new Exception("A schema needs eather a file or a json selection to load from");
                }
                break;

            // Cable
            case "NodeSplitter":
                if(sizeBusInput != null && sizeBusInput.length > 0){
                    newGate = new NodeSplitter(sizeBusInput[0]);
                }
                else{
                    newGate = new NodeSplitter();
                }
                break;
            case "Splitter":
                if(sizeBusInput != null && sizeBusInput.length > 0){
                    newGate = new Splitter(sizeBusInput[0]);
                }
                else{
                    newGate = new Splitter();
                }
                break;
            case "Merger":
                if(sizeBusInput != null  && sizeBusInput.length > 0){
                    newGate = new Merger(sizeBusInput);
                }
                else{
                    newGate = new Merger();
                }
                break;

            default:
                throw new Exception(String.format("No match found for the string '%s'", type));
        }

        if (label == null || label.isBlank()) {
            this.addGate(newGate);
        } else {
            this.addGate(newGate, label);
        }

        return newGate;
    }

    // #endregion

    // #region addGatesFromJson
    // TODO : after creating a gate, load the specific informations
    // TODO : see where to find their specific information in the json

    /**
     * Adds gates to the circuit from a JSON configuration and connects them.
     *
     * @param circuit_Json the JSON object containing the circuit configuration
     * @throws Exception if the circuit cannot be read or contains invalid values
     */
    public final void addGatesFromJson(JSONObject circuit_Json) throws Exception {
        Circuit tempCircuit = new Circuit();

        try {
            JSONArray gate_JsonArray = circuit_Json.getJSONArray("Gate");

            // 1 : We create new gates from those we find in the json Object and we set
            // their old uuid as a label
            for (int i = 0; i < gate_JsonArray.length(); i++) {
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String type = gate_Json.getString("type");
                String oldId = String.valueOf(gate_Json.getInt("uuid"));

                ArrayList<Integer> listToInt = new ArrayList<Integer>();
                JSONArray busSize_JsonArray = gate_Json.getJSONArray("inputBus");
                for(int j = 0; i<busSize_JsonArray.length(); i++){
                    listToInt.add(busSize_JsonArray.getInt(j));
                }
                int[] sizeBusInput = listToInt.stream().mapToInt(Integer::intValue).toArray();

                listToInt.clear();
                busSize_JsonArray = gate_Json.getJSONArray("outputBus");
                for(int j = 0; i<busSize_JsonArray.length(); i++){
                    listToInt.add(busSize_JsonArray.getInt(j));
                }
                int[] sizeBusOutput = listToInt.stream().mapToInt(Integer::intValue).toArray();

                String schemaFile = gate_Json.optString("circuitPath");

                // We create a gate with basic informations
                Gate addedGate = null;
                addedGate = tempCircuit.addNewGate(
                    type,
                    oldId,
                    sizeBusInput,
                    sizeBusOutput,
                    schemaFile,
                    null);

                // We add custom information to this gate
                switch (type) {
                    case "Lever":
                        if(gate_Json.optBoolean("flipped")){
                            ((Lever)addedGate).flip();
                        }
                        break;
                    case "Button":
                        long delayFound = gate_Json.optLong("delay", -1);
                        if(delayFound != -1 && delayFound != ((Button)addedGate).getDelay()){
                            ((Button)addedGate).setDelay(delayFound);
                        }
                        break;
                    case "Clock":
                        long cycleSpeedFound = gate_Json.optLong("cycleSpeed", -1);
                        if(cycleSpeedFound != -1 && cycleSpeedFound != ((Clock)addedGate).getCycleSpeed()){
                            ((Clock)addedGate).setCycleSpeed(cycleSpeedFound);
                        }
                        break;
                    case "Numeric":
                        int baseFound = gate_Json.optInt("base", -1);
                        String valueFound = gate_Json.optString("value", "");
                        if(baseFound != -1 && baseFound != ((Numeric)addedGate).getInputBase()){
                            ((Numeric)addedGate).setInputBase(baseFound);
                        }
                        if(!valueFound.isBlank() && !valueFound.equals(((Numeric)addedGate).getValue())){
                            ((Numeric)addedGate).setInputValue(valueFound);
                        }
                        break;
                    case "Display":
                        baseFound = gate_Json.optInt("base", -1);
                        if(baseFound != -1 && baseFound != ((Display)addedGate).getBaseOutput()){
                            ((Display)addedGate).setBaseOutput(baseFound);
                        }
                        break;
                    case "NodeSplitter":
                        int busNumberFound = sizeBusOutput.length;
                        if(busNumberFound > ((NodeSplitter)addedGate).getOutputNumber()){
                            ((NodeSplitter)addedGate).addOutput(busNumberFound - ((NodeSplitter)addedGate).getOutputNumber());
                        }
                        break;
                }

            }
            // We set the input/output assigned port to a schema
            // Input
            for (String key : tempCircuit.getInputGates().keySet()) {
                Input gate = tempCircuit.getInputGates().get(key);
                if (!(gate instanceof Lever || gate instanceof Numeric)) {
                    continue;
                }

                int inputOldId = Integer.valueOf(key);

                // set the input schema port
                for (int input_jsonIndex = 0; input_jsonIndex < gate_JsonArray.length(); input_jsonIndex++) {
                    if (gate_JsonArray.getJSONObject(input_jsonIndex).getInt("uuid") == inputOldId) {
                        tempCircuit.setSchemaInputGatePort(
                            key,
                            gate_JsonArray.getJSONObject(input_jsonIndex).getInt("schemaInputPort"));
                    }
                }
            }

            // Output
            for (String key : tempCircuit.getOutputGates().keySet()) {
                int outputOldId = Integer.valueOf(key);

                // set the output schema port
                for (int output_jsonIndex = 0; output_jsonIndex < gate_JsonArray.length(); output_jsonIndex++) {
                    if (gate_JsonArray.getJSONObject(output_jsonIndex).getInt("uuid") == outputOldId) {
                        tempCircuit.setSchemaOutputGatePort(
                            key,
                            gate_JsonArray.getJSONObject(output_jsonIndex).getInt("schemaOutputPort"));
                    }
                }
            }

            // 2 : Once all the gate are created, we connect them thanks to their old uuid
            // Though, cables will now use their new 'uuid'
            for (int i = 0; i < gate_JsonArray.length(); i++) {
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String baseGateOldId = String.valueOf(gate_Json.getInt("uuid"));

                JSONArray output_JsonArray = gate_Json.getJSONArray("outputTo");
                for (int baseGateOutputIndex = 0; baseGateOutputIndex < output_JsonArray
                        .length(); baseGateOutputIndex++) {
                    String targetGateOldId = String
                            .valueOf(output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(0));
                    int targetGateInputIndex = output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(1);

                    // the target gate is outside the selection_Json
                    if (targetGateOldId.equals("-1"))
                        continue;

                    tempCircuit.connectGate(baseGateOldId, targetGateOldId, baseGateOutputIndex, targetGateInputIndex);
                }
            }

            // 3 : We fuse the temporary ciruit with the main one
            for (Gate gate : tempCircuit.allGates.values()) {
                this.addGate(gate);
            }
        } catch (JSONException e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    /**
     * add gates from a circuit_Json for the inner circuit of a schéma, it will set
     * the port connection with the schema gate
     *
     * @param circuit_Json the JSON object containing the circuit configuration
     * @param schema the schema gate to connect to
     * @throws Exception if the circuit cannot be read, schema is null, or contains invalid values
     */
    public final void addGatesFromJson(JSONObject circuit_Json, Schema schema) throws Exception {
        if (schema == null) {
            throw new Exception("schema is null");
        }

        Circuit tempCircuit = new Circuit();

        try {
            JSONArray gate_JsonArray = circuit_Json.getJSONArray("Gate");

            // 1 : We create new gates from those we find in the json Object and we set
            // their old uuid as a label
            for (int i = 0; i < gate_JsonArray.length(); i++) {
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String type = gate_Json.getString("type");
                String oldId = String.valueOf(gate_Json.getInt("uuid"));

                ArrayList<Integer> listToInt = new ArrayList<Integer>();
                JSONArray busSize_JsonArray = gate_Json.getJSONArray("inputBus");
                for(int j = 0; i<busSize_JsonArray.length(); i++){
                    listToInt.add(busSize_JsonArray.getInt(j));
                }
                int[] sizeBusInput = listToInt.stream().mapToInt(Integer::intValue).toArray();

                listToInt.clear();
                busSize_JsonArray = gate_Json.getJSONArray("outputBus");
                for(int j = 0; i<busSize_JsonArray.length(); i++){
                    listToInt.add(busSize_JsonArray.getInt(j));
                }
                int[] sizeBusOutput = listToInt.stream().mapToInt(Integer::intValue).toArray();

                String schemaFile = gate_Json.optString("circuitPath");

                // We create a gate with basic informations
                Gate addedGate = null;
                addedGate = tempCircuit.addNewGate(
                    type,
                    oldId,
                    sizeBusInput,
                    sizeBusOutput,
                    schemaFile,
                    null);

                // We add custom information to this gate
                switch (type) {
                    case "Lever":
                        if(gate_Json.optBoolean("flipped")){
                            ((Lever)addedGate).flip();
                        }
                        break;
                    case "Button":
                        long delayFound = gate_Json.optLong("delay", -1);
                        if(delayFound != -1 && delayFound != ((Button)addedGate).getDelay()){
                            ((Button)addedGate).setDelay(delayFound);
                        }
                        break;
                    case "Clock":
                        long cycleSpeedFound = gate_Json.optLong("cycleSpeed", -1);
                        if(cycleSpeedFound != -1 && cycleSpeedFound != ((Clock)addedGate).getCycleSpeed()){
                            ((Clock)addedGate).setCycleSpeed(cycleSpeedFound);
                        }
                        break;
                    case "Numeric":
                        int baseFound = gate_Json.optInt("base", -1);
                        String valueFound = gate_Json.optString("value", "");
                        if(baseFound != -1 && baseFound != ((Numeric)addedGate).getInputBase()){
                            ((Numeric)addedGate).setInputBase(baseFound);
                        }
                        if(!valueFound.isBlank() && !valueFound.equals(((Numeric)addedGate).getValue())){
                            ((Numeric)addedGate).setInputValue(valueFound);
                        }
                        break;
                    case "Display":
                        baseFound = gate_Json.optInt("base", -1);
                        if(baseFound != -1 && baseFound != ((Display)addedGate).getBaseOutput()){
                            ((Display)addedGate).setBaseOutput(baseFound);
                        }
                        break;
                    case "NodeSplitter":
                        int busNumberFound = sizeBusOutput.length;
                        if(busNumberFound > ((NodeSplitter)addedGate).getOutputNumber()){
                            ((NodeSplitter)addedGate).addOutput(busNumberFound - ((NodeSplitter)addedGate).getOutputNumber());
                        }
                        break;
                }
            }

            // We set the input/output assigned port to a schema
            // Input
            for (String key : tempCircuit.getInputGates().keySet()) {
                Input gate = tempCircuit.getInputGates().get(key);
                if (!(gate instanceof Lever || gate instanceof Numeric)) {
                    continue;
                }

                int inputOldId = Integer.valueOf(key);

                // set the input schema port
                for (int input_jsonIndex = 0; input_jsonIndex < gate_JsonArray.length(); input_jsonIndex++) {
                    if (gate_JsonArray.getJSONObject(input_jsonIndex).getInt("uuid") == inputOldId) {
                        tempCircuit.setSchemaInputGatePort(
                            key,
                            gate_JsonArray.getJSONObject(input_jsonIndex).getInt("schemaInputPort"));
                    }
                }
            }

            // Output
            for (String key : tempCircuit.getOutputGates().keySet()) {

                int outputOldId = Integer.valueOf(key);

                // set the output schema port
                for (int output_jsonIndex = 0; output_jsonIndex < gate_JsonArray.length(); output_jsonIndex++) {
                    if (gate_JsonArray.getJSONObject(output_jsonIndex).getInt("uuid") == outputOldId) {
                        tempCircuit.setSchemaOutputGatePort(
                            key,
                            gate_JsonArray.getJSONObject(output_jsonIndex).getInt("schemaOutputPort"));
                    }
                }
            }

            // 1.bis : We whant to turn all Input(Lever/Numeric)  and Output into schema port
            // Input
            for (String key : tempCircuit.getInputGates().keySet()) {
                // We only want to make schema port from thoses input gates
                Input gate = tempCircuit.getInputGates().get(key);
                if (!(gate instanceof Lever || gate instanceof Numeric)) {
                    continue;
                }

                int inputOldId = Integer.valueOf(key);

                // get the input gate_json
                int input_jsonIndex;
                for (input_jsonIndex = 0; input_jsonIndex < gate_JsonArray.length(); input_jsonIndex++) {
                    if (gate_JsonArray.getJSONObject(input_jsonIndex).getInt("uuid") == inputOldId) {
                        break;
                    }
                }

                // find if the input gate is connected to anorther gate and change his input
                // connection
                JSONArray inputOutput_JsonArray = gate_JsonArray.getJSONObject(input_jsonIndex)
                        .getJSONArray("outputTo");
                if (inputOutput_JsonArray.length() == 1) {
                    int targetGateOldId = inputOutput_JsonArray.getJSONArray(0).getInt(0);
                    if (targetGateOldId < 0
                            || !tempCircuit.getAllGates().containsKey(String.valueOf(targetGateOldId))) {
                        throw new Exception("input target not found in the circuit");
                    }

                    // get the target gate_json
                    int target_jsonIndex;
                    for (target_jsonIndex = 0; target_jsonIndex < gate_JsonArray.length(); target_jsonIndex++) {
                        if (gate_JsonArray.getJSONObject(target_jsonIndex).getInt("uuid") == targetGateOldId) {
                            break;
                        }
                    }

                    // find were the input gate is connected in the target gate and change his input
                    // connection
                    JSONArray targetInput_JsonArray = gate_JsonArray.getJSONObject(target_jsonIndex)
                            .getJSONArray("inputFrom");
                    for (int targetGateInputIndex = 0; targetGateInputIndex < targetInput_JsonArray
                            .length(); targetGateInputIndex++) {
                        if (targetInput_JsonArray.getJSONArray(targetGateInputIndex).getInt(0) == inputOldId) {
                            // we set the target get to be connected to the schéma port
                            targetInput_JsonArray.getJSONArray(targetGateInputIndex).put(0, -1);
                            targetInput_JsonArray.getJSONArray(targetGateInputIndex).put(
                                    1,
                                    gate_JsonArray.getJSONObject(input_jsonIndex).getInt("schemaInputPort"));
                        }
                    }
                }

                // we now delete the gate from the circuit and the json
                gate_JsonArray.remove(input_jsonIndex);

                // remove from the circuit
                tempCircuit.delGate(key);
            }

            // output
            for (String key : tempCircuit.getOutputGates().keySet()) {

                // We only want to make schema port from thoses output gates
                /*
                Output gate = tempCircuit.getOutputGates().get(key);
                if (!(gate instanceof Display)) {
                    continue;
                }
                */

                int outputOldId = Integer.valueOf(key);

                // get the output gate_json
                int output_jsonIndex;
                for (output_jsonIndex = 0; output_jsonIndex < gate_JsonArray.length(); output_jsonIndex++) {
                    if (gate_JsonArray.getJSONObject(output_jsonIndex).getInt("uuid") == outputOldId) {
                        break;
                    }
                }

                // find if the output gate is connected to anorther gate and change his input
                // connection
                JSONArray outputInput_JsonArray = gate_JsonArray.getJSONObject(output_jsonIndex)
                        .getJSONArray("inputFrom");
                if (outputInput_JsonArray.length() == 1) {
                    int targetGateOldId = outputInput_JsonArray.getJSONArray(0).getInt(0);
                    if (targetGateOldId < 0
                            || !tempCircuit.getAllGates().containsKey(String.valueOf(targetGateOldId))) {
                        throw new Exception("output target not found in the circuit");
                    }

                    // get the target gate_json
                    int target_jsonIndex;
                    for (target_jsonIndex = 0; target_jsonIndex < gate_JsonArray.length(); target_jsonIndex++) {
                        if (gate_JsonArray.getJSONObject(target_jsonIndex).getInt("uuid") == targetGateOldId) {
                            break;
                        }
                    }

                    // find were the output gate is connected in the target gate and change his
                    // output connection
                    JSONArray targetOutput_JsonArray = gate_JsonArray.getJSONObject(target_jsonIndex)
                            .getJSONArray("outputTo");
                    for (int targetGateOutputIndex = 0; targetGateOutputIndex < targetOutput_JsonArray
                            .length(); targetGateOutputIndex++) {
                        if (targetOutput_JsonArray.getJSONArray(targetGateOutputIndex).getInt(0) == outputOldId) {
                            // we set the target get to be connected to the schéma port
                            targetOutput_JsonArray.getJSONArray(targetGateOutputIndex).put(0, -1);
                            targetOutput_JsonArray.getJSONArray(targetGateOutputIndex).put(
                                    1,
                                    gate_JsonArray.getJSONObject(output_jsonIndex).getInt("schemaOutputPort"));
                        }
                    }
                }

                // we now delete the gate from the circuit and the json
                gate_JsonArray.remove(output_jsonIndex);

                // remove from the circuit
                tempCircuit.delGate(key);
            }

            // 2 : Once all the gate are created, we connect them thanks to their old uuid
            // Though, cables will now use their new 'uuid'

            // But as we are in a schema here, we want to start connecting the gates port
            // possessing a schema port index
            HashMap<Integer, ArrayList<Integer>> innerInputGatesWithIndex = new HashMap<>();
            HashMap<Integer, ArrayList<Integer>> innerOuputGatesWithIndex = new HashMap<>();

            for (int i = 0; i < gate_JsonArray.length(); i++) {
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String baseGateOldId = String.valueOf(gate_Json.getInt("uuid"));

                // inner input port detected
                JSONArray input_JsonArray = gate_Json.getJSONArray("inputFrom");
                for (int baseGateInputIndex = 0; baseGateInputIndex < input_JsonArray.length(); baseGateInputIndex++) {
                    String targetGateOldId = String.valueOf(input_JsonArray.getJSONArray(baseGateInputIndex).getInt(0));
                    int targetGateOutputIndex = input_JsonArray.getJSONArray(baseGateInputIndex).getInt(1);

                    if (targetGateOldId.equals("-1")) {
                        if (targetGateOutputIndex != -1) {
                            Gate.connectInnerInputGate(
                                schema,
                                targetGateOutputIndex,
                                tempCircuit.getAllGates().get(baseGateOldId),
                                baseGateInputIndex);
                            if (!innerInputGatesWithIndex.containsKey(i)) {
                                innerInputGatesWithIndex.put(i, new ArrayList<>());
                            }
                            innerInputGatesWithIndex.get(i).add(baseGateInputIndex);
                        }
                    }
                }

                // inner output port detected
                JSONArray output_JsonArray = gate_Json.getJSONArray("outputTo");
                for (int baseGateOutputIndex = 0; baseGateOutputIndex < output_JsonArray
                        .length(); baseGateOutputIndex++) {
                    String targetGateOldId = String
                            .valueOf(output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(0));
                    int targetGateInputIndex = output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(1);

                    if (targetGateOldId.equals("-1")) {
                        if (targetGateInputIndex != -1) {
                            Gate.connectInnerOutputGate(
                                schema,
                                targetGateInputIndex,
                                tempCircuit.getAllGates().get(baseGateOldId),
                                baseGateOutputIndex);
                            if (!innerOuputGatesWithIndex.containsKey(i)) {
                                innerOuputGatesWithIndex.put(i, new ArrayList<>());
                            }
                            innerOuputGatesWithIndex.get(i).add(baseGateOutputIndex);
                        }
                    }
                }
            }

            // Then we connect all the other gates an thoses with no inner input/output
            // indication (-1)
            for (int i = 0; i < gate_JsonArray.length(); i++) {
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String baseGateOldId = String.valueOf(gate_Json.getInt("uuid"));

                JSONArray output_JsonArray = gate_Json.getJSONArray("outputTo");
                for (int baseGateOutputIndex = 0; baseGateOutputIndex < output_JsonArray
                        .length(); baseGateOutputIndex++) {
                    // if the connection has already been made before we skip this port
                    if (innerOuputGatesWithIndex.containsKey(i)
                            && innerOuputGatesWithIndex.get(i).contains(baseGateOutputIndex)) {
                        continue;
                    }

                    String targetGateOldId = String
                            .valueOf(output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(0));
                    int targetGateInputIndex = output_JsonArray.getJSONArray(baseGateOutputIndex).getInt(1);

                    // the target gate is the schema
                    if (targetGateOldId.equals("-1")) {
                        Gate.connectInnerOutputGate(
                            schema,
                            targetGateInputIndex,
                            tempCircuit.getAllGates().get(baseGateOldId),
                            baseGateOutputIndex);
                        continue;
                    }

                    tempCircuit.connectGate(baseGateOldId, targetGateOldId, baseGateOutputIndex, targetGateInputIndex);
                }

                // we are in a schema and want to connect a cable between the inner circuit and
                // the schema gate (input this time)
                JSONArray input_JsonArray = gate_Json.getJSONArray("inputFrom");
                for (int baseGateInputIndex = 0; baseGateInputIndex < input_JsonArray.length(); baseGateInputIndex++) {
                    // if the connection has already been made before we skip this port
                    if (innerInputGatesWithIndex.containsKey(i)
                            && innerInputGatesWithIndex.get(i).contains(baseGateInputIndex)) {
                        continue;
                    }

                    String targetGateOldId = String.valueOf(input_JsonArray.getJSONArray(baseGateInputIndex).getInt(0));
                    int targetGateOutputIndex = input_JsonArray.getJSONArray(baseGateInputIndex).getInt(1);

                    // the target gate is the schema
                    if (targetGateOldId.equals("-1")) {
                        Gate.connectInnerInputGate(
                            schema,
                            targetGateOutputIndex,
                            tempCircuit.getAllGates().get(baseGateOldId),
                            baseGateInputIndex);
                    }
                }
            }

            // 3 : We fuse the temporary ciruit with the main one
            for (Gate gate : tempCircuit.allGates.values()) {
                this.addGate(gate);
            }
        } catch (JSONException e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
            e.printStackTrace();
        }
    }

    // #endregion

    // #region loadGatesFromFile

    /**
     * Loads gates from a JSON file and adds them to the circuit.
     * @param filePath the path to the JSON file
     * @throws Exception if the file cannot be read or contains invalid data
     */

    public final void loadGatesFromFile(String filePath) throws Exception {
        loadGatesFromFile(filePath, null);
    }

    /**
     * Load the Json of the circuit at the given localisation from './data/'
     * It then calls {@link #addGatesFromJson(JSONObject)} to finish the job.
     * Set the name of the circuit to the circuit loaded name.
     *
     * @param filePath the path to the JSON file
     * @param schema the schema gate to connect to (can be null)
     * @throws Exception if the file cannot be read or contains invalid data
     */
    public final void loadGatesFromFile(String filePath, Schema schema) throws Exception {
        // We format filePath
        if ((filePath != null) && (!filePath.isBlank())) {
            filePath = filePath.replace(".", "");

            if (!filePath.startsWith("/")) {
                filePath = "/" + filePath;
            }
        } else {
            throw new Exception("Empty filePath");
        }

        if (!filePath.startsWith(UtilsSave.saveFolder.toString().replace(".", ""))) {
            filePath = UtilsSave.saveFolder.toString() + filePath;
        }
        else{
            filePath = "." + filePath;
        }

        if (filePath.endsWith("json")) {
            filePath = filePath.substring(0, filePath.length() - 4);
        }

        filePath = filePath + ".json";

        filePath = filePath.replace("/", File.separator);

        // We then read and create a json object from the file
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            JSONObject circuit_Json = new JSONObject(content.toString());

            // As we are loading the circuit from a file, we set the name to the loaded circuit
            this.setName(circuit_Json.getString("name"));

            // if we are dealing with a schema
            if (schema != null) {
                this.addGatesFromJson(circuit_Json, schema);
            } else {
                this.addGatesFromJson(circuit_Json);
            }
        } catch (IOException e) {
            System.err.println(String.format("Problem while reading '%s' : ", filePath, e.getMessage()));
        }
    }

    // #endregion



    // #region delGate


    /**
     * Removes a gate from the circuit and disconnects all its connections.
     *
     * @param label the label of the gate to remove
     */
    public void delGate(String label) {
        if (this.getAllGates().containsKey(label)) {
            Gate gate = this.getAllGates().get(label);

            gate.disconnect();
            this.getAllGates().remove(label);

            if (gate instanceof Input) {
                this.getInputGates().remove(label);

                if (gate instanceof Clock) {
                    this.getClockGates().remove(label);
                } else if (gate instanceof Button) {
                    this.getButtonGates().remove(label);
                }
            } else if (gate instanceof Schema) {
                this.getSchemaGates().remove(label);
            } else if (gate instanceof Output) {
                this.getOutputGates().remove(label);
            }
        } else {
            System.err.println(String.format("Warnig : gate '%s' not found in circuit '%s'", label, this.name));
        }
    }

    // #endregion

    // #region delGateFromIdList
    /**
     * Removes multiple gates from the circuit.
     *
     * @param labelGates list of gate labels to remove
     */
    public void delGateFromIdList(ArrayList<String> labelGates) {
        for (String label : labelGates) {
            delGate(label);
        }
    }

    // #endregion


    // #region connectGate

    /**
     * Connects two gates by creating a cable between specified ports
     *
     * @param fromGate The gate whose output port you want to connect
     * @param toGate   The gate whose input port you want to connect
     * @param fromPort The index of the output port
     * @param toPort   The index of the input port
     * @return The cable created
     * @throws Exception if either gate is not found or connection fails
     */
    public Cable connectGate(String fromGate, String toGate, int fromPort, int toPort) throws Exception {
        if (!this.allGates.containsKey(fromGate)) {
            throw new Exception(String.format("Key not found : %s", fromGate));
        }
        if (!this.allGates.containsKey(toGate)) {
            throw new Exception(String.format("Key not found : %s", toGate));
        }

        Cable res = null;

        try {
            res = this.allGates.get(fromGate).connect(this.allGates.get(toGate), fromPort, toPort);
        } catch (Exception e) {
            System.err.println(e);
        }

        return res;
    }

    // #endregion

    // #region selectGatesFromIdList

        /**
         * Create the formated json of a selection of gates.
         * All port with a cable going toward a gate that is not in the selection
         * will be overriden to [-1, -1].
         *
         * Additionnal note : the format is [int gateIdex, int gatePort].
         *
         * @param labelGates list of gate labels to include in the selection
         * @return JSON object containing the selected gate
         */
    public JSONObject selectGatesFromIdList(ArrayList<String> labelGates){
            JSONObject selection_Json = new JSONObject();

            // selection in Gate Array
            ArrayList<Gate> selectedGates = new ArrayList<>();
            for(String label : labelGates){
                selectedGates.add(this.allGates.get(label));
            }

            // selection in Json Array
            JSONArray gate_JsonArray = new JSONArray();
            for (Gate gate : selectedGates) {
                //System.out.println(gate);
                gate_JsonArray.put(gate.toJson());
            }

            // In the Json Array, we override to -1 the input/output cables going out of the selection
            selectedGates.forEach(gate -> {
                // detection for input cable :
                gate.getInputCable().forEach(cable -> {
                    if (cable != null) {
                        // if the cable is connected to a gate out of the selection
                        if (!selectedGates.contains(cable.getInputGate())) {
                            int gateId = gate.uuid();
                            int targetId = cable.getInputGate().uuid();

                            // we find the json of the connection
                            for (int jsonIndex = 0; jsonIndex < gate_JsonArray.length(); jsonIndex++) {
                                if (gate_JsonArray.getJSONObject(jsonIndex).getInt("uuid") == gateId) {
                                    JSONArray gateInput_JsonArray = gate_JsonArray.getJSONObject(jsonIndex).getJSONArray("inputFrom");

                                    // we find the port of the connection
                                    for (int gateInputIndex = 0; gateInputIndex < gateInput_JsonArray.length(); gateInputIndex++) {
                                        if (gateInput_JsonArray.getJSONArray(gateInputIndex).getInt(0) == targetId) {

                                            // we set the target get to be connected to the schéma port
                                            gateInput_JsonArray.getJSONArray(gateInputIndex).put(0, -1);
                                            gateInput_JsonArray.getJSONArray(gateInputIndex).put(1, -1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

                // detection for output cable :
                gate.getOutputCable().forEach(cable -> {
                    if (cable != null) {
                        if (!selectedGates.contains(cable.getOutputGate())) {
                            int gateId = gate.uuid();
                            int targetId = cable.getOutputGate().uuid();

                            // we find the json of the connection
                            for (int jsonIndex = 0; jsonIndex < gate_JsonArray.length(); jsonIndex++) {
                                if (gate_JsonArray.getJSONObject(jsonIndex).getInt("uuid") == gateId) {
                                    JSONArray gateOutput_JsonArray = gate_JsonArray.getJSONObject(jsonIndex).getJSONArray("outputTo");

                                    // we find the port of the connection
                                    for (int gateOutputIndex = 0; gateOutputIndex < gateOutput_JsonArray.length(); gateOutputIndex++) {
                                        if (gateOutput_JsonArray.getJSONArray(gateOutputIndex).getInt(0) == targetId) {

                                            // we set the target get to be connected to the schéma port
                                            gateOutput_JsonArray.getJSONArray(gateOutputIndex).put(0, -1);
                                            gateOutput_JsonArray.getJSONArray(gateOutputIndex).put(1, -1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            });

            selection_Json.put("Gate", gate_JsonArray);

            return selection_Json;
        }

    //#endregion



    // #region toJson
    /**
     * Converts the circuit to JSON format.
     *
     * @return the circuit as a JSON object
    */

    public JSONObject toJson() {
        // adding gates within the circuit to a JSON array
        JSONArray gate_JsonArray = new JSONArray();
        for (Gate gate : allGates.values()) {
            //System.out.println(gate);
            gate_JsonArray.put(gate.toJson());
        }

        // Creating the JSON object
        JSONObject circuit_Json = new JSONObject();
        circuit_Json.put("Gate", gate_JsonArray);
        circuit_Json.put("name", this.name);

        return circuit_Json;
    }

    // #endregion

    // #region save

    /**
     * Shorthand for {@link #save(String folderPath)}
     *Saves the circuit to the default location with the circuit's name as filename.
     *
     * @throws Exception if the save operation fails
     */
    public void save() throws Exception {
        this.save("");
    }

    /**
     * Shorthand for {@link #save(String folderPath, String fileName)}
     *Saves the circuit to the specified folder with the circuit's name as filename.
     *
     * @param folderPath the folder path where to save the circuit
     * @throws Exception if the save operation fails
     */
    public void save(String folderPath) throws Exception {
        this.save(folderPath, this.name);
    }

    /**
     * Save the Json schema of the circuit at the given localisation from './data/'
     * The file name will be the name of the circuit
     *
     * see below folderPath exemple
     * "" -> "./data/"
     * file -> "./data/file/"
     * /file -> "./data/file/"
     * data/file -> "./data/file/"
     * /data/file -> "./data/file/"
     * ./data/file -> "./data/file/"
     *
     * @param folderPath the folder path where to save the circuit
     * @param fileName the name of the file (without .json extension)
     * @throws Exception if the save operation fails
     */
    public void save(String folderPath, String fileName) throws Exception {
        // Formating folderPath
        if (!new File(folderPath).exists()) {
            if ((folderPath != null) && (!folderPath.isBlank())) {
                folderPath = folderPath.replace(".", "");

                if (!folderPath.startsWith("/")) {
                    folderPath = "/" + folderPath;
                }
            } else {
                folderPath = "";
            }

            if (!folderPath.startsWith(UtilsSave.saveFolder.toString().replace(".", ""))) {
                folderPath = UtilsSave.saveFolder.toString() + folderPath;
            }
            else{
                folderPath = "." + folderPath;
            }

        }

        if (!folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }

        // Formating fileName
        if (fileName.endsWith(".json")) {
            fileName = fileName.substring(0, fileName.length() - 5);
        }

        // Creating filePath :
        String filePath = String.format("%s%s.json", folderPath, fileName).replace("/", File.separator);

        // We create the designed file
        try {
            File file = new File(filePath);
            if (file.getParentFile().mkdirs() || file.createNewFile()) {
                System.out.println(String.format("'%s' created", filePath));
            } else {
                System.out.println(String.format("'%s' already exist", filePath));
            }
        } catch (NullPointerException | IOException e) {
            System.err.println(e);
        }

        // We write in the designed file
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(this.toJson().toString(1));
            writer.close();

            System.out.println("Circuit saved with success in: " + filePath);
        } catch (IOException e) {
            System.err.println("Error " + filePath + " can't be saved : " + e.getMessage());
        }
    }

    // #endregion
}
