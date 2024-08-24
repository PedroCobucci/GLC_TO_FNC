package src;
import java.io.IOException;
import java.util.List;
//import FileManager;

public class Main {
    public static void main(String[] args) {
        fileManager fileManager = new fileManager();
    
        try {
            String readFilePath = args[0];
            String writeFilePath = args[1];
            List<List<String>> grammerArray = fileManager.readFile(readFilePath);
            fileManager.writeFile(writeFilePath, grammerArray);
            //System.out.println("Primeiro estado: " + grammerArray.get(0).get(0));
            
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
