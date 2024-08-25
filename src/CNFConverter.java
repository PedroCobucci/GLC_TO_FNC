import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CNFConverter {

    public List<List<String>> transformToCNF(List<List<String>> grammar) {
        // First Step: Transform terminals accompanied by non-terminals or part of larger terminal-only productions
        List<List<String>> firstStepRules = new ArrayList<>();
        List<List<String>> newRules = new ArrayList<>();
        Map<String, String> terminalToNewState = new HashMap<>();
        
        for (List<String> rule : grammar) {
            List<String> newRule = new ArrayList<>();
            String nonTerminal = rule.get(0);
            newRule.add(nonTerminal);

            for (int i = 1; i < rule.size(); i++) {
                String production = rule.get(i);

                // If the production is a single terminal, add it as it is
                if (production.length() == 1 && Character.isLowerCase(production.charAt(0))) {
                    newRule.add(production);
                    continue;
                }

                // Transform terminal symbols in the production
                StringBuilder transformedProduction = new StringBuilder();

                for (char c : production.toCharArray()) {
                    String symbol = String.valueOf(c);

                    // If the symbol is a terminal and part of a larger production, create a new state
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

        // Combine original rules with new rules, ensuring original rules come first
        firstStepRules.addAll(newRules);

        // Step 2, Part 1: Create T rules for original productions longer than 2
        int tCounter = 1; // Counter for new T states
        List<List<String>> tRules = new ArrayList<>();

        for (List<String> rule : firstStepRules) {
            for (int j = 1; j < rule.size(); j++) {
                String production = rule.get(j);

                // If the production is longer than 2, create a new T rule
                while (countSymbols(production) > 2) {
                    String firstSymbol = extractFirstSymbol(production);  // Extract first symbol
                    String rest = production.substring(firstSymbol.length());  // Rest of the production
                    String newTState = "T" + tCounter++;

                    // Create the new T rule and add it to tRules
                    tRules.add(new ArrayList<>(List.of(newTState, rest)));

                    // Replace the production in the original rule
                    production = firstSymbol + newTState;
                    rule.set(j, production);
                }
            }
        }

        // Add the T rules created in the first pass to firstStepRules
        firstStepRules.addAll(tRules);


        // Step 2, Part 2: Break down the productions within the new T rules if they are longer than 2
        boolean hasChanges = true;

        while (hasChanges) {
            hasChanges = false;
            List<List<String>> newTRules = new ArrayList<>();  // Temporary list for new rules

            for (List<String> rule : tRules) {
                for (int j = 1; j < rule.size(); j++) {
                    String production = rule.get(j);

                    // If the production is longer than 2, break it down
                    while (countSymbols(production) > 2) {
                        hasChanges = true;

                        String firstSymbol = extractFirstSymbol(production);  // Extract first symbol
                        String rest = production.substring(firstSymbol.length());  // Rest of the production
                        String newTState = "T" + tCounter++;

                        // Create the new T rule and add it to newTRules
                        newTRules.add(new ArrayList<>(List.of(newTState, rest)));

                        // Replace the production in the T rule
                        production = firstSymbol + newTState;
                        rule.set(j, production);
                    }
                }
            }

            // Add the new rules to tRules after the iteration to avoid concurrent modification
            tRules.addAll(newTRules);

            // Add newT rules to firstStepRules
            firstStepRules.addAll(newTRules);
        }

        return firstStepRules;
    }

    private String extractFirstSymbol(String production) {
        if (production.length() > 1 && (production.charAt(1) == '\'' || Character.isDigit(production.charAt(1)))) {
            return production.substring(0, 2);  // Handles A', T1, etc.
        }
        return production.substring(0, 1);  // Handles single characters like A, T
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
