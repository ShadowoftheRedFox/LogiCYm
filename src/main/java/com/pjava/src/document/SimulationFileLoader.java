package com.pjava.src.document;

import com.pjava.src.document.FileReaderSimulation;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.pjava.src.components.Circuit;
import com.pjava.src.components.Gate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Utility class to handle input file loading for simulation
 */
public class SimulationFileLoader {
    
    /**
     * Directory where simulation files will be saved
     */
    private static final String READ_DIRECTORY = "src/main/java/com/pjava/src/document/Read/";
    
    /**
     * Opens a file chooser dialog to select a CSV file for simulation
     * @param stage The parent stage for the dialog
     * @return The path to the saved file, or null if canceled
     */
    public static Path loadSimulationFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Simulation Input File");
        
        // Set file extensions filter
        FileChooser.ExtensionFilter csvFilter = 
            new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
        FileChooser.ExtensionFilter txtFilter = 
            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(csvFilter, txtFilter);
        
        // Show the dialog
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                // Ensure the read directory exists
                File readDir = new File(READ_DIRECTORY);
                if (!readDir.exists()) {
                    readDir.mkdirs();
                }
                
                // Create destination path
                Path destPath = Paths.get(READ_DIRECTORY + selectedFile.getName());
                
                // Copy the file to our read directory
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                
                System.out.println("File saved to: " + destPath);
                return destPath;
                
            } catch (IOException e) {
                System.err.println("Error saving simulation file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    /**
     * Loads and runs a simulation from the specified file path
     * @param filePath Path to the simulation file
     * @param circuit The circuit to associate with the simulation
     * @return FileReaderSimulation instance if successful, null otherwise
     */
    public static FileReaderSimulation startSimulation(Path filePath,Circuit circuit) throws Exception {
        try {
            // Create data table from file
            String[][] data = FileReaderSimulation.createTab(filePath);
            
            if (data.length == 0 || data[0].length == 0) {
                System.err.println("Error: Missing input data");
                return null;
                
            }
            FileReaderSimulation newSimulation = new FileReaderSimulation();
            newSimulation.maxStep = data[0].length-1;
            newSimulation.circuit=circuit;
            newSimulation.simulationActivated=true;

            for(int i = 0; i< data.length ; i++){
              String leverName = data[i][0];
              ArrayList<Boolean> leverStates = new ArrayList<>();

                for(int step = 1; step< data[i].length ; step++){
                    boolean state = !"0".equals(data[i][step]);
                    leverStates.add(state);
                }
                newSimulation.leverSimulator.put(leverName, leverStates);
            }
            return newSimulation;
            
        } catch (Exception e) {
            System.err.println("Error running simulation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
