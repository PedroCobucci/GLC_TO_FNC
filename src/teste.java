package src;

import java.util.ArrayList;
import java.util.List;

public class teste {
        public static void main(String[] args) {
        // GLC para teste
        List<List<String>> gramatica = new ArrayList<>();
        gramatica.add(List.of("S", "aA", "BA"));
        gramatica.add(List.of("A", "a", "BC", "AC"));
        gramatica.add(List.of("B", "b", "BA"));
        gramatica.add(List.of("C", "cC"));
        //gramatica.add(List.of("D", "aB"));

        
        //term transformer = new term(gramatica);
        //List<List<String>> gramaticafnc = transformer.removerNaoTerminais();

       // reach remocaoNaoAlcancaveis = new reach(gramatica);
       // List<List<String>>gramaticafnc = remocaoNaoAlcancaveis.removerNaoAlcancaveis();

        fnc transformaFnc = new fnc();
        List<List<String>> gramaticafnc = transformaFnc.converterParaFNC(gramatica);




        for (List<String> regra : gramaticafnc) {
            System.out.println(regra);
        }
    }
}
