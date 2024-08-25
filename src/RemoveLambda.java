package src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveLambda {

    public List<List<String>> removeLambda(List<List<String>> grammar) {
        Set<String> nullable = determineNullableVariables(grammar);
        grammar = addNewRules(grammar, nullable);
        grammar = removeLambdaRules(grammar, nullable);
        return grammar;
    }

    private Set<String> determineNullableVariables(List<List<String>> grammar) {
        Set<String> nullable = new HashSet<>();
        Set<String> prevNullable;

        do {
            prevNullable = new HashSet<>(nullable);

            for (List<String> rule : grammar) {
                String variable = rule.get(0);

                for (int i = 1; i < rule.size(); i++) {
                    String production = rule.get(i);
                    if (production.equals(".") || isNullableProduction(production, nullable)) {
                        nullable.add(variable);
                        break;
                    }
                }
            }
        } while (!nullable.equals(prevNullable));
        return nullable;
    }

    private boolean isNullableProduction(String production, Set<String> nullable) {
        for (char symbol : production.toCharArray()) {
            if (!nullable.contains(String.valueOf(symbol))) {
                return false;
            }
        }
        return true;
    }

    private List<List<String>> addNewRules(List<List<String>> grammar, Set<String> nullable) {
        List<List<String>> newRules = new ArrayList<>();
        
        String initialRule = grammar.get(0).get(0); 
        
        for (List<String> rule : grammar) {
            String variable = rule.get(0);
            List<String> newProductions = new ArrayList<>();
    
            for (int i = 1; i < rule.size(); i++) {
                String production = rule.get(i);
                newProductions.add(production);
    
                addNullableCombinations(production, nullable, newProductions);
            }
    
            if (variable.equals(initialRule)) {
                if (!newProductions.contains(".")) {
                    newProductions.add(".");
                }
            }
    
            List<String> newRule = new ArrayList<>();
            newRule.add(variable);
            newRule.addAll(new HashSet<>(newProductions)); 
            newRules.add(newRule);
        }
    
        return newRules;
    }
    

    private void addNullableCombinations(String production, Set<String> nullable, List<String> newProductions) {
        int n = production.length();
        for (int i = 1; i < (1 << n); i++) {
            StringBuilder newProduction = new StringBuilder();
            boolean valid = true;

            for (int j = 0; j < n; j++) {
                char symbol = production.charAt(j);
                if ((i & (1 << j)) == 0 || !nullable.contains(String.valueOf(symbol))) {
                    newProduction.append(symbol);
                }
            }

            if (valid && newProduction.length() > 0 && !newProductions.contains(newProduction.toString())) {
                newProductions.add(newProduction.toString());
            }
        }
    }

    private List<List<String>> removeLambdaRules(List<List<String>> grammar, Set<String> nullable) {
        List<List<String>> updatedGrammar = new ArrayList<>();

        boolean isFirstIteration = true;
        
        for (List<String> rule : grammar) {
            String variable = rule.get(0);
            List<String> newProductions = new ArrayList<>();
        
            if (isFirstIteration) {
                updatedGrammar.add(new ArrayList<>(rule));
                isFirstIteration = false;
                continue;
            }
        
            for (int i = 1; i < rule.size(); i++) {
                String production = rule.get(i);
                if (!production.equals(".")) {
                    newProductions.add(production);
                }
            }
        
            List<String> newRule = new ArrayList<>();
            newRule.add(variable);
            newRule.addAll(newProductions);
            updatedGrammar.add(newRule);
        }
        
        return updatedGrammar;
    }

}
