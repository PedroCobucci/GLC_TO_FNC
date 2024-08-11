package src;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public List<List<String>> readFile(String filePath) throws IOException {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                stringList.add(line);
            }
        }

        List<List<String>> grammerArray = processRules(stringList.toArray(new String[0]));

        return grammerArray;
    }

    public void writeFile(String filePath, String[] rules) throws IOException {
        // #TODO Implementar o método de escrita
    }


    public List<List<String>> processRules(String[] lines) {
        List<List<String>> grammerMatriz = new ArrayList<>();
        for (String line : lines) {

            String[] rules = line.split("[->|]");
            List<String> processedRules = new ArrayList<>();

            for (String rule : rules){
                if(rule.equals("")){
                    continue;
                }
                processedRules.add(rule.trim());
            };

            grammerMatriz.add(processedRules);
        }

        return  grammerMatriz;
    }
}

