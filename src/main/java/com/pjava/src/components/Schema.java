package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Schema extends Gate {

    private ArrayList<Gate> selectedGates = new ArrayList<Gate>();
    private String name;

    private ArrayList<Integer> externalInput = new ArrayList<>();
    private ArrayList<Integer> externalOutput = new ArrayList<>();

    public Schema(String schemaName) {
        name = schemaName;
    }

    public Schema(String schemaName, ArrayList<Gate> gates) {
        name = schemaName;
        selectedGates = gates;
    }

    public void exportSchema() throws FileNotFoundException, UnsupportedEncodingException {
        ArrayList<Gate> excludedOutputs = new ArrayList<Gate>();
        ArrayList<Gate> excludedInputs = new ArrayList<Gate>();
        selectedGates.forEach(gate -> {
            gate.getInputCable().forEach(cable -> {
                if (!selectedGates.contains(cable.inputGate))
                    excludedInputs.add(cable.inputGate);
                if (!selectedGates.contains(cable.outputGate))
                    excludedOutputs.add(cable.outputGate);
            });
            gate.getOutputCable().forEach(cable -> {
                if (!selectedGates.contains(cable.inputGate))
                    excludedInputs.add(cable.inputGate);
                if (!selectedGates.contains(cable.outputGate))
                    excludedOutputs.add(cable.outputGate);
            });
        });
        System.out.println("Schema Inputs\t" + excludedInputs + "\nSchema Gates\t" + selectedGates
                + "\nSchema Outputs\t" + excludedOutputs);

        excludedInputs.forEach(gate -> {
            for (int i = 0; i < gate.getOutputNumber(); i++) {
                try {
                    externalOutput.add(gate.getOutputBus()[i]);
                } catch (IndexOutOfBoundsException e) {
                    throw new Error(e);
                }
            }
        });

        excludedOutputs.forEach(gate -> {
            for (int i = 0; i < gate.getInputNumber(); i++) {
                try {
                    externalInput.add(gate.getInputBus()[i]);
                } catch (IndexOutOfBoundsException e) {
                    throw new Error(e);
                }
            }
        });

        System.out.println("Input Count: " + externalInput.size() + "\n\t" + externalInput);
        System.out.println("Output Count: " + externalOutput.size() + "\n\t" + externalOutput);

        JSONObject schemaObject = new JSONObject();
        ArrayList<JSONObject> gatesArray = new ArrayList<JSONObject>();

        schemaObject.put("externalInput", externalInput);
        schemaObject.put("externalOutput", externalOutput);
        selectedGates.forEach(gate -> {
            JSONObject gateInfoObject = new JSONObject();
            ArrayList<Integer> inputArray = new ArrayList<Integer>();
            ArrayList<Integer> outputArray = new ArrayList<Integer>();
            ArrayList<Integer> inputBusSizeArray = new ArrayList<Integer>();
            ArrayList<Integer> outputBusSizeArray = new ArrayList<Integer>();

            int i = 0, j = 0;
            // BUG This code can possibly not work in the linkage between external gates
            // (input and output).
            for (Cable inputCable : gate.getInputCable()) {
                if (!excludedInputs.contains(inputCable.getInputGate()))
                    inputArray.add(inputCable.getInputGate().uuid());
                else {
                    inputArray.add(i);
                    i--;
                }
                inputBusSizeArray.add(inputCable.getBusSize());
            }
            for (Cable outputCable : gate.getOutputCable()) {
                if (!excludedOutputs.contains(outputCable.getOutputGate()))
                    outputArray.add(outputCable.getOutputGate().uuid());
                else {
                    outputArray.add(j);
                    j--;
                }
                outputBusSizeArray.add(outputCable.getBusSize());
            }

            gateInfoObject.put("outputTo", outputArray);
            gateInfoObject.put("inputFrom", inputArray);
            gateInfoObject.put("outputBusSize", outputBusSizeArray);
            gateInfoObject.put("inputBusSize", inputBusSizeArray);
            gateInfoObject.put("powered", gate.getPowered());
            gateInfoObject.put("uuid", gate.uuid());
            gateInfoObject.put("type", gate.getClass().getSimpleName());

            gatesArray.add(gateInfoObject);
        });
        schemaObject.put("Gates", gatesArray);
        System.out.println(schemaObject.toString(1));

        PrintWriter writer = new PrintWriter("./data/schemas/" + name + "-schema.json", "UTF-8");
        writer.println(schemaObject.toString(1));
        writer.close();
    }

    public Schema importSchema(String name) throws IOException {
        String data = new String(Files.readAllBytes(Paths.get("./data/schemas/" + name + "-schema.json")));
        JSONObject schemaData = new JSONObject(data);
        JSONArray schemaGates = schemaData.getJSONArray("Gates");

        System.out.println(schemaGates);

        for (int i = 0; i < schemaGates.length(); i++) {
            JSONObject gateData = schemaGates.getJSONObject(i);
            switch (gateData.get("type").toString()) {
                case "And":

                    break;
                case "Not":

                    break;
                case "Or":

                    break;
                case "Button":

                    break;
                case "Clock":

                    break;
                case "Ground":

                    break;
                case "Input":

                    break;
                case "Lever":

                    break;
                case "Power":

                    break;
                case "Display":

                    break;

                default:
                    break;
            }
        }

        return null;
    }

    @Override
    public BitSet getState() {
        return state;
    }
}
