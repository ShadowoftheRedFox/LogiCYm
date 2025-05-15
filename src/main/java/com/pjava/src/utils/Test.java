package com.pjava.src.utils;

import java.io.*;
import java.util.*;
import org.json.*;


// TODO ====================================


// Interface pour tous les composants de circuit_bis

interface Component_bis {
    boolean getState(int pin);
    void connect(Component_bis component_bis);
    void connect(Component_bis component_bis, int fromPin, int toPin);
    String getType();
    List<Connection_bis> getConnection_biss();
    String getId(); // Identifiant unique pour la référence JSON
}

class Test{
    public Test(){

    }
 
    public static void main(String[] args){
        System.out.println(String.format("bonjour le monde"));
    }
}


    // Classe de base pour les composants
abstract class BaseComponent_bis implements Component_bis {
    protected List<Connection_bis> connection_biss = new ArrayList<>();
    protected String id;
    
    public BaseComponent_bis() {
        // Générer un identifiant unique
        this.id = UUID.randomUUID().toString();
    }
    
    @Override
    public List<Connection_bis> getConnection_biss() {
        return connection_biss;
    }
    
    @Override
    public void connect(Component_bis component_bis) {
        // Par défaut, connecte le pin 0 au pin 0
        connect(component_bis, 0, 0);
    }
    
    @Override
    public void connect(Component_bis component_bis, int fromPin, int toPin) {
        connection_biss.add(new Connection_bis(this, component_bis, fromPin, toPin));
    }
    
    @Override
    public String getId() {
        return id;
    }
}

// Classe Connection_bis pour stocker les informations de connexion
class Connection_bis {
    private Component_bis source;
    private Component_bis target;
    private int sourcePin;
    private int targetPin;
    
    public Connection_bis(Component_bis source, Component_bis target, int sourcePin, int targetPin) {
        this.source = source;
        this.target = target;
        this.sourcePin = sourcePin;
        this.targetPin = targetPin;
    }
    
    public Component_bis getSource() { return source; }
    public Component_bis getTarget() { return target; }
    public int getSourcePin() { return sourcePin; }
    public int getTargetPin() { return targetPin; }
    
    @Override
    public String toString() {
        return source.getType() + "[" + sourcePin + "] -> " + target.getType() + "[" + targetPin + "]";
    }
    
    // Convertir la connexion en JSONObject
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("sourceId", source.getId());
        json.put("targetId", target.getId());
        json.put("sourcePin", sourcePin);
        json.put("targetPin", targetPin);
        return json;
    }
}

// Composants spécifiques du circuit_bis
class Power_bis extends BaseComponent_bis {
    @Override
    public boolean getState(int pin) {
        return true; // Toujours vrai (1)
    }
    
    @Override
    public String getType() {
        return "Power_bis";
    }
    
    // Convertir en JSONObject
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("type", getType());
        return json;
    }
}

class Ground_bis extends BaseComponent_bis {
    @Override
    public boolean getState(int pin) {
        return false; // Toujours faux (0)
    }
    
    @Override
    public String getType() {
        return "Ground_bis";
    }
    
    // Convertir en JSONObject
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("type", getType());
        return json;
    }
}

class And_bis extends BaseComponent_bis {
    private Component_bis input1;
    private Component_bis input2;
    private int input1Pin = 0;
    private int input2Pin = 0;
    
    public void setInputs(Component_bis input1, Component_bis input2) {
        this.input1 = input1;
        this.input2 = input2;
    }
    
    public void setInputs(Component_bis input1, int pin1, Component_bis input2, int pin2) {
        this.input1 = input1;
        this.input2 = input2;
        this.input1Pin = pin1;
        this.input2Pin = pin2;
    }
    
    @Override
    public boolean getState(int pin) {
        if (input1 == null || input2 == null) {
            return false;
        }
        return input1.getState(input1Pin) && input2.getState(input2Pin);
    }
    
    @Override
    public String getType() {
        return "And_bis";
    }
    
    // Convertir en JSONObject
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("type", getType());
        return json;
    }
}

class Not_bis extends BaseComponent_bis {
    private Component_bis input;
    private int inputPin = 0;
    
    public void setInput(Component_bis input) {
        this.input = input;
    }
    
    public void setInput(Component_bis input, int pin) {
        this.input = input;
        this.inputPin = pin;
    }
    
    @Override
    public boolean getState(int pin) {
        if (input == null) {
            return true;
        }
        return !input.getState(inputPin);
    }
    
    @Override
    public String getType() {
        return "Not_bis";
    }
    
    // Convertir en JSONObject
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("type", getType());
        return json;
    }
}

// Classe Circuit_bis pour gérer l'ensemble du circuit_bis
class Circuit_bis {
    private List<Component_bis> component_biss = new ArrayList<>();
    private Map<String, Component_bis> component_bissById = new HashMap<>();
    
    public void addComponent_bis(Component_bis component_bis) {
        component_biss.add(component_bis);
        component_bissById.put(component_bis.getId(), component_bis);
    }
    
    public List<Component_bis> getComponent_biss() {
        return component_biss;
    }
    
    public Component_bis getComponent_bisById(String id) {
        return component_bissById.get(id);
    }
    
    // Fonction pour convertir le circuit_bis en JSON
    public JSONObject toJSON() {
        JSONObject circuit_bisJson = new JSONObject();
        
        // Ajouter tous les composants
        JSONArray component_bissArray = new JSONArray();
        for (Component_bis component_bis : component_biss) {
            JSONObject component_bisJson = null;
            
            if (component_bis instanceof Power_bis) {
                component_bisJson = ((Power_bis) component_bis).toJSON();
            } else if (component_bis instanceof Ground_bis) {
                component_bisJson = ((Ground_bis) component_bis).toJSON();
            } else if (component_bis instanceof And_bis) {
                component_bisJson = ((And_bis) component_bis).toJSON();
            } else if (component_bis instanceof Not_bis) {
                component_bisJson = ((Not_bis) component_bis).toJSON();
            }
            
            if (component_bisJson != null) {
                component_bissArray.put(component_bisJson);
            }
        }
        circuit_bisJson.put("component_biss", component_bissArray);
        
        // Ajouter toutes les connexions
        JSONArray connection_bissArray = new JSONArray();
        for (Component_bis component_bis : component_biss) {
            for (Connection_bis connection_bis : component_bis.getConnection_biss()) {
                connection_bissArray.put(connection_bis.toJSON());
            }
        }
        circuit_bisJson.put("connection_biss", connection_bissArray);
        
        return circuit_bisJson;
    }
    
    // Fonction pour sauvegarder le circuit_bis dans un fichier JSON
    public static void saveCircuit_bisAsJson(Circuit_bis circuit_bis, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            JSONObject circuit_bisJson = circuit_bis.toJSON();
            writer.write(circuit_bisJson.toString(2)); // Indentation de 2 espaces pour lisibilité
            System.out.println("Circuit_bis sauvegardé avec succès en JSON dans " + fileName);
            
            // Afficher le chemin absolu
            File file = new File(fileName);
            System.out.println("Chemin absolu du fichier : " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du circuit_bis en JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Fonction pour charger un circuit_bis à partir d'un fichier JSON
    public static Circuit_bis loadCircuit_bisFromJson(String fileName) {
        try {
            // Lire le fichier JSON
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }
            
            // Parser le JSON
            JSONObject circuit_bisJson = new JSONObject(content.toString());
            Circuit_bis circuit_bis = new Circuit_bis();
            
            // Créer d'abord tous les composants
            JSONArray component_bissArray = circuit_bisJson.getJSONArray("component_biss");
            for (int i = 0; i < component_bissArray.length(); i++) {
                JSONObject component_bisJson = component_bissArray.getJSONObject(i);
                String type = component_bisJson.getString("type");
                String id = component_bisJson.getString("id");
                
                Component_bis component_bis = null;
                switch (type) {
                    case "Power_bis":
                        Power_bis power_bis = new Power_bis();
                        ReflectionUtil_bis.setField(power_bis, "id", id);
                        component_bis = power_bis;
                        break;
                    case "Ground_bis":
                        Ground_bis ground_bis = new Ground_bis();
                        ReflectionUtil_bis.setField(ground_bis, "id", id);
                        component_bis = ground_bis;
                        break;
                    case "And_bis":
                        And_bis and_bis = new And_bis();
                        ReflectionUtil_bis.setField(and_bis, "id", id);
                        component_bis = and_bis;
                        break;
                    case "Not_bis":
                        Not_bis not_bis = new Not_bis();
                        ReflectionUtil_bis.setField(not_bis, "id", id);
                        component_bis = not_bis;
                        break;
                }
                
                if (component_bis != null) {
                    circuit_bis.addComponent_bis(component_bis);
                }
            }
            
            // Puis établir les connexions
            JSONArray connection_bissArray = circuit_bisJson.getJSONArray("connection_biss");
            for (int i = 0; i < connection_bissArray.length(); i++) {
                JSONObject connection_bisJson = connection_bissArray.getJSONObject(i);
                String sourceId = connection_bisJson.getString("sourceId");
                String targetId = connection_bisJson.getString("targetId");
                int sourcePin = connection_bisJson.getInt("sourcePin");
                int targetPin = connection_bisJson.getInt("targetPin");
                
                Component_bis source = circuit_bis.getComponent_bisById(sourceId);
                Component_bis target = circuit_bis.getComponent_bisById(targetId);
                
                if (source != null && target != null) {
                    source.connect(target, sourcePin, targetPin);
                }
            }
            
            System.out.println("Circuit_bis chargé avec succès depuis JSON: " + fileName);
            return circuit_bis;
        } catch (IOException | JSONException e) {
            System.err.println("Erreur lors du chargement du circuit_bis depuis JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

// Utilitaire pour modifier des champs privés par réflexion
class ReflectionUtil_bis {
    public static void setField(Object object, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = object.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}




