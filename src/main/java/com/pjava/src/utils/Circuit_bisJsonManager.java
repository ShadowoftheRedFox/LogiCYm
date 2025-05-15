
package com.pjava.src.utils;

import java.util.*;

public class Circuit_bisJsonManager {
    public static void main(String[] args) {
        // Exemple de création de circuit_bis comme dans votre code
        Circuit_bis circuit_bis = new Circuit_bis();
        
        Power_bis p1 = new Power_bis();
        Ground_bis p2 = new Ground_bis();
        And_bis and = new And_bis();
        Not_bis not = new Not_bis();
        
        // Ajouter les composants au circuit_bis
        circuit_bis.addComponent_bis(p1);
        circuit_bis.addComponent_bis(p2);
        circuit_bis.addComponent_bis(and);
        circuit_bis.addComponent_bis(not);
        
        // Connecter les composants
        and.connect(not);
        
        // Connecter p1 et p2 à and
        p1.connect(and, 0, 0);
        p2.connect(and, 0, 1);
        
        // Sauvegarder le circuit_bis en JSON
        Circuit_bis.saveCircuit_bisAsJson(circuit_bis, "H:/Documents/GitHub/LogiCYm/save/mon_circuit_bis.json");
        
        // Charger le circuit_bis depuis JSON
        Circuit_bis loadedCircuit_bis = Circuit_bis.loadCircuit_bisFromJson("H:/Documents/GitHub/LogiCYm/save/mon_circuit_bis.json");
        
        // Vérifier que le circuit_bis chargé fonctionne comme prévu
        if (loadedCircuit_bis != null) {
            List<Component_bis> components = loadedCircuit_bis.getComponent_biss();
            System.out.println("Composants chargés: " + components.size());
            
            // Afficher les connexions
            for (Component_bis component : components) {
                System.out.println("Composant: " + component.getType() + " (ID: " + component.getId() + ") \n");
                for (Connection_bis conn : component.getConnection_biss()) {
                    System.out.println(" - " + conn);
                }
            }
        }
    }
}


