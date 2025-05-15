
// package com.pjava.src.utils;

// import com.pjava.src.components.gates.And;
// import com.pjava.src.components.gates.Not;
// import com.pjava.src.components.input.Ground;
// import com.pjava.src.components.input.Power;

// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.ObjectInputStream;
// import java.io.ObjectOutputStream;
// import java.util.List;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.DirectoryStream;
// import java.nio.file.Files;
// /**
//  * An abstract class used to save,read and find circuit.
//  */
// public abstract class UtilsSave {

// /**
//  * Write sommething in a file
//  */
//         public static void writeFile(Path path,String content){
//         try{
//             byte[] bs = content.getBytes();
//             Files.write(path,bs);
//         }
//         catch (IOException e){
//             System.err.println("read error: " + e.getMessage());
//         }
//     }

//     /**
//  * Read a file
//  */
//         public static void readFile(Path path){
//         try {
//             List<String> lines = Files.readAllLines(path);
//             for (String line : lines) {
//                 System.out.println(line);
//             }
//         } catch (IOException e) {
//             System.out.println("Reading Error: " + e.getMessage());
//         }
//     }  

//     /**
//  * Shearch a fild name in a case 
//  */
    
//     public static void verif(Path path){
//         if(Files.exists(path)) System.out.println("File exists");
//         else System.out.println("The file does not exist");
//     }

//     /**
//  * Merge a name of field and the path for create create,read or verif the field
//  * @return the merge path
//  */
//     public static Path Merge(Path path, String name){
//         String merge = path + "/" + name;
//         Path path2 = Paths.get(merge);
//         return path2;
//     }

//     public static String Merge2(String path, String name){
//         String merge = path + "/" + name;
//         return merge;
//     }
// /**
//  * print the list of all the document in the case save
//  */

//     public static void list(Path path){
//         try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
//             for (Path entry : stream) {
//                 System.out.println(entry.getFileName());
//             }}
//         catch(IOException e){
//             System.out.println("Reading Error: " + e.getMessage());
//         }
//     }

//     public static void saveCircuit(Circuit circuit, String fileName) {
//         try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
//             oos.writeObject(circuit);
//             System.out.println(fileName + "Save with succes");
//         } catch (IOException e) {
//             System.err.println("Error in the save" + e.getMessage());
//             e.printStackTrace();
//         }
//     }


//     public static Circuit loadCircuit(String fileName) {
//         try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
//             Circuit circuit = (Circuit) ois.readObject();
//             System.out.println("Load with succes " + fileName);
//             return circuit;
//         } catch (IOException | ClassNotFoundException e) {
//             System.err.println("Error in the save" + e.getMessage());
//             e.printStackTrace();
//             return null;
//         }
//     }

//     public static void main(String[] args) throws Exception {
//         Path save = Paths.get("H:/Documents/GitHub/LogiCYm/save");
//         String filePath = "H:/Documents/GitHub/LogiCYm/save";
//         String name = "simu.txt";
//         Path path = Merge(save, name);
//         String filName = Merge2(filePath, name);
//         String str = "Poulet";

//         System.out.println(filName);

//         verif(path);
//         list(save);

//         Power p1 = new Power();
//         // Ground p1 = new Ground();
//         // Power p2 = new Power();
//         Ground p2 = new Ground();

//         boolean a = p1.getState(0);
//         boolean b = p2.getState(0);

//         And and = new And();
//         Not not = new Not();
//         and.connect(not);

//     }
// }
