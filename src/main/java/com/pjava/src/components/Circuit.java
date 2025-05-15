package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import org.json.JSONObject;

import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.Cyclic;

import com.pjava.src.components.gates.Or;
import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;

import com.pjava.src.components.input.Input;
import com.pjava.src.components.output.Output;

public class Circuit{

    /**
    * List of all the gates of a circuit
    */
    public ArrayList<Gate> listGate = new ArrayList<Gate>();

    // /**
    // * List of all the input gates of a circuit
    // */
    // private ArrayList<Input> circuitInput = new ArrayList<Input>();

    // /**
    // * List of all the output gates of a circuit
    // */
    // private ArrayList<Output> circuitOutput = new ArrayList<Output>();

    public Circuit(){
    }

    /**
     *
     * @param type A string of the gate type to be created
     * @retrun The gate that has just been created
     */
    public Gate addGate(String type) throws Error{
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
        listGate.add(newGate);

        return newGate;
    }

    public void startSimulation(){
    }

    public static void saveCircuit(Circuit circuit,String fileName){

    }
    // public static void saveCircuit(Circuit_bis circuit_bis, String fileName) {
    //     try (FileWriter writer = new FileWriter(fileName)) {
    //         JSONObject circuit_bisJson = circuit_bis.toJson();
    //         writer.write(circuit_bisJson.toString(2)); // Indentation de 2 espaces pour lisibilité
    //         System.out.println("Circuit_bis sauvegardé avec succès en JSON dans " + fileName);

    //         // Afficher le chemin absolu
    //         File file = new File(fileName);
    //         System.out.println("Chemin absolu du fichier : " + file.getAbsolutePath());
    //     } catch (IOException e) {
    //         System.err.println("Erreur lors de la sauvegarde du circuit_bis en JSON: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    // }


    // public void loadCircuit(String pathFile){

    //     // Lire le fichier JSON
    //         StringBuilder content = new StringBuilder();
    //         try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
    //             String line;
    //             while ((line = reader.readLine()) != null) {
    //                 content.append(line);
    //             }
    //         }
    // }
    // public void loadCircuit(JSonObject save) {
    //     try{


    //         // Parser le JSON
    //         JSONObject circuit_bisJson = new JSONObject(content.toString());
    //         Circuit_bis circuit_bis = new Circuit_bis();

    //         // Créer d'abord tous les composants
    //         JSONArray component_bissArray = circuit_bisJson.getJSONArray("component_biss");
    //         for (int i = 0; i < component_bissArray.length(); i++) {
    //             JSONObject component_bisJson = component_bissArray.getJSONObject(i);
    //             String type = component_bisJson.getString("type");
    //             String id = component_bisJson.getString("id");

    //             Component_bis component_bis = null;
    //             switch (type) {
    //                 case "Power_bis":
    //                     Power_bis power_bis = new Power_bis();
    //                     ReflectionUtil_bis.setField(power_bis, "id", id);
    //                     component_bis = power_bis;
    //                     break;
    //                 case "Ground_bis":
    //                     Ground_bis ground_bis = new Ground_bis();
    //                     ReflectionUtil_bis.setField(ground_bis, "id", id);
    //                     component_bis = ground_bis;
    //                     break;
    //                 case "And_bis":
    //                     And_bis and_bis = new And_bis();
    //                     ReflectionUtil_bis.setField(and_bis, "id", id);
    //                     component_bis = and_bis;
    //                     break;
    //                 case "Not_bis":
    //                     Not_bis not_bis = new Not_bis();
    //                     ReflectionUtil_bis.setField(not_bis, "id", id);
    //                     component_bis = not_bis;
    //                     break;
    //             }

    //             if (component_bis != null) {
    //                 circuit_bis.addComponent_bis(component_bis);
    //             }
    //         }

    //         // Puis établir les connexions
    //         JSONArray connection_bissArray = circuit_bisJson.getJSONArray("connection_biss");
    //         for (int i = 0; i < connection_bissArray.length(); i++) {
    //             JSONObject connection_bisJson = connection_bissArray.getJSONObject(i);
    //             String sourceId = connection_bisJson.getString("sourceId");
    //             String targetId = connection_bisJson.getString("targetId");
    //             int sourcePin = connection_bisJson.getInt("sourcePin");
    //             int targetPin = connection_bisJson.getInt("targetPin");

    //             Component_bis source = circuit_bis.getComponent_bisById(sourceId);
    //             Component_bis target = circuit_bis.getComponent_bisById(targetId);

    //             if (source != null && target != null) {
    //                 source.connect(target, sourcePin, targetPin);
    //             }
    //         }

    //         System.out.println("Circuit_bis chargé avec succès depuis JSON: " + fileName);
    //         return circuit_bis;
    //     } catch (IOException | JSONException e) {
    //         System.err.println("Erreur lors du chargement du circuit_bis depuis JSON: " + e.getMessage());
    //         e.printStackTrace();
    //         return null;
    //     }
    // }
}
