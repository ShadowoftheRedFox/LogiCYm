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
import com.pjava.src.components.cables.NodeSplitter;
import com.pjava.src.components.input.Button;
import com.pjava.src.components.input.Clock;
import com.pjava.src.components.input.Input;
import com.pjava.src.components.input.Lever;
import com.pjava.src.components.input.Numeric;
import com.pjava.src.components.output.Display;
import com.pjava.src.components.output.Output;
import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.UtilsSave;

/**
 * The schema gate is special as he hold a circuit within himself.
 * His main objective is to connect this inner circuit to the circuit he belongs
 * He act like a bridge.
 */
public class Schema extends Gate {

    // #region Attributes
    /**
     * Name of the schema.
     */
    private String name = "";

    /**
     * File path to a circuit to create the schema
     */
    private String filePath = "";

    /**
     * Gates that are inside the schema.
     */
    private Circuit innerCircuit = new Circuit();

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
     * input that are inside the schema.
     */
    private ArrayList<Cable> innerInputCable = new ArrayList<>();
    /**
     * FIXME javadoc
     * output that are inside the schema.
     */
    private ArrayList<Cable> innerOutputCable = new ArrayList<>();

    // #endregion

    // #region Constructor()

    /**
     * Create a new schema with a generated name from a json selection.
     * The schema's inner circuit is saved in the folder
     * './data/schema/SCHEMA_NAME.json'
     *
     * @param selection schema in json format
     * @throws Exception json format can't be read
     */
    public Schema(JSONObject selection) throws Exception {

        this.loadFromJson(selection);

        this.setNameAndSave(String.format("Schema_%d", this.uuid()));

    }

    /**
     * Create a new schema with the given circuit file json,
     * set his name from the circuit name.
     *
     * @param filePath File path to a circuit to create the schema
     * @throws Exception Throws if the schema fails to save, or name is invalid, or
     *                   gates array is invalid.
     */
    public Schema(String filePath) throws Exception {
        this.loadFromFile(filePath);
        this.setName(this.innerCircuit.getName());
    }

    /**
     * Create a new schema with the given names and a json selection
     * The schema's inner circuit is saved in the folder
     * './data/schema/SCHEMA_NAME.json'
     *
     * @param schemaName The name of the schema file.
     * @param selection  JSON of a selection to create the schema
     * @throws Exception Throws if the schema fails to save, or name is invalid, or
     *                   gates array is invalid.
     */
    public Schema(String schemaName, JSONObject selection) throws Exception {
        this.setName(schemaName);
        this.loadFromJson(selection);

        this.filePath = String.format("%s/schema/%s.json", UtilsSave.saveFolder.toString(), schemaName);
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
    }

    // #endregion

    // #region Getters
    /**
     * Returns the name of the circuit.
     *
     * @return the circuit name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the file path of the circuit.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Returns the inner circuit.
     *
     * @return the inner Circuit object
     */

    public Circuit getInnerCircuit() {
        return innerCircuit;
    }

    /**
     * Returns the list of input cables connected to the inner circuit.
     *
     * @return a list of input cables
     */
    public ArrayList<Cable> getInnerInputCable() {
        return innerInputCable;
    }

    /**
     * Returns the list of output cables connected to the inner circuit.
     *
     * @return a list of output cables
     */
    public ArrayList<Cable> getInnerOutputCable() {
        return innerOutputCable;
    }

    /**
     * Shema has no state, it's just a bridge between the circuit it's in an his
     * inner circuit.
     * {@link Cable#updateState()} should use this bridge when it detect
     * that his output gate is a schema.
     *
     * @deprecated
     * @return Always null.
     */
    @Override
    public BitSet getState() {
        System.err.println(
                "Shema has no state, it's just a bridge between the circuit it's in an his inner circuit.\n 'Cable.updateState()' should use this bridge when it detect that his output gate is a schema");
        return null;
    }


    // #endregion

    // #region Setters

    /**
     * check if the name is correct
     *
     * @param name name of the schema
     * @throws IllegalArgumentException if name is empty
     */
    public final void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        this.name = name;
    }

    // #endregion

    // #region loadFromJson

    /**
     * Set the schema gate inner circuit and ports from a json selection of gates
     *
     * @param selection schema in Json format
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
            this.innerCircuit.loadGatesFromFile(filePath, this);
        } catch (Exception e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    // #endregion

    // #region convertInnerCircuitToCircuit

    /**
     * Format the inner circuit to fit the standard circuit json.
     * meaning all schema inner input port will become a Lever or a Numeric gate
     * with an assigned port,
     * and all schema inner output port will become a output gate with an assigned
     * port.
     *
     * @return the inner Circuit
     */
    public Circuit convertInnerCircuitToCircuit() {
        Circuit newCircuit = new Circuit(this.name);

        // We get the json but some gate will be connected to the schema ports
        JSONObject circuit_Json = this.innerCircuit.toJson();

        Circuit tempCircuit = new Circuit();

        // we set up the new gate that will replace the connections toward the schema
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

                ArrayList<Integer> listToInt = new ArrayList<Integer>();
                JSONArray busSize_JsonArray = gate_Json.getJSONArray("inputBus");
                for (int j = 0; j < busSize_JsonArray.length(); j++) {
                    listToInt.add(busSize_JsonArray.getInt(j));
                }
                int[] sizeBusInput = listToInt.stream().mapToInt(Integer::intValue).toArray();

                listToInt.clear();
                busSize_JsonArray = gate_Json.getJSONArray("outputBus");
                for (int j = 0; j < busSize_JsonArray.length(); j++) {
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
                        if (gate_Json.optBoolean("flipped")) {
                            ((Lever) addedGate).flip();
                        }
                        break;
                    case "Button":
                        long delayFound = gate_Json.optLong("delay", -1);
                        if (delayFound != -1 && delayFound != ((Button) addedGate).getDelay()) {
                            ((Button) addedGate).setDelay(delayFound);
                        }
                        break;
                    case "Clock":
                        long cycleSpeedFound = gate_Json.optLong("cycleSpeed", -1);
                        if (cycleSpeedFound != -1 && cycleSpeedFound != ((Clock) addedGate).getCycleSpeed()) {
                            ((Clock) addedGate).setCycleSpeed(cycleSpeedFound);
                        }
                        break;
                    case "NodeSplitter":
                        int busNumberFound = sizeBusOutput.length;
                        if (busNumberFound > ((NodeSplitter) addedGate).getOutputNumber()) {
                            ((NodeSplitter) addedGate)
                                    .addOutput(busNumberFound - ((NodeSplitter) addedGate).getOutputNumber());
                        }
                        break;
                }
            }

            // 1.2 : the new input gates
            for (Cable innerInputCable : this.innerInputCable) {
                try {
                    // setup new Input gate
                    if (innerInputCable.getBusSize() > 1) {
                        newInputGates.add(
                                innerInputCable.getInputPort(),
                                ((Numeric) tempCircuit.addNewGate(
                                        "Numeric",
                                        null,
                                        new int[] { innerInputCable.getBusSize() })));
                    } else {
                        newInputGates.add(innerInputCable.getInputPort(), ((Lever) tempCircuit.addNewGate("Lever")));
                    }
                    // set the schema port of the new gate
                    newInputGates.get(innerInputCable.getInputPort())
                            .setSchemaInputPort(innerInputCable.getInputPort());
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            // 1.3 : the new output gates
            for (Cable innerOutputCable : this.innerOutputCable) {
                try {
                    // setup new output gate
                    if (innerOutputCable.getBusSize() > 1) {
                        newOutputGates.add(
                                innerOutputCable.getOutputPort(),
                                ((Display) tempCircuit.addNewGate(
                                        "Display",
                                        new int[] { innerOutputCable.getBusSize() },
                                        null)));
                    } else {
                        // TODO : Maybe if we add a size 1 output (like a LED) but this 'if' is of now
                        // use for the moment
                        // newGate = ((Led)newCircuit.addNewGate("Led"));
                        newOutputGates.add(
                                innerOutputCable.getOutputPort(),
                                ((Display) tempCircuit.addNewGate(
                                        "Display",
                                        new int[] { innerOutputCable.getBusSize() },
                                        null)));
                    }
                    newOutputGates.get(innerOutputCable.getInputPort())
                            .setSchemaOutputPort(innerOutputCable.getOutputPort());
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
                    if (targetGateOldId.equals(String.valueOf(this.uuid()))) {
                        tempCircuit.connectGate(
                                baseGateOldId,
                                String.valueOf(newOutputGates.get(targetGateInputIndex).uuid()),
                                baseGateOutputIndex,
                                0);
                    } else {
                        // target is a normal gate
                        tempCircuit.connectGate(baseGateOldId, targetGateOldId, baseGateOutputIndex,
                                targetGateInputIndex);
                    }
                }

                JSONArray input_JsonArray = gate_Json.getJSONArray("inputFrom");
                for (int baseGateInputIndex = 0; baseGateInputIndex < input_JsonArray.length(); baseGateInputIndex++) {
                    String targetGateOldId = String.valueOf(input_JsonArray.getJSONArray(baseGateInputIndex).getInt(0));
                    int targetGateOutputIndex = input_JsonArray.getJSONArray(baseGateInputIndex).getInt(1);

                    // the target gate is the shéma gate
                    if (targetGateOldId.equals(String.valueOf(this.uuid()))) {
                        tempCircuit.connectGate(
                                String.valueOf(newInputGates.get(targetGateOutputIndex).uuid()),
                                baseGateOldId,
                                0,
                                baseGateInputIndex);
                    }
                }
            }

            // 3 : We fuse the temporary ciruit with our new one
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

    /**
     * Converts this schema element to a JSON object.
     *
     * @return a JSONObject representing this schema
     *
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put("name", this.name);
        json.put("circuitPath", this.filePath);

        return json;
    }

    // #endregion

    // #region saveInnerCircuit

    /**
     * rename the schema and save it as './data/schema/NAME.json'
     *
     * @param name name of the schema
     * @throws IllegalArgumentException if name is empty
     */
    public final void setNameAndSave(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        this.name = name;

        try {
            this.saveInnerCircuit(String.format("%s/schema/%s.json", UtilsSave.saveFolder.toString(), this.name));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Shorthand for {@link #saveInnerCircuit(String filePath)}
     * Saves the inner circuit to its current file path.
     *
     * @throws Exception if saving fail
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
     * @param filePath The destination path for the JSON file.
     * @throws Exception If an error occurs while saving the file.
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

            // As the shema has been successfully saved, we set the default file of the
            // schema here
            this.filePath = filePath;
        } catch (IOException e) {
            System.err.println("Error " + filePath + " can't be saved : " + e.getMessage());
        }
    }

    // #endregion

}
