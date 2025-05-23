package com.pjava.src.document;

import com.pjava.src.document.FileReaderSimulation;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                // Ensure the read directory exists
                File readDir = new File(READ_DIRECTORY);
                if (!readDir.exists()) {
                    readDir.mkdirs();
                }
                Path destPath = Paths.get(READ_DIRECTORY + selectedFile.getName());
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
     * @return true if simulation ran successfully
     */
    public static boolean runSimulation(Path filePath) {
        try {
            // Create data table from file
            String[][] data = FileReaderSimulation.createTab(filePath);
            
            if (data.length == 0 || data[0].length == 0) {
                System.err.println("Error: Empty or invalid data in simulation file");
                return false;
            }
            FileReaderSimulation.Simulation(data);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error running simulation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
