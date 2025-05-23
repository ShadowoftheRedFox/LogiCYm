package com.pjava.src.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


 //An abstract class used to save,read and find circuit.
public abstract class UtilsSave {

    /**
     * Extension of the save files
     */
    public static final String saveExtension = ".json";

    /**
     * Path to the save folder.
     */
    public static final Path saveFolder = Paths.get("./data");

    /**
     * Write sommething in a file
     *
     * @param path    The path to the file
     * @param content The content to put inside the file.
     */
    public static void writeFile(Path path, String content) {
        if (path == null) {
            return;
        }
        if (content == null) {
            content = "";
        }
        try {
            byte[] bs = content.getBytes();
            Files.write(path, bs);
        } catch (IOException e) {
            System.err.println("read error: " + e.getMessage());
        }
    }

    /**
     * Make the directory of the given path.
     *
     * @param path The path to create.
     * @return The success.
     */
    public static boolean mkdir(Path path) {
        if (path == null) {
            return false;
        }
        try {
            return path.toFile().mkdirs();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Read a file
     *
     * @param path the path to the file
     * @return The content of the file, null if error.
     */
    public static List<String> readFile(Path path) {
        if (path == null) {
            return null;
        }
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                System.out.println(line);
            }
            return lines;
        } catch (IOException e) {
            System.out.println("Reading Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Search a file name in a case
     *
     * @param path Check if the given file exists
     * @return a boolean if File exist
     */
    public static boolean isFileExists(Path path) {
        if (path == null) {
            return false;
        }
        if (Files.exists(path)) {
            System.out.println("File exists");
            return true;
        } else {
            System.out.println("The file does not exist");
            return false;
        }
    }

    /**
     * Merge a name of field and the path for create create,read or verif the field
     * 
     * @param path The base directory path.
     * @param name The name of the file or subdirectory to merge.
     * @return The resulting merged path.
     */
    public static Path Merge(Path path, String name) {
        return Paths.get(path.toString(), name);
    }

    /**
     * print the list of all the document in the case save
     * @param path The directory to list.
     * @return A list of file paths in the directory, or null if an error occurs.
     */
    public static ArrayList<Path> list(Path path) {
        if (path == null) {
            return null;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            ArrayList<Path> array = new ArrayList<Path>();
            for (Path entry : stream) {
                // System.out.println(entry.getFileName());
                array.add(entry);
            }
            return array;
        } catch (IOException | IllegalStateException e) {
            System.out.println("Reading Error: " + e.getMessage());
            return null;
        }
    }
    /**
     * Entry point for testing file operations:
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Path save = Paths.get("./data");
        String name = "simu.txt";
        Path path = Merge(save, name);
        String str = "Poulet";
        isFileExists(path);
        writeFile(path, str);
        readFile(path);
        list(save);
    }
}
