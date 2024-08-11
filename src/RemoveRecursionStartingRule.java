package src;

import java.util.ArrayList;
import java.util.List;

public class RemoveRecursionStartingRule {

    public List<List<String>> removeS(List<List<String>> grammar) {
        boolean containsS = false;

        for (List<String> rule : grammar) {
            for (int i = 1; i < rule.size(); i++) {
                String production = rule.get(i);
                if (productionHasS(production)) {
                    containsS = true;
                    break;
                }
            }
            if (containsS) {
                break;
            }
        }

        if (containsS) {
            addNewSRule(grammar);
        }

        return grammar;
    }

    private boolean productionHasS(String production) {
        return production.contains("S");
    }

    private void addNewSRule(List<List<String>> grammar) {
        List<String> newSPrimeRule = new ArrayList<>();
        newSPrimeRule.add("S'");
        newSPrimeRule.add("S");
        grammar.add(0, newSPrimeRule);
    }
}
