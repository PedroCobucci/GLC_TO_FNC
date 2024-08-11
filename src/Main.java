package src;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
    
        try {
            String filePath = args[0];
            List<List<String>> grammerArray = fileManager.readFile(filePath);
            System.out.println("Primeiro estado: " + grammerArray.get(0).get(0));
            
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
