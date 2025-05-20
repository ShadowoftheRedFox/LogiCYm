package com.pjava.src.components.gates;

import java.util.ArrayList;
import java.util.BitSet;

import org.json.JSONObject;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Circuit;
import com.pjava.src.components.Gate;
import com.pjava.src.errors.BusSizeException;

/**
 * The schema gate is special as he hold a circuit within himself.
 * His main objective is to connect this inner circuit to the circuit he belongs (outside circuit).
 */
public class Schema extends Gate {

    //#region Attributes

        /**
         * Name of the schema. Will be save under the name "NAME-schema.json".
         */
        private String name;

        /*
         * Extend Gate :
         * private int[] inputBus = new int[]{};
         * private int[] outputBus = new int[]{};
         *
         * private ArrayList<Cable> inputCable = new ArrayList<Cable>();
         * private ArrayList<Cable> outputCable = new ArrayList<Cable>();
        */

        private ArrayList<Cable> innerInputCable = new ArrayList<>();
        private ArrayList<Cable> innerOutputCable = new ArrayList<>();


        // TODO : Quand on sauvegarde un schéma, on sauvegarde le circuit interne du schéma dans un fichier séparé
        // et dans le circuit principal, le gate schéma garde en mémoire l'emplacement de ce circuit et ses port.
        // TODO : connecter un circuit lambda à l'interieur d'un circuit
        /**
         * Gates that are inside the schema.
         */
        private Circuit innerCircuit;

    //#endregion

    //#region Constructor()

        /**
         * Load a schema with the given name.a
         *
         * @param schemaName The name of the schema file.
         * @throws Exception Throws if the schema fails to load.
         */
        public Schema(String schemaName) throws Exception {
            setName(schemaName);
            //importSchema(schemaName);
        }

        //TODO : prendre du json en entrée plutot?
        /**
         * Create a new schema with the given names and gates.
         *
         * @param schemaName The name of the schema file.
         * @param gates      The list of gates to add to the schema.
         * @throws Exception Throws if the schema fails to save, or name is invalid, or
         *                   gates array is invalid.
         */
        public Schema(String schemaName, ArrayList<Gate> gates) throws Exception {
            // ToDo cables position
            // ToDo position, label, rotation of gates
            // ToDo unit tests

            this(schemaName);
            //setGates(gates);
            //exportSchema();
            //importSchema(schemaName);
        }

    //#endregion

    // #region Getters

        // /**
        //  * Getter for {@link #selectedGates}
        //  */
        // public ArrayList<Gate> getGates() {
        //     return selectedGates;
        // }

        // public String getName() {
        //     return name;
        // }

        @Override
        public BitSet getState() {
            return state;
        }

        public Circuit get_innerCircuit(){
            return innerCircuit;
        }

    // #endregion

    // #region Setters

    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can be empty");
        }
        this.name = name;
    }

    // public void setGates(ArrayList<Gate> array) {
    //     if (array == null) {
    //         throw new NullPointerException("Array can't be null");
    //     }
    //     for (Gate gate : array) {
    //         if (gate == null) {
    //             throw new NullPointerException("Gate can't be null");
    //         }
    //     }
    //     selectedGates = array;
    // }

    // #endregion

    // TODO : toJson()


    // //#region save()

    //     // TODO : si le circuit n'existe pas, tous les cables sortant se transforment en gates input/output
    //     /**
    //      * Save the current {@link #selectedGates} into the schema with the given {@link #name}.
    //      *
    //      * @throws FileNotFoundException Throws when file handler fails, typicaly a wrong name.
    //      */
    //     public void save() throws FileNotFoundException {
    //         ArrayList<Gate> excludedOutputs = new ArrayList<>();
    //         ArrayList<Gate> excludedInputs = new ArrayList<>();

    //         // we get the the input and output of the selected gates, which in the end will
    //         // be the inputs and outputs of the schema
    //         /*
    //         selectedGates.forEach(gate -> {
    //             gate.getInputCable().forEach(cable -> {
    //                 if (cable != null) {
    //                     if (!selectedGates.contains(cable.getInputGate())) {
    //                         excludedInputs.add(cable.getInputGate());
    //                     }
    //                     if (!selectedGates.contains(cable.getOutputGate())) {
    //                         excludedOutputs.add(cable.getOutputGate());
    //                     }
    //                 }
    //             });
    //             gate.getOutputCable().forEach(cable -> {
    //                 if (cable != null) {
    //                     if (!selectedGates.contains(cable.getInputGate())) {
    //                         excludedInputs.add(cable.getInputGate());
    //                     }
    //                     if (!selectedGates.contains(cable.getOutputGate())) {
    //                         excludedOutputs.add(cable.getOutputGate());
    //                     }
    //                 }
    //             });
    //         });
    //         */

    //         // get the bus sizes for each outputs and inputs of the schema
    //         excludedInputs.forEach(gate -> {
    //             for (int i = 0; i < gate.getOutputNumber(); i++) {
    //                 externalOutput.add(gate.getOutputBus()[i]);
    //             }
    //         });
    //         excludedOutputs.forEach(gate -> {
    //             for (int i = 0; i < gate.getInputNumber(); i++) {
    //                 externalInput.add(gate.getInputBus()[i]);
    //             }
    //         });

    //         // DEBUG
    //         System.out.println("Input Count: " + externalInput.size() + "\n\t" + externalInput);
    //         System.out.println("Output Count: " + externalOutput.size() + "\n\t" + externalOutput);

    //         // create our json constructor
    //         JSONObject schemaObject = new JSONObject();
    //         ArrayList<JSONObject> gatesArray = new ArrayList<>();

    //         // represent the inputs/outputs bus of the schema
    //         schemaObject.put("externalInput", externalInput);
    //         schemaObject.put("externalOutput", externalOutput);

    //         // save to json format each gate
    //         selectedGates.forEach(gate -> {
    //             // json constructor of the gate
    //             JSONObject gateInfoObject = gate.toJson();
    //             // // array of inputs/outputs of the gate that are connected
    //             // ArrayList<Integer> inputArray = new ArrayList<>();
    //             // ArrayList<Integer> outputArray = new ArrayList<>();
    //             // // array of the inputs/outputs bus sizes
    //             // ArrayList<Integer> inputBusSizeArray = new ArrayList<>();
    //             // ArrayList<Integer> outputBusSizeArray = new ArrayList<>();

    //             // // BUG This code can possibly not work in the linkage between external gates
    //             // // (input and output).
    //             // int i = 0, j = 0;
    //             // for (Cable inputCable : gate.getInputCable()) {
    //             //     if (inputCable != null) {
    //             //         if (!excludedInputs.contains(inputCable.getInputGate()))
    //             //             inputArray.add(inputCable.getInputGate().uuid());
    //             //         else {
    //             //             inputArray.add(i);
    //             //             i--;
    //             //         }
    //             //         inputBusSizeArray.add(inputCable.getBusSize());
    //             //     }
    //             // }
    //             // for (Cable outputCable : gate.getOutputCable()) {
    //             //     if (outputCable != null) {
    //             //         if (!excludedOutputs.contains(outputCable.getOutputGate()))
    //             //             outputArray.add(outputCable.getOutputGate().uuid());
    //             //         else {
    //             //             outputArray.add(j);
    //             //             j--;
    //             //         }
    //             //         outputBusSizeArray.add(outputCable.getBusSize());
    //             //     }
    //             // }

    //             // // "print" the collected data with the name of the field
    //             // gateInfoObject.put("outputTo", outputArray);
    //             // gateInfoObject.put("inputFrom", inputArray);
    //             // gateInfoObject.put("outputBusSize", outputBusSizeArray);
    //             // gateInfoObject.put("inputBusSize", inputBusSizeArray);
    //             // gateInfoObject.put("powered", gate.getPowered());
    //             // gateInfoObject.put("uuid", gate.uuid());
    //             // gateInfoObject.put("type", gate.getClass().getSimpleName());
    //             // // special case
    //             // if (gate instanceof Schema) {
    //             //     gateInfoObject.put("filename", name);
    //             // }
    //             // if (gate instanceof Clock) {
    //             //     gateInfoObject.put("cycleSpeed", ((Clock) gate).getCycleSpeed());
    //             // }
    //             // if (gate instanceof Button) {
    //             //     gateInfoObject.put("inverted", ((Button) gate).getInverted());
    //             //     gateInfoObject.put("delay", ((Button) gate).getDelay());
    //             // }
    //             // if (gate instanceof Lever) {
    //             //     gateInfoObject.put("flipped", ((Lever) gate).getState(0));
    //             // }
    //             // if (gate instanceof Display) {
    //             //     gateInfoObject.put("base", ((Display) gate).getBaseOutput());
    //             // }

    //             gatesArray.add(gateInfoObject);
    //         });
    //         schemaObject.put("Gates", gatesArray);

    //         // save to file

    //         PrintWriter writer;
    //         try {
    //             writer = new PrintWriter("./data/schemas/" + name + "-schema.json", "UTF-8");
    //         } catch (UnsupportedEncodingException e) {
    //             throw new Error("Invalid charset format", e);
    //         } catch (SecurityException e) {
    //             throw new Error("Permission denied", e);
    //         }
    //         writer.println(schemaObject.toString(1));
    //         writer.close();
    //     }

    // //#endregion

    public Cable connectInnerInput(Gate gate, int gateInputIndex, int schemaInnerInputIndex) throws Exception, NullPointerException, IndexOutOfBoundsException, BusSizeException{
        // verifications
        if (gate == null) {
            throw new NullPointerException(
                "Expected arg0 to be an instance of Gate, received null");
        }
        if (gateInputIndex < 0 || gate.getInputBus().length <= gateInputIndex) {
            throw new IndexOutOfBoundsException(
                "Expected 0 <= gateInputIndex < " + gate.getInputBus().length + ", received " + gateInputIndex);
        }


        // set the input bus size at the right port
        if(schemaInnerInputIndex == -1){
            // if the port index is not specified we try to find an unused index
            int indexNotUsed = this.innerInputCable.indexOf(null);
            if(indexNotUsed != -1){
                schemaInnerInputIndex = indexNotUsed;
            }
            else{
                // if we dont find an unused index, we will put it at the end of the ArrayList
                schemaInnerInputIndex = this.innerInputCable.size();
            }
        }
        // The port is now precised, but we need 'schema.inputBus' to have enough place to put the value at the right index
        while(this.inputBus.length <= schemaInnerInputIndex){
            // FIXME : might cause a problem if done wrong (too few or too many index created)
            // we put unused bus size and cable index (hopefully they will be used, they need to be)
            this.addInputBus(1);
            this.innerInputCable.add(null);
            this.inputCable.add(null);

            assert(this.inputBus.length == this.innerInputCable.size() && this.inputBus.length == this.inputCable.size());
        }

        // we set the bus size at the right index 'schemaInnerInputIndex'
        this.inputBus[schemaInnerInputIndex] = gate.getInputBus()[gateInputIndex];

        // We now create a Cable between the schema and the inner gate
        // check if both gate are already linked
        Cable thisInnerInputCable = this.innerInputCable.get(schemaInnerInputIndex);
        Cable gateInputCable = gate.getInputCable().get(gateInputIndex);
        if (thisInnerInputCable != null && gateInputCable != null) {
            if (thisInnerInputCable.equals(gateInputCable)) {
                return thisInnerInputCable;
            } else if (thisInnerInputCable.getBusSize() != gateInputCable.getBusSize()) {
                // incompatible sizes
                return null;
            } else {
                throw new Exception("connection possible but bus allready full");
            }
        } else // check if both cable are empty
        if (thisInnerInputCable == null && gateInputCable == null) {
            Cable result = new Cable(this.outputBus[schemaInnerInputIndex]);
            result.setInputGate(this);
            result.setOutputGate(gate);
            result.setInputPort(schemaInnerInputIndex);
            result.setOutputPort(gateInputIndex);

            this.innerInputCable.set(schemaInnerInputIndex, result);
            gate.getInputCable().set(gateInputIndex, result);

            result.updatePower();
            result.updateState();
            return result;
        } else // if either is null
        if (thisInnerInputCable != null && gateInputCable == null) {
            if (thisInnerInputCable.getOutputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            thisInnerInputCable.setOutputGate(gate);
            gate.getInputCable().set(gateInputIndex, thisInnerInputCable);

            gate.updatePower();
            gate.updateState();
            return thisInnerInputCable;
        } else if (thisInnerInputCable == null && gateInputCable != null) {
            if (gateInputCable.getInputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            gateInputCable.setInputGate(this);
            this.innerInputCable.set(schemaInnerInputIndex, gateInputCable);

            gateInputCable.updatePower();
            gateInputCable.updateState();
            return gateInputCable;
        }

        return null;
    }

    public Cable connectInnerOutput(Gate gate, int gateOutputIndex, int schemaInnerOutputIndex) throws Exception, NullPointerException, IndexOutOfBoundsException, BusSizeException{
        // verifications
        if (gate == null) {
            throw new NullPointerException(
                "Expected arg0 to be an instance of Gate, received null");
        }
        if (gateOutputIndex < 0 || gate.getOutputBus().length <= gateOutputIndex) {
            throw new IndexOutOfBoundsException(
                "Expected 0 <= gateOutputIndex < " + gate.getOutputBus().length + ", received " + gateOutputIndex);
        }


        // set the output bus size at the right port
        if(schemaInnerOutputIndex == -1){
            // if the port index is not specified we try to find an unused index
            int indexNotUsed = this.innerOutputCable.indexOf(null);
            if(indexNotUsed != -1){
                schemaInnerOutputIndex = indexNotUsed;
            }
            else{
                // if we dont find an unused index, we will put it at the end of the ArrayList
                schemaInnerOutputIndex = this.innerOutputCable.size();
            }
        }
        // The port is now precised, but we need 'schema.outputBus' to have enough place to put the value at the right index
         while(this.outputBus.length <= schemaInnerOutputIndex){
            // FIXME : might cause a problem if done wrong (too few or too many index created)
            // we put unused bus size and cable index (hopefully they will be used, they need to be)
            this.addOutputBus(1);
            this.innerOutputCable.add(null);
            this.outputCable.add(null);

            assert(this.outputBus.length == this.innerOutputCable.size() && this.outputBus.length == this.outputCable.size());
        }

        // we set the bus size at the right index 'schemaInnerOutputIndex'
        this.outputBus[schemaInnerOutputIndex] = gate.getOutputBus()[gateOutputIndex];


        // We now create a Cable between the schema and the inner gate
        // check if both gate are already linked
        Cable thisInnerOutputCable = this.innerOutputCable.get(schemaInnerOutputIndex);
        Cable gateOutputCable = gate.getOutputCable().get(gateOutputIndex);
        if (thisInnerOutputCable != null && gateOutputCable != null) {
            if (thisInnerOutputCable.equals(gateOutputCable)) {
                return thisInnerOutputCable;
            } else if (thisInnerOutputCable.getBusSize() != gateOutputCable.getBusSize()) {
                // incompatible sizes
                return null;
            } else {
                throw new Exception("connection possible but bus allready full");
            }
        } else // check if both cable are empty
        if (thisInnerOutputCable == null && gateOutputCable == null) {
            Cable result = new Cable(this.outputBus[schemaInnerOutputIndex]);
            result.setInputGate(gate);
            result.setOutputGate(this);
            result.setInputPort(gateOutputIndex);
            result.setOutputPort(schemaInnerOutputIndex);

            this.innerOutputCable.set(schemaInnerOutputIndex, result);
            gate.getOutputCable().set(gateOutputIndex, result);

            result.updatePower();
            result.updateState();
            return result;
        } else // if either is null
        if (thisInnerOutputCable != null && gateOutputCable == null) {
            if (thisInnerOutputCable.getInputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            thisInnerOutputCable.setInputGate(gate);
            gate.getOutputCable().set(gateOutputIndex, thisInnerOutputCable);

            gate.updatePower();
            gate.updateState();
            return thisInnerOutputCable;
        } else if (thisInnerOutputCable == null && gateOutputCable != null) {
            if (gateOutputCable.getOutputGate() != null) {
                throw new Exception("connection possible but bus allready full");
            }
            gateOutputCable.setOutputGate(this);
            this.innerOutputCable.set(schemaInnerOutputIndex, gateOutputCable);

            gateOutputCable.updatePower();
            gateOutputCable.updateState();
            return gateOutputCable;
        }

        return null;
    }


    /**
     * Set the schema gate inner circuit and ports from a selection of gates
     */
    public void loadFromSelection(JSONObject selection){
        // input/output gate that are set to -1 will be connected to the schema gate
        try {
            this.innerCircuit.addGatesFromJson(selection, this);

        } catch (Exception e) {
            System.err.println("Error circuit can't be launch " + e.getMessage());
        }
    }

    public void loadFromFile(){

    }

    // //#region load()

    //     /**
    //      * This function imports data from a saved schema and create the schema gate
    //      * accordingly.
    //      *
    //      * @param name The name of a saved schema.
    //      * @throws Exception Throws if the schema fails to load.
    //      */
    //     public void importSchema(String name) throws Exception {
    //         String data = new String(Files.readAllBytes(Paths.get("./data/schemas/" + name + "-schema.json")));
    //         JSONObject schemaData = new JSONObject(data);
    //         JSONArray schemaGates = schemaData.getJSONArray("Gates");

    //         for (int i = 0; i < schemaGates.length(); i++) {
    //             JSONObject gateData = schemaGates.getJSONObject(i);
    //             Gate newGate = null;
    //             ArrayList<Integer> inputBusSize = new ArrayList<>();
    //             ArrayList<Integer> outputBusSize = new ArrayList<>();

    //             for (int j = 0; j < gateData.getJSONArray("inputBusSize").length(); j++) {
    //                 inputBusSize.add(gateData.getJSONArray("inputBusSize").getInt(j));
    //             }
    //             for (int j = 0; j < gateData.getJSONArray("outputBusSize").length(); j++) {
    //                 outputBusSize.add(gateData.getJSONArray("outputBusSize").getInt(j));
    //             }

    //             System.out.println(gateData.toString());
    //             Integer ploof = !inputBusSize.isEmpty() ? inputBusSize.get(0) : outputBusSize.get(0);

    //             switch (gateData.get("type").toString()) {
    //                 case "And":
    //                     newGate = new And(ploof);
    //                     break;
    //                 case "Not":
    //                     newGate = new Not(ploof);
    //                     break;
    //                 case "Or":
    //                     newGate = new Or(ploof);
    //                     break;
    //                 case "Button":
    //                     newGate = new (Button)(gateData.getInt("delay"), gateData.getBoolean("inverted"));
    //                     break;
    //                 case "Clock":
    //                     newGate = new Clock(gateData.getLong("cycleSpeed"));
    //                     break;
    //                 case "Lever":
    //                     newGate = new Lever(gateData.getBoolean("flipped"));
    //                     break;
    //                 case "Ground":
    //                     newGate = new Ground(ploof);
    //                     break;
    //                 case "Power":
    //                     newGate = new Power(ploof);
    //                     break;
    //                 case "Display":
    //                     newGate = new Display(ploof, gateData.getInt("base"));
    //                     break;
    //                 case "Schema":
    //                     newGate = new Schema(gateData.getString("filename"));
    //                     break;

    //                 default:
    //                     throw new Error("Unknown gate type: " + gateData.getString("type"));
    //             }

    //             selectedGates.add(newGate);
    //         }
    //     }

    // //#endregion

}


// TODO : schema extend gate
// TODO : Schéma à 2 listes de Cable de plus que les Gates normeaux, pour les connexion 'internes' : input et output
// TODO : cable spéciaux d'entrée et de sortie de schéma

// TODO : override Gate.updateState() for it to directly

// TODO : selection : Un circuit temporaire où on add les gates qui nous interrent. On le passe ensuite en JSON.
// quand des input/output sont connectés à des gates hors selection, leur cable pointe vers -1 au port -1 (car pas encore connecté)
// note: on à besoin de savoir qu'ils etaient connecté avant qu'on les séléctionnes pour creer les ports du schéma.
// on pourrait aussi ne pas prendre cette information en compte et creer un port pour tout les ports libres de la sélection.

// TODO : selection/circuit.json : si des gates input/outputs sont détéctés, ils sont considérés "hors selection" et deviennent des ports du shéma.

// TODO : un circuit schémaJson formaté à des gates normaux à l'exception que les connexions sortante pointe vers le gate -1 (le schéma)
// et le port interieur.
