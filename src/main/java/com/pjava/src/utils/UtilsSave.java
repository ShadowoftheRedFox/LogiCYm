package com.pjava.src.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

/**
 * An abstract class used to save,read and find circuit.
 */
public abstract class UtilsSave {

    /**
     * Write sommething in a file
     *
     * @param path    The path to the file
     * @param content The content to put inside the file.
     */
    public static void writeFile(Path path, String content) {
        try {
            byte[] bs = content.getBytes();
            Files.write(path, bs);
        } catch (IOException e) {
            System.err.println("read error: " + e.getMessage());
        }
    }

    /**
     * Read a file
     *
     * @param path the path to the file
     * @return The content of the file, null if error.
     */
    public static List<String> readFile(Path path) {
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
     */
    public static boolean isFileExists(Path path) {
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
     * @return the merged path
     */
    public static Path Merge(Path path, String name) {
        String merge = path + "/" + name;
        Path result = Paths.get(merge);
        return result;
    }

    /**
     * print the list of all the document in the case save
     *
     * @return the iterator of the path to the files, null if error.
     */
    public static Iterator<Path> list(Path path) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }
            return stream.iterator();
        } catch (IOException e) {
            System.out.println("Reading Error: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Path save = Paths.get("H:/Documents/GitHub/LogiCYm/save");
        String name = "simu.txt";
        Path path = Merge(save, name);
        String str = "Poulet";
        isFileExists(path);
        writeFile(path, str);
        readFile(path);
        list(save);
    }
}
