package src;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class fileManager {

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

        grammerArray = checkRepeatedState(grammerArray);

        return grammerArray;
    }

    public void writeFile(String filePath, List<List<String>> grammerArray) throws IOException {
        //grammerArray = checkRepeatedState(grammerArray);
        String result = "";

        for (List<String> grammer : grammerArray) {
            int index = 0;
            for (String rule : grammer) {
                if (index == 0) {
                   result = result + "" + rule + " -> ";
                }else{
                   result = result + "" + rule + " | ";
                }
                index++;
            }
            result = result.substring(0, result.length() - 2);
            result = result + "\r\n";
            
        }

        System.out.println(result);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<List<String>> checkRepeatedState(List<List<String>> grammarArray) {
        Map<String, List<String>> hash = new LinkedHashMap<>();

        for (List<String> grammar : grammarArray) {
            String key = grammar.get(0);
            List<String> values = grammar.subList(1, grammar.size());

            if (hash.containsKey(key)) {
                hash.get(key).addAll(values);
            } else {
                hash.put(key, new ArrayList<>(values));
            }
        }

        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : hash.entrySet()) {
            List<String> combinedList = new ArrayList<>();
            combinedList.add(entry.getKey());
            combinedList.addAll(entry.getValue());
            result.add(combinedList);
        }

        return result;
    }

    private List<List<String>> processRules(String[] lines) {
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

