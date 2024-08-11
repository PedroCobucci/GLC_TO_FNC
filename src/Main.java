package src;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
    
        try {
            String filePath = args[0];
            String[][] grammerArray = fileManager.readFile(filePath);
            System.out.println("Primeiro estado: " + grammerArray[0][0]);
            
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
