package src;
import java.util.*;

public class RemoveChain {

    public List<List<String>> removeChainRules(List<List<String>> grammar) {
        // A map to store the chain derivations for each non-terminal
        Map<String, Set<String>> chainMap = new HashMap<>();
        
        // Initialize chain sets for each non-terminal
        for (List<String> rule : grammar) {
            String nonTerminal = rule.get(0);
            chainMap.put(nonTerminal, new HashSet<>());
            chainMap.get(nonTerminal).add(nonTerminal);
        }

        // Populate the chain sets
        for (List<String> rule : grammar) {
            String nonTerminal = rule.get(0);
            for (String production : rule.subList(1, rule.size())) {
                if (isNonTerminal(production)) {
                    chainMap.get(nonTerminal).add(production);
                }
            }
        }

        // Expand chain sets to include indirect derivations
        for (String nonTerminal : chainMap.keySet()) {
            expandChainSet(nonTerminal, chainMap);
        }

        // Create a new grammar without chain rules
        List<List<String>> newGrammar = new ArrayList<>();
        for (List<String> rule : grammar) {
            String nonTerminal = rule.get(0);
            List<String> newRule = new ArrayList<>();
            newRule.add(nonTerminal);

            Set<String> expandedProductions = new HashSet<>();
            for (String chainNonTerminal : chainMap.get(nonTerminal)) {
                for (List<String> r : grammar) {
                    if (r.get(0).equals(chainNonTerminal)) {
                        for (String production : r.subList(1, r.size())) {
                            if (!isNonTerminal(production) || production.equals(".")) {
                                expandedProductions.add(production);
                            }
                        }
                    }
                }
            }
            newRule.addAll(expandedProductions);
            newGrammar.add(newRule);
        }

        return newGrammar;
    }

    // Recursive method to expand the chain set to include all reachable non-terminals
    private void expandChainSet(String nonTerminal, Map<String, Set<String>> chainMap) {
        Set<String> expandedSet = new HashSet<>(chainMap.get(nonTerminal));
        for (String chainNonTerminal : chainMap.get(nonTerminal)) {
            if (!chainNonTerminal.equals(nonTerminal)) {
                expandedSet.addAll(chainMap.get(chainNonTerminal));
            }
        }
        chainMap.put(nonTerminal, expandedSet);
    }

    // Helper method to check if a string is a non-terminal (assumed to be a single uppercase letter)
    private boolean isNonTerminal(String symbol) {
        return symbol.length() == 1 && Character.isUpperCase(symbol.charAt(0));
    }

    // public static void main(String[] args) {
    //     List<List<String>> grammar = Arrays.asList(
    //         Arrays.asList("S", "AS", "A"),
    //         Arrays.asList("A", "aA", "bB", "C"),
    //         Arrays.asList("B", "bB", "b"),
    //         Arrays.asList("C", "cC", "B")
    //     );

    //     RemoveChain removeChain = new RemoveChain();
    //     List<List<String>> newGrammar = removeChain.removeChainRules(grammar);

    //     System.out.println("Grammar without chain rules:");
    //     for (List<String> rule : newGrammar) {
    //         System.out.println(rule);
    //     }
    // }
}
