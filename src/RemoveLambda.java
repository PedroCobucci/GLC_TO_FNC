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

    //Determine the set of nullable variables
    private Set<String> determineNullableVariables(List<List<String>> grammar) {
        Set<String> nullable = new HashSet<>();
        Set<String> prevNullable;

        // Initialize nullable with variables that directly produce lambda
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
        System.out.println("Nullable Variables: " + nullable);
        return nullable;
    }

    // Check if a production is nullable
    private boolean isNullableProduction(String production, Set<String> nullable) {
        for (char symbol : production.toCharArray()) {
            if (!nullable.contains(String.valueOf(symbol))) {
                return false;
            }
        }
        return true;
    }

    //Add rules by omitting nullable variables
    private List<List<String>> addNewRules(List<List<String>> grammar, Set<String> nullable) {
        List<List<String>> newRules = new ArrayList<>();
        
        // Identify the initial rule
        String initialRule = grammar.get(0).get(0); // Assumes the first rule is the initial rule
        
        for (List<String> rule : grammar) {
            String variable = rule.get(0);
            List<String> newProductions = new ArrayList<>();
    
            // Add original productions
            for (int i = 1; i < rule.size(); i++) {
                String production = rule.get(i);
                newProductions.add(production);
    
                // Add productions omitting nullable variables
                addNullableCombinations(production, nullable, newProductions);
            }
    
            // Ensure lambda is included in the initial rule
            if (variable.equals(initialRule)) {
                if (!newProductions.contains(".")) {
                    newProductions.add(".");
                }
            }
    
            // Remove duplicates
            List<String> newRule = new ArrayList<>();
            newRule.add(variable);
            newRule.addAll(new HashSet<>(newProductions)); // Remove duplicates before adding to newRules
            newRules.add(newRule);
        }
    
        return newRules;
    }
    

    // Generate and add all combinations omitting nullable variables
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

    //Remove the lambda rules
    private List<List<String>> removeLambdaRules(List<List<String>> grammar, Set<String> nullable) {
        List<List<String>> updatedGrammar = new ArrayList<>();

        // Flag to track the first iteration
        boolean isFirstIteration = true;
        
        for (List<String> rule : grammar) {
            String variable = rule.get(0);
            List<String> newProductions = new ArrayList<>();
        
            // Skip the first iteration because it's initial symbol
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

    // public static void main(String[] args) {
    //     List<List<String>> grammar1 = new ArrayList<>();

    //     //S'	→	S
    //     //S	→	BSA	|	A
    //     //A	→	aA	|	λ
    //     //B	→	Bba	|	λ

    //     //NULL = {A, B, S, S'}
        
    //     grammar1.add(new ArrayList<>(List.of("S'", "S")));
    //     grammar1.add(new ArrayList<>(List.of("S", "BSA", "A")));
    //     grammar1.add(new ArrayList<>(List.of("A", "aA", ".")));
    //     grammar1.add(new ArrayList<>(List.of("B", "Bba", ".")));

    //     RemoveLambda remover = new RemoveLambda();
    //     List<List<String>> newGrammar = remover.removeLambda(grammar1);

    //     System.out.println("New Grammar");
    //     for (List<String> rule : newGrammar) {
    //         System.out.println(rule);
    //     }
    // }
}
