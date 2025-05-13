package com.pjava.src.utils;

import java.io.IOException;
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
 */
        public static void writeFile(Path path,String content){
        try{
            byte[] bs = content.getBytes();
            Files.write(path,bs);
        }
        catch (IOException e){
            System.err.println("read error: " + e.getMessage());
        }
    }

    /**
 * Read a file
 */
        public static void readFile(Path path){
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Reading Error: " + e.getMessage());
        }
    }  

    /**
 * Shearch a fild name in a case 
 */
    
    public static void verif(Path path){
        if(Files.exists(path)) System.out.println("File exists");
        else System.out.println("The file does not exist");
    }

    /**
 * Merge a name of field and the path for create create,read or verif the field
 * @return the merge path
 */
    public static Path Merge(Path path, String name){
        String merge = path + "/" + name;
        Path path2 = Paths.get(merge);
        return path2;
    }
/**
 * print the list of all the document in the case save
 */

    public static void list(Path path){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }}
        catch(IOException e){
            System.out.println("Reading Error: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        Path save = Paths.get("H:/Documents/GitHub/LogiCYm/save");
        String name = "simu.txt";
        Path path = Merge(save,name);
        String str = "Poulet";
        verif(path);
        writeFile(path,str);
        readFile(path); 
        list(save);
    }   
}
