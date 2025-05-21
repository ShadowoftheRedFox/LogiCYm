package com.pjava.src.components.gates;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Circuit;
import com.pjava.src.components.Gate;
import com.pjava.src.components.input.Input;
import com.pjava.src.components.input.Lever;
import com.pjava.src.components.input.Numeric;
import com.pjava.src.components.output.Display;
import com.pjava.src.components.output.Output;


// TODO : override Gate.updateState() for it to directly '.updateState()' the inner gate connected to a port
// the output are forwarded too

// ToDo cables position
// ToDo position, label, rotation of gates
// ToDo unit tests

/**
 * The schema gate is special as he hold a circuit within himself.
 * His main objective is to connect this inner circuit to the circuit he belongs
 * (outside circuit).
 */
public class Schema extends Gate {

    // #region Attributes
    /**
     * Name of the schema.
     */
    private String name;

    /**
     * FIXME javadoc
     */
    private String filePath = null;

    /**
     * Gates that are inside the schema.
     */
    private Circuit innerCircuit;

    /*
     * Extend Gate :
     * private int[] inputBus = new int[]{};
     * private int[] outputBus = new int[]{};
     *
     * private ArrayList<Cable> inputCable = new ArrayList<Cable>();
     * private ArrayList<Cable> outputCable = new ArrayList<Cable>();
     */

    /**
     * FIXME javadoc
     */
    private ArrayList<Cable> innerInputCable = new ArrayList<>();
    /**
     * FIXME javadoc
     */
    private ArrayList<Cable> innerOutputCable = new ArrayList<>();

    // #endregion

    // #region Constructor()

    /**
     * Create a new schema with the given names and json
     * The schema's inner circuit is saved in the folder
     * 'data/schema/SCHEMA_NAME.json'
     *
     * @param schemaName The name of the schema file.
     * @param selection  JSON of a selection to create the schema
     * @throws Exception Throws if the schema fails to save, or name is invalid, or
     *                   gates array is invalid.
     */
    public Schema(String schemaName, JSONObject selection) throws Exception {
        this.setName(schemaName);
        this.loadFromJson(selection);

        this.filePath = String.format("./data/schema/%s.json", schemaName);
        this.saveInnerCircuit();
    }

    /**
     * Create a new schema with the given names and a file.
     *
     * @param schemaName The name of the schema.
     * @param filePath   File path to a circuit to create the schema
     * @throws Exception Throws if the schema fails to save, or name is invalid, or
     *                   gates array is invalid.
     */
    public Schema(String schemaName, String filePath) throws Exception {
        this.setName(schemaName);
        this.loadFromFile(filePath);
        this.filePath = filePath;
    }

    // #endregion

    // #region Getters

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public Circuit getInnerCircuit() {
        return innerCircuit;
    }

    public ArrayList<Cable> getInnerInputCable(){
        return innerInputCable;
    }

    public ArrayList<Cable> getInnerOutputCable(){
        return innerOutputCable;
    }


    @Override
    public BitSet getState() {
        return state;
    }

    // #endregion

    // #region Setters

    public final void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can be empty");
        }
        this.name = name;
    }

    // #endregion

    // //#region save()
    //
    // // TODO : si le circuit n'existe pas, tous les cables sortant se transforment
    // en gates input/output
    // /**
    // * Save the current {@link #selectedGates} into the schema with the given
    // {@link #name}.
    // *
    // * @throws FileNotFoundException Throws when file handler fails, typicaly a
    // wrong name.
    // */
    // public void save() throws FileNotFoundException {
    // ArrayList<Gate> excludedOutputs = new ArrayList<>();
    // ArrayList<Gate> excludedInputs = new ArrayList<>();
    //
    // // we get the the input and output of the selected gates, which in the end
    // will
    // // be the inputs and outputs of the schema
    // /*
    // selectedGates.forEach(gate -> {
    // gate.getInputCable().forEach(cable -> {
    // if (cable != null) {
    // if (!selectedGates.contains(cable.getInputGate())) {
    // excludedInputs.add(cable.getInputGate());
    // }
    // if (!selectedGates.contains(cable.getOutputGate())) {
    // excludedOutputs.add(cable.getOutputGate());
    // }
    // }
    // });
    // gate.getOutputCable().forEach(cable -> {
    // if (cable != null) {
    // if (!selectedGates.contains(cable.getInputGate())) {
    // excludedInputs.add(cable.getInputGate());
    // }
    // if (!selectedGates.contains(cable.getOutputGate())) {
    // excludedOutputs.add(cable.getOutputGate());
    // }
    // }
    // });
    // });
    // */
    //
    // // get the bus sizes for each outputs and inputs of the schema
    // excludedInputs.forEach(gate -> {
    // for (int i = 0; i < gate.getOutputNumber(); i++) {
    // externalOutput.add(gate.getOutputBus()[i]);
    // }
    // });
    // excludedOutputs.forEach(gate -> {
    // for (int i = 0; i < gate.getInputNumber(); i++) {
    // externalInput.add(gate.getInputBus()[i]);
    // }
    // });
    //
    // // DEBUG
    // System.out.println("Input Count: " + externalInput.size() + "\n\t" +
    // externalInput);
    // System.out.println("Output Count: " + externalOutput.size() + "\n\t" +
    // externalOutput);
    //
    // // create our json constructor
    // JSONObject schemaObject = new JSONObject();
    // ArrayList<JSONObject> gatesArray = new ArrayList<>();
    //
    // // represent the inputs/outputs bus of the schema
    // schemaObject.put("externalInput", externalInput);
    // schemaObject.put("externalOutput", externalOutput);
    //
    // // save to json format each gate
    // selectedGates.forEach(gate -> {
    // // json constructor of the gate
    // JSONObject gateInfoObject = gate.toJson();
    // // // array of inputs/outputs of the gate that are connected
    // // ArrayList<Integer> inputArray = new ArrayList<>();
    // // ArrayList<Integer> outputArray = new ArrayList<>();
    // // // array of the inputs/outputs bus sizes
    // // ArrayList<Integer> inputBusSizeArray = new ArrayList<>();
    // // ArrayList<Integer> outputBusSizeArray = new ArrayList<>();
    //
    // // // BUG This code can possibly not work in the linkage between external
    // gates
    // // // (input and output).
    // // int i = 0, j = 0;
    // // for (Cable inputCable : gate.getInputCable()) {
    // // if (inputCable != null) {
    // // if (!excludedInputs.contains(inputCable.getInputGate()))
    // // inputArray.add(inputCable.getInputGate().uuid());
    // // else {
    // // inputArray.add(i);
    // // i--;
    // // }
    // // inputBusSizeArray.add(inputCable.getBusSize());
    // // }
    // // }
    // // for (Cable outputCable : gate.getOutputCable()) {
    // // if (outputCable != null) {
    // // if (!excludedOutputs.contains(outputCable.getOutputGate()))
    // // outputArray.add(outputCable.getOutputGate().uuid());
    // // else {
    // // outputArray.add(j);
    // // j--;
    // // }
    // // outputBusSizeArray.add(outputCable.getBusSize());
    // // }
    // // }
    //
    // // // "print" the collected data with the name of the field
    // // gateInfoObject.put("outputTo", outputArray);
    // // gateInfoObject.put("inputFrom", inputArray);
    // // gateInfoObject.put("outputBusSize", outputBusSizeArray);
    // // gateInfoObject.put("inputBusSize", inputBusSizeArray);
    // // gateInfoObject.put("powered", gate.getPowered());
    // // gateInfoObject.put("uuid", gate.uuid());
    // // gateInfoObject.put("type", gate.getClass().getSimpleName());
    // // // special case
    // // if (gate instanceof Schema) {
    // // gateInfoObject.put("filename", name);
    // // }
    // // if (gate instanceof Clock) {
    // // gateInfoObject.put("cycleSpeed", ((Clock) gate).getCycleSpeed());
    // // }
    // // if (gate instanceof Button) {
    // // gateInfoObject.put("inverted", ((Button) gate).getInverted());
    // // gateInfoObject.put("delay", ((Button) gate).getDelay());
    // // }
    // // if (gate instanceof Lever) {
    // // gateInfoObject.put("flipped", ((Lever) gate).getState(0));
    // // }
    // // if (gate instanceof Display) {
    // // gateInfoObject.put("base", ((Display) gate).getBaseOutput());
    // // }
    //
    // gatesArray.add(gateInfoObject);
    // });
    // schemaObject.put("Gates", gatesArray);
    //
    // // save to file
    //
    // PrintWriter writer;
    // try {
    // writer = new PrintWriter("./data/schemas/" + name + "-schema.json", "UTF-8");
    // } catch (UnsupportedEncodingException e) {
    // throw new Error("Invalid charset format", e);
    // } catch (SecurityException e) {
    // throw new Error("Permission denied", e);
    // }
    // writer.println(schemaObject.toString(1));
    // writer.close();
    // }
    //
    // //#endregion

    // //#region load()
    //
    // /**
    // * This function imports data from a saved schema and create the schema gate
    // * accordingly.
    // *
    // * @param name The name of a saved schema.
    // * @throws Exception Throws if the schema fails to load.
    // */
    // public void importSchema(String name) throws Exception {
    // String data = new String(Files.readAllBytes(Paths.get("./data/schemas/" +
    // name + "-schema.json")));
    // JSONObject schemaData = new JSONObject(data);
    // JSONArray schemaGates = schemaData.getJSONArray("Gates");
    //
    // for (int i = 0; i < schemaGates.length(); i++) {
    // JSONObject gateData = schemaGates.getJSONObject(i);
    // Gate newGate = null;
    // ArrayList<Integer> inputBusSize = new ArrayList<>();
    // ArrayList<Integer> outputBusSize = new ArrayList<>();
    //
    // for (int j = 0; j < gateData.getJSONArray("inputBusSize").length(); j++) {
    // inputBusSize.add(gateData.getJSONArray("inputBusSize").getInt(j));
    // }
    // for (int j = 0; j < gateData.getJSONArray("outputBusSize").length(); j++) {
    // outputBusSize.add(gateData.getJSONArray("outputBusSize").getInt(j));
    // }
    //
    // System.out.println(gateData.toString());
    // Integer ploof = !inputBusSize.isEmpty() ? inputBusSize.get(0) :
    // outputBusSize.get(0);
    //
    // switch (gateData.get("type").toString()) {
    // case "And":
    // newGate = new And(ploof);
    // break;
    // case "Not":
    // newGate = new Not(ploof);
    // break;
    // case "Or":
    // newGate = new Or(ploof);
    // break;
    // case "Button":
    // newGate = new (Button)(gateData.getInt("delay"),
    // gateData.getBoolean("inverted"));
    // break;
    // case "Clock":
    // newGate = new Clock(gateData.getLong("cycleSpeed"));
    // break;
    // case "Lever":
    // newGate = new Lever(gateData.getBoolean("flipped"));
    // break;
    // case "Ground":
    // newGate = new Ground(ploof);
    // break;
    // case "Power":
    // newGate = new Power(ploof);
    // break;
    // case "Display":
    // newGate = new Display(ploof, gateData.getInt("base"));
    // break;
    // case "Schema":
    // newGate = new Schema(gateData.getString("filename"));
    // break;
    //
    // default:
    // throw new Error("Unknown gate type: " + gateData.getString("type"));
    // }
    //
    // selectedGates.add(newGate);
    // }
    // }
    //
    // //#endregion

    // #region loadFromJson

    /**
     * Set the schema gate inner circuit and ports from a json selection of gates
     *
     * @param selection
     */
    public final void loadFromJson(JSONObject selection) {
        // input/output gate that are set to -1 will be connected to the schema gate

        try {
            this.innerCircuit.addGatesFromJson(selection, this);
        } catch (Exception e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    // #endregion

    // #region loadFromFile

    /**
     * Load the current schema instance from a given file.
     * BUG check if current instance is empty?
     *
     * @param filePath The path to the saved schema to load.
     */
    public final void loadFromFile(String filePath) {
        try {
            this.innerCircuit.addGatesFromFile(filePath, this);
        } catch (Exception e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    // #endregion

    // #region convertInnerCircuitToCircuit

    /**
     * Format the inner circuit to fit the standard circuit json
     * meaning all schema inner input port will become a Lever or a Numeric gate
     * with an assigned port
     * and all schema inner output port will become a output gate with an assigned
     * port
     *
     * @return
     */
    public Circuit convertInnerCircuitToCircuit() {
        Circuit newCircuit = new Circuit(this.name);

        JSONObject circuit_Json = this.toJson();
        Circuit tempCircuit = new Circuit();

        ArrayList<Input> newInputGates = new ArrayList<>();
        for (int i = 0; i < this.getInputNumber(); i++) {
            newInputGates.add(null);
        }
        ArrayList<Output> newOutputGates = new ArrayList<>();
        for (int i = 0; i < this.getOutputNumber(); i++) {
            newOutputGates.add(null);
        }

        try {
            JSONArray gate_JsonArray = circuit_Json.getJSONArray("Gate");

            // 1 : We create new gates from those we find in the json Object and we set
            // their old uuid as a label
            // 1.1 : the inner gates
            for (int i = 0; i < gate_JsonArray.length(); i++) {
                JSONObject gate_Json = gate_JsonArray.getJSONObject(i);

                String type = gate_Json.getString("type");
                String oldId = String.valueOf(gate_Json.getInt("uuid"));
                // FIXME : needs more info for each gates
                tempCircuit.addNewGate(type, oldId);
            }

            // 1.2 : the new input gates
            for (Cable innerInputCable : this.innerInputCable) {
                try {
                    // setup new Input gate
                    if (innerInputCable.getBusSize() > 1) {
                        // FIXME : set the gate bus size when creating the Numeric gate below
                        // innerInputCable.getBusSize()
                        newInputGates.add(innerInputCable.getInputPort(), ((Numeric) tempCircuit.addNewGate("Numeric")));
                    } else {
                        newInputGates.add(innerInputCable.getInputPort(), ((Lever) tempCircuit.addNewGate("Lever")));
                    }
                    newInputGates.get(innerInputCable.getInputPort()).setSchemaInputPort(innerInputCable.getInputPort());
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            // 1.3 : the new output gates
            for (Cable innerOutputCable : this.innerOutputCable) {
                try {
                    // setup new output gate
                    if (innerOutputCable.getBusSize() > 1) {
                        // FIXME : set the gate bus size when creating the Display gate below
                        // innerOutputCable.getBusSize()
                        newOutputGates.add(innerOutputCable.getOutputPort(), ((Display) tempCircuit.addNewGate("Display")));
                    } else {
                        // TODO : Maybe if we add a size 1 output (like a LED) but that is of now use
                        // right now
                        // newGate = ((Led)newCircuit.addNewGate("Led"));
                        newOutputGates.add(innerOutputCable.getOutputPort(), ((Display) tempCircuit.addNewGate("Display")));
                    }
                    newOutputGates.get(innerOutputCable.getInputPort()).setSchemaOutputPort(innerOutputCable.getOutputPort());
                } catch (Exception e) {
                    System.err.println(e);
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

                    // the target gate is the shéma gate
                    if (targetGateOldId.equals(String.valueOf(this.uuid()))){
                        tempCircuit.connectGate(
                            baseGateOldId,
                            String.valueOf(newOutputGates.get(targetGateInputIndex).uuid()),
                            baseGateOutputIndex,
                            0);
                    }
                    else{
                        // target is a normal gate
                        tempCircuit.connectGate(baseGateOldId, targetGateOldId, baseGateOutputIndex, targetGateInputIndex);
                    }
                }

                JSONArray input_JsonArray = gate_Json.getJSONArray("inputFrom");
                for (int baseGateInputIndex = 0; baseGateInputIndex < input_JsonArray.length(); baseGateInputIndex++) {
                    String targetGateOldId = String.valueOf(input_JsonArray.getJSONArray(baseGateInputIndex).getInt(0));
                    int targetGateOutputIndex = input_JsonArray.getJSONArray(baseGateInputIndex).getInt(1);

                    // the target gate is the shéma gate
                    if (targetGateOldId.equals(String.valueOf(this.uuid()))){
                        tempCircuit.connectGate(
                            String.valueOf(newInputGates.get(targetGateOutputIndex).uuid()),
                            baseGateOldId,
                            0,
                            baseGateInputIndex);
                    }
                }
            }

            // 3 : We fuse the temporary ciruit with the main one
            for (Gate gate : tempCircuit.getAllGates().values()) {
                newCircuit.addGate(gate);
            }
        } catch (Exception e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }

        return newCircuit;
    }

    // #endregion

    // #region toJson

    // TODO : toJson()

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put("circuitPath", this.filePath);

        return json;
    }

    // #endregion

    // #region saveInnerCircuit

    /**
     * Shorthand for {@link #saveInnerCircuit(String filePath)}
     *
     * @throws Exception
     */
    public void saveInnerCircuit() throws Exception {
        this.saveInnerCircuit(this.filePath);
    }

    /**
     * Save the Json of the inner circuit at the given localisation
     * We aim for './data/schema/'
     * The inner circuit is formated to be a circuit
     * i.e : all schéma's port are tranformed into input/output gates
     * We use {@link #convertInnerCircuitToCircuit()}
     *
     * @param filePath
     * @throws Exception
     */
    public void saveInnerCircuit(String filePath) throws Exception {
        // Formating folderPath
        filePath = filePath.replace("/", File.separator);

        // We create the designed file
        try {
            File file = new File(filePath);
            if (file.getParentFile().mkdirs() || file.createNewFile()) {
                System.out.println(String.format("'%s' created", filePath));
            } else {
                System.out.println(String.format("'%s' already exist", filePath));
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

        // We write in the designed file
        try {
            FileWriter writer = new FileWriter(filePath);

            // the inner circuit is adapted to a circuit and printed
            writer.write(this.convertInnerCircuitToCircuit().toJson().toString(1));
            writer.close();

            System.out.println("Schema inner circuit saved with success in: " + filePath);
        } catch (IOException e) {
            System.err.println("Error " + filePath + " can't be saved : " + e.getMessage());
        }
    }

    // #endregion

}
