package src;

import java.util.List;

public class PrintGrammar {
    public void print(List<List<String>> grammar){
        for (List<String> rule : grammar) {
            System.out.println(rule);
        }
    }
}
