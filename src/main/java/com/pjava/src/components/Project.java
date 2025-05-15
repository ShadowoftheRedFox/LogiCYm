package com.pjava.src.components;

import com.pjava.src.components.input.Ground;
import com.pjava.src.components.input.Power;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Project {
    private ArrayList<Element> elements = new ArrayList<>();
    
    /**
     * Ajoute un élément au projet
     */
    public void addElement(Element element) {
        if (!elements.contains(element)) {
            elements.add(element);
        }
    }
    
    /**
     * Sauvegarde le projet dans un fichier JSON
     * @param filePath Chemin du fichier de sauvegarde
     * @return true si la sauvegarde a réussi, false sinon
     */
    public boolean saveProject(String filePath) {
        try {
            JSONObject root = new JSONObject();
            JSONArray elementsArray = new JSONArray();
            
            // Sauvegarde de tous les éléments
            for (Element element : elements) {
                elementsArray.put(element.toJSON());
            }
            
            root.put("elements", elementsArray);
            
            // Écriture dans le fichier
            FileWriter writer = new FileWriter(filePath);
            writer.write(root.toString(2)); // Indentation de 2 espaces
            writer.close();
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving project: " + e.getMessage());
            return false;
        }
    }
    public static void test(String[] args){
        Power p1 = new Power();
        // Ground p1 = new Ground();
        // Power p2 = new Power();
        Ground p2 = new Ground();

        boolean a = p1.getState(0);
        boolean b = p2.getState(0);

        And and = new And();
        Not not = new Not();
        try{
            and.connect(not);
        }
        catch(Error e){
            
        }

        Project project = new Project();
        project.addElement(p1);
        project.addElement(p2);
        project.addElement(and);
        project.addElement(not);

        String filePath = "save.json";
        boolean success = project.saveProject(filePath);
        
        if (success) {
            System.out.println("Circuit saved to " + filePath);
        } else {
            System.err.println("Failed to save circuit");
        }
    }
}