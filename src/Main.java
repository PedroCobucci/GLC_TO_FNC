package src;
import java.io.IOException;
import java.util.List;
//import FileManager;

public class Main {
    public static void main(String[] args) {
        fileManager fileManager = new fileManager();
        RemoveRecursionStartingRule removeRecursionStartingRule = new RemoveRecursionStartingRule();
        RemoveLambda removeLambda = new RemoveLambda();
        RemoveChain removeChain = new RemoveChain();
    
        try {
            String readFilePath = "glc1.txt";//args[0];
            String writeFilePath = "glc2.txt";//args[1];
            List<List<String>> grammerArray = fileManager.readFile(readFilePath);
            
            

            grammerArray = removeRecursionStartingRule.removeS(grammerArray);
            grammerArray = removeLambda.removeLambda(grammerArray);
            grammerArray = removeChain.removeChainRules(grammerArray);

            grammerArray = new term(grammerArray).removerNaoTerminais();

            grammerArray = new reach(grammerArray).removerNaoAlcancaveis();

            //fnc


            fileManager.writeFile(writeFilePath, grammerArray);
            
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
