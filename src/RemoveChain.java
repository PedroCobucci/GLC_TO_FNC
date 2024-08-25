package src;
import java.util.*;

public class RemoveChain {

    public List<List<String>> removeChainRules(List<List<String>> grammar) {
        Map<String, Set<String>> chainMap = new LinkedHashMap<>();
        
        for (List<String> rule : grammar) {
            String nonTerminal = rule.get(0);
            chainMap.put(nonTerminal, new HashSet<>());
            chainMap.get(nonTerminal).add(nonTerminal);
        }

        for (List<String> rule : grammar) {
            String nonTerminal = rule.get(0);
            for (String production : rule.subList(1, rule.size())) {
                if (isNonTerminal(production)) {
                    chainMap.get(nonTerminal).add(production);
                }
            }
        }

        for (String nonTerminal : chainMap.keySet()) {
            expandChainSet(nonTerminal, chainMap);
        }

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

    private void expandChainSet(String nonTerminal, Map<String, Set<String>> chainMap) {
        Set<String> expandedSet = new HashSet<>(chainMap.get(nonTerminal));
        for (String chainNonTerminal : chainMap.get(nonTerminal)) {
            if (!chainNonTerminal.equals(nonTerminal)) {
                expandedSet.addAll(chainMap.get(chainNonTerminal));
            }
        }
        chainMap.put(nonTerminal, expandedSet);
    }

    private boolean isNonTerminal(String symbol) {
        return symbol.length() == 1 && Character.isUpperCase(symbol.charAt(0));
    }

}
