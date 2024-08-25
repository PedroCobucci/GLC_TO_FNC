package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CNFConverter {

    public List<List<String>> transformToCNF(List<List<String>> grammar) {
        List<List<String>> firstStepRules = new ArrayList<>();
        List<List<String>> newRules = new ArrayList<>();
        Map<String, String> terminalToNewState = new LinkedHashMap<>();
        
        for (List<String> rule : grammar) {
            List<String> newRule = new ArrayList<>();
            String nonTerminal = rule.get(0);
            newRule.add(nonTerminal);

            for (int i = 1; i < rule.size(); i++) {
                String production = rule.get(i);

                if (production.length() == 1 && Character.isLowerCase(production.charAt(0))) {
                    newRule.add(production);
                    continue;
                }

                StringBuilder transformedProduction = new StringBuilder();

                for (char c : production.toCharArray()) {
                    String symbol = String.valueOf(c);

                    if (Character.isLowerCase(c)) {
                        if (!terminalToNewState.containsKey(symbol)) {
                            String newState = symbol.toUpperCase() + "'";
                            terminalToNewState.put(symbol, newState);
                            newRules.add(List.of(newState, symbol));
                        }
                        transformedProduction.append(terminalToNewState.get(symbol));
                    } else {
                        transformedProduction.append(symbol);
                    }
                }

                newRule.add(transformedProduction.toString());
            }

            firstStepRules.add(new ArrayList<>(newRule));
        }

        firstStepRules.addAll(newRules);

        int tCounter = 1; 
        List<List<String>> tRules = new ArrayList<>();
        Map<String, String> restToTStateMap = new LinkedHashMap<>();

        for (List<String> rule : firstStepRules) {
            for (int j = 1; j < rule.size(); j++) {
                String production = rule.get(j);

                while (countSymbols(production) > 2) {
                    String firstSymbol = extractFirstSymbol(production);  
                    String rest = production.substring(firstSymbol.length());  

                    String newTState;
                    if (restToTStateMap.containsKey(rest)) {
                        newTState = restToTStateMap.get(rest);
                    } else {
                        newTState = "T" + tCounter++;
                        restToTStateMap.put(rest, newTState);
                    }

                    production = firstSymbol + newTState;
                    rule.set(j, production);
                }
            }
        }

        for (Map.Entry<String, String> entry : restToTStateMap.entrySet()) {
            List<String> combinedList = new ArrayList<>();
            combinedList.add(entry.getValue());
            combinedList.add(entry.getKey());
            tRules.add(combinedList);
        }

        firstStepRules.addAll(tRules);


        boolean hasChanges = true;

        while (hasChanges) {
            hasChanges = false;
            List<List<String>> newTRules = new ArrayList<>();  

            for (List<String> rule : tRules) {
                for (int j = 1; j < rule.size(); j++) {
                    String production = rule.get(j);

                    while (countSymbols(production) > 2) {
                        hasChanges = true;

                        String firstSymbol = extractFirstSymbol(production);  
                        String rest = production.substring(firstSymbol.length());  
                        String newTState = "T" + tCounter++;

                        newTRules.add(new ArrayList<>(List.of(newTState, rest)));

                        production = firstSymbol + newTState;
                        rule.set(j, production);
                    }
                }
            }

            tRules.addAll(newTRules);

            firstStepRules.addAll(newTRules);
        }

        return firstStepRules;
    }

    private String extractFirstSymbol(String production) {
        if (production.length() > 1 && (production.charAt(1) == '\'' || Character.isDigit(production.charAt(1)))) {
            return production.substring(0, 2);  
        }
        return production.substring(0, 1);  
    }

    private int countSymbols(String production) {
        int count = 0;
        for (int i = 0; i < production.length(); i++) {
            char c = production.charAt(i);
            if (Character.isUpperCase(c)) {
                count++;
            }
        }
        return count;
    }

}
