/*
package com.pjava.src.utils;

import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;
import java.util.*;

public class CircuitJsonManager {
    public static void main(String[] args) {
        // Exemple de création de circuit_bis comme dans votre code
        Circuit_bis circuit_bis = new Circuit();
        
        Power p1 = new Power();
        Ground p2 = new Ground();
        And and_bis = new And();
        Not not_bis = new Not();
        
        // Ajouter les composants au circuit_bis
        circuit_bis.addComponent(p1);
        circuit_bis.addComponent(p2);
        circuit_bis.addComponent(and_bis);
        circuit_bis.addComponent(not_bis);
        
        // Connecter les composants
        and_bis.connect(not_bis);
        
        // Connecter p1 et p2 à and_bis
        p1.connect(and_bis, 0, 0);
        p2.connect(and_bis, 0, 1);
        
        // Sauvegarder le circuit_bis en JSON
        Circuit_bis.saveCircuit_bisAsJson(circuit_bis, "mon_circuit_bis.json");
        
        // Charger le circuit_bis depuis JSON
        Circuit_bis loadedCircuit_bis = Circuit_bis.loadCircuit_bisFromJson("mon_circuit_bis.json");
        
        // Vérifier que le circuit_bis chargé fonctionne comme prévu
        if (loadedCircuit_bis != null) {
            List<Component> components = loadedCircuit_bis.getComponents();
            System.out.println("Composants chargés: " + components.size());
            
            // Afficher les connexions
            for (Component component : components) {
                System.out.println("Composant: " + component.getType() + " (ID: " + component.getId() + ")");
                for (Connection conn : component.getConnections()) {
                    System.out.println(" - " + conn);
                }
            }
        }
    }
}
*/

