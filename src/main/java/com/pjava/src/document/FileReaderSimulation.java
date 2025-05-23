package com.pjava.src.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pjava.src.components.Circuit;
import com.pjava.src.components.input.Lever;

public class FileReaderSimulation {

    /**
     * Create a 2D array to store the different values ​​of the file
     *
     * @param path of the file
     * @return the 2D array
     */
    public static String[][] createTab(Path path) {
        List<String[]> data = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                String[] values = line.trim().split(";");
                data.add(values);
            }
            int nbLines = data.size();
            int nbCols = data.get(0).length;

            String[][] columns = new String[nbCols][nbLines];
            for (int i = 0; i < nbLines; i++) {
                for (int j = 0; j < nbCols; j++) {
                    columns[j][i] = data.get(i)[j];
                }
            }
            return columns;

        } catch (IOException e) {
            System.out.println("Error: Cannot read file - " + e.getMessage());
            return new String[0][0];
        }
    }

    /**
     * Create a simulation of the file read with lever and the input give
     *
     * @param the tab create with createTab
     */

    /*
     * public static void Simulation(String[][] tab){
     *
     * Map<String, Lever> levers = new HashMap<>();
     * for (int i = 0; i < tab.length; i++) {
     * String name = tab[i][0];
     * Lever lever = new Lever();
     * levers.put(name, lever);
     * }
     * System.out.println(levers);
     *
     * int maxCols = 0;
     * for (String[] row : tab) {
     * maxCols = Math.max(maxCols, row.length);
     * }
     *
     * for(int j=1; j< maxCols;j++){
     * System.out.println("Step " + j);
     * for(int i=0; i<tab.length;i++){
     * String name = tab[i][0];
     * Lever lever = levers.get(name);
     *
     * String value = tab[i][j];
     * if ("1".equals(value)) {
     *
     * lever.flip();
     * System.out.println("Lever" + name + ": "+ lever.getState(0));
     * lever.flip();
     * }
     * else{
     * System.out.println("Lever" + name + ": "+ lever.getState(0));
     * }
     * }
     * }
     * }
     */
    /*  */
    public static void Simulation(String[][] tab) throws Exception {
        Circuit circuit = null;
        ArrayList<Lever> listeInput = new ArrayList<>();
        // listeInput = circuit.getLeverGate();
        // String path =
        // "H:/Documents/GitHub/LogiCYm/src/main/java/com/pjava/src/document/Read/";
        for (int i = 0; i < tab.length; i++) {
            String name = tab[i][0];
        }

    }

    /**
     * Displays the lever values
     */
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("H:/Documents/GitHub/LogiCYm/src/main/java/com/pjava/src/document/Read/simu.txt");
        String[][] tab = createTab(path);

        for (String[] col : tab) {
            System.out.println(Arrays.toString(col));
        }
        Simulation(tab);
    }
}
