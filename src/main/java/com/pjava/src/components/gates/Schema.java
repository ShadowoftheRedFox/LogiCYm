package com.pjava.src.components.gates;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pjava.src.components.Gate;
import com.pjava.src.components.input.Button;
import com.pjava.src.components.input.Clock;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Lever;
import com.pjava.src.components.input.Power;
import com.pjava.src.components.output.Display;

/**
 * The schema gate is a group of invisibale gate. It emulates this group, to
 * enable quick setup of large amount of gates.
 * It can create and load schema, and schemas inside one another is also
 * possible.
 */
public class Schema extends Gate {

    /**
     * Gates that are inside the schema.
     */
    private ArrayList<Gate> selectedGates = new ArrayList<>();
    /**
     * Name of the schema. Will be save under the name "NAME-schema.json".
     */
    private String name;

    /**
     * List of the bus size that compose the input of this gate.
     */
    final private ArrayList<Integer> externalInput = new ArrayList<>();
    /**
     * List of the bus size that compose the output of this gate.
     */
    final private ArrayList<Integer> externalOutput = new ArrayList<>();

    /**
     * Load a schema with the given name.a
     *
     * @param schemaName The name of the schema file.
     * @throws Exception Throws if the schema fails to load.
     */
    public Schema(String schemaName) throws Exception {
        setName(schemaName);
        importSchema(schemaName);
    }

    /**
     * Create a new schema with the given names and gates.
     *
     * @param schemaName The name of the schema file.
     * @param gates      The list of gates to add to the schema.
     * @throws Exception Throws if the schema fails to save, or name is invalid, or
     *                   gates array is invalid.
     */
    public Schema(String schemaName, ArrayList<Gate> gates) throws Exception {
        // TODO cables position
        // TODO position, label, rotation of gates
        // TODO unit tests
        setName(schemaName);
        setGates(gates);
        exportSchema();
        importSchema(schemaName);
    }

    /**
     * Save the current {@link #selectedGates} into the schema with the given
     * {@link #name}.a
     *
     * @throws FileNotFoundException Throws when file handler fails, typicaly
     *                               a wrong name.
     */
    public void exportSchema() throws FileNotFoundException {
        ArrayList<Gate> excludedOutputs = new ArrayList<>();
        ArrayList<Gate> excludedInputs = new ArrayList<>();

        // we get the the input and output of the selected gates, which in the end will
        // be the inputs and outputs of the schema
        selectedGates.forEach(gate -> {
            gate.getInputCable().forEach(cable -> {
                if (cable != null) {
                    if (!selectedGates.contains(cable.getInputGate())) {
                        excludedInputs.add(cable.getInputGate());
                    }
                    if (!selectedGates.contains(cable.getOutputGate())) {
                        excludedOutputs.add(cable.getOutputGate());
                    }
                }
            });
            gate.getOutputCable().forEach(cable -> {
                if (cable != null) {
                    if (!selectedGates.contains(cable.getInputGate())) {
                        excludedInputs.add(cable.getInputGate());
                    }
                    if (!selectedGates.contains(cable.getOutputGate())) {
                        excludedOutputs.add(cable.getOutputGate());
                    }
                }
            });
        });

        // DEBUG
        System.out.println("Schema Inputs\t" + excludedInputs + "\nSchema Gates\t" + selectedGates
                + "\nSchema Outputs\t" + excludedOutputs);

        // get the bus sizes for each outputs and inputs of the schema
        excludedInputs.forEach(gate -> {
            for (int i = 0; i < gate.getOutputNumber(); i++) {
                externalOutput.add(gate.getOutputBus()[i]);
            }
        });
        excludedOutputs.forEach(gate -> {
            for (int i = 0; i < gate.getInputNumber(); i++) {
                externalInput.add(gate.getInputBus()[i]);
            }
        });

        // DEBUG
        System.out.println("Input Count: " + externalInput.size() + "\n\t" + externalInput);
        System.out.println("Output Count: " + externalOutput.size() + "\n\t" + externalOutput);

        // create our json constructor
        JSONObject schemaObject = new JSONObject();
        ArrayList<JSONObject> gatesArray = new ArrayList<>();

        // represent the inputs/outputs bus of the schema
        schemaObject.put("externalInput", externalInput);
        schemaObject.put("externalOutput", externalOutput);

        // save to json format each gate
        selectedGates.forEach(gate -> {
            // json constructor of the gate
            JSONObject gateInfoObject = gate.toJson();
            // // array of inputs/outputs of the gate that are connected
            // ArrayList<Integer> inputArray = new ArrayList<>();
            // ArrayList<Integer> outputArray = new ArrayList<>();
            // // array of the inputs/outputs bus sizes
            // ArrayList<Integer> inputBusSizeArray = new ArrayList<>();
            // ArrayList<Integer> outputBusSizeArray = new ArrayList<>();

            // // BUG This code can possibly not work in the linkage between external gates
            // // (input and output).
            // int i = 0, j = 0;
            // for (Cable inputCable : gate.getInputCable()) {
            //     if (inputCable != null) {
            //         if (!excludedInputs.contains(inputCable.getInputGate()))
            //             inputArray.add(inputCable.getInputGate().uuid());
            //         else {
            //             inputArray.add(i);
            //             i--;
            //         }
            //         inputBusSizeArray.add(inputCable.getBusSize());
            //     }
            // }
            // for (Cable outputCable : gate.getOutputCable()) {
            //     if (outputCable != null) {
            //         if (!excludedOutputs.contains(outputCable.getOutputGate()))
            //             outputArray.add(outputCable.getOutputGate().uuid());
            //         else {
            //             outputArray.add(j);
            //             j--;
            //         }
            //         outputBusSizeArray.add(outputCable.getBusSize());
            //     }
            // }

            // // "print" the collected data with the name of the field
            // gateInfoObject.put("outputTo", outputArray);
            // gateInfoObject.put("inputFrom", inputArray);
            // gateInfoObject.put("outputBusSize", outputBusSizeArray);
            // gateInfoObject.put("inputBusSize", inputBusSizeArray);
            // gateInfoObject.put("powered", gate.getPowered());
            // gateInfoObject.put("uuid", gate.uuid());
            // gateInfoObject.put("type", gate.getClass().getSimpleName());
            // // special case
            // if (gate instanceof Schema) {
            //     gateInfoObject.put("filename", name);
            // }
            // if (gate instanceof Clock) {
            //     gateInfoObject.put("cycleSpeed", ((Clock) gate).getCycleSpeed());
            // }
            // if (gate instanceof Button) {
            //     gateInfoObject.put("inverted", ((Button) gate).getInverted());
            //     gateInfoObject.put("delay", ((Button) gate).getDelay());
            // }
            // if (gate instanceof Lever) {
            //     gateInfoObject.put("flipped", ((Lever) gate).getState(0));
            // }
            // if (gate instanceof Display) {
            //     gateInfoObject.put("base", ((Display) gate).getBaseOutput());
            // }

            gatesArray.add(gateInfoObject);
        });
        schemaObject.put("Gates", gatesArray);

        // save to file

        PrintWriter writer;
        try {
            writer = new PrintWriter("./data/schemas/" + name + "-schema.json", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Error("Invalid charset format", e);
        } catch (SecurityException e) {
            throw new Error("Permission denied", e);
        }
        writer.println(schemaObject.toString(1));
        writer.close();
    }

    /**
     * This function imports data from a saved schema and create the schema gate
     * accordingly.
     *
     * @param name The name of a saved schema.
     * @throws Exception Throws if the schema fails to load.
     */
    public void importSchema(String name) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get("./data/schemas/" + name + "-schema.json")));
        JSONObject schemaData = new JSONObject(data);
        JSONArray schemaGates = schemaData.getJSONArray("Gates");

        for (int i = 0; i < schemaGates.length(); i++) {
            JSONObject gateData = schemaGates.getJSONObject(i);
            Gate newGate = null;
            ArrayList<Integer> inputBusSize = new ArrayList<>();
            ArrayList<Integer> outputBusSize = new ArrayList<>();

            for (int j = 0; j < gateData.getJSONArray("inputBusSize").length(); j++) {
                inputBusSize.add(gateData.getJSONArray("inputBusSize").getInt(j));
            }
            for (int j = 0; j < gateData.getJSONArray("outputBusSize").length(); j++) {
                outputBusSize.add(gateData.getJSONArray("outputBusSize").getInt(j));
            }

            System.out.println(gateData.toString());
            Integer ploof = !inputBusSize.isEmpty() ? inputBusSize.get(0) : outputBusSize.get(0);

            switch (gateData.get("type").toString()) {
                case "And":
                    newGate = new And(ploof);
                    break;
                case "Not":
                    newGate = new Not(ploof);
                    break;
                case "Or":
                    newGate = new Or(ploof);
                    break;
                case "Button":
                    newGate = new Button(gateData.getInt("delay"), gateData.getBoolean("inverted"));
                    break;
                case "Clock":
                    newGate = new Clock(gateData.getLong("cycleSpeed"));
                    break;
                case "Lever":
                    newGate = new Lever(gateData.getBoolean("flipped"));
                    break;
                case "Ground":
                    newGate = new Ground(ploof);
                    break;
                case "Power":
                    newGate = new Power(ploof);
                    break;
                case "Display":
                    newGate = new Display(ploof, gateData.getInt("base"));
                    break;
                case "Schema":
                    newGate = new Schema(gateData.getString("filename"));
                    break;

                default:
                    throw new Error("Unknown gate type: " + gateData.getString("type"));
            }

            selectedGates.add(newGate);
        }
    }

    // #region Getters
    /**
     * Getter for {@link #selectedGates}
     */
    public ArrayList<Gate> getGates() {
        return selectedGates;
    }

    public String getName() {
        return name;
    }

    @Override
    public BitSet getState() {
        return state;
    }
    // #endregion

    // #region Setters
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can be empty");
        }
        this.name = name;
    }

    public void setGates(ArrayList<Gate> array) {
        if (array == null) {
            throw new NullPointerException("Array can't be null");
        }
        for (Gate gate : array) {
            if (gate == null) {
                throw new NullPointerException("Gate can't be null");
            }
        }
        selectedGates = array;
    }
    // #endregion
}


// TODO : schema extend gate
// TODO : Schéma à 2 listes de Cable de plus que les Gates normeaux, pour les connexion 'internes' : input et output
// TODO : cable spéciaux d'entrée et de sortie de schéma

// TODO : override Gate.connect() to connect inner gates with exterior gates.
// TODO : override Gate.updateState() for it to directly
