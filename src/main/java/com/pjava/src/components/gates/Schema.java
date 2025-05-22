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
import com.pjava.src.utils.UtilsSave;


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
     * Create a new schema with a generated name from a json selection.
     * The schema's inner circuit is saved in the folder
     * './data/schema/SCHEMA_NAME.json'
     *
     * @param selection
     * @throws Exception
     */
    public Schema(JSONObject selection) throws Exception {
        this.loadFromJson(selection);
        this.setNameAndSave(String.format("Schema_%d", this.uuid()));
    }

    /**
     * Create a new schema with the given circuit file json,
     * set his name from the circuit name.
     *
     * @param schemaName The name of the schema.
     * @param filePath   File path to a circuit to create the schema
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

        this.filePath = String.format("%sschema/%s.json", UtilsSave.saveFolder.toString(), schemaName);
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

        /**
         * check if the name is correct
         *
         * @param name
         * @throws IllegalArgumentException
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
     * @return
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

    // TODO : toJson()

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
     * @param name
     * @throws IllegalArgumentException
     */
    public final void setNameAndSave(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        this.name = name;

        try {
            this.saveInnerCircuit(String.format("%sschema/%s.json", UtilsSave.saveFolder.toString(), this.name));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

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

            // As the shema has been successfully saved, we set the default file of the schema here
            this.filePath = filePath;
        } catch (IOException e) {
            System.err.println("Error " + filePath + " can't be saved : " + e.getMessage());
        }
    }

    // #endregion

}
