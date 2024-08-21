package src;

import java.util.ArrayList;
import java.util.List;

public class reach {
    private List<List<String>> gramatica;

    public reach(List<List<String>> gramatica) {
        this.gramatica = gramatica;
    }

    public List<List<String>> removerNaoAlcancaveis() {
        List<String> regrasAlcancaveis = new ArrayList<>();
        regrasAlcancaveis.add("S");

        boolean mudou = true;
        while (mudou) {
            mudou = false;
            for (List<String> regra : gramatica) {
                String variavel = regra.get(0);
                if (regrasAlcancaveis.contains(variavel)) {
                    for (int i = 1; i < regra.size(); i++) {
                        String geracao = regra.get(i);
                        for (char c : geracao.toCharArray()) {
                            if (Character.isUpperCase(c) && !regrasAlcancaveis.contains(String.valueOf(c))) {
                                regrasAlcancaveis.add(String.valueOf(c));
                                mudou = true;
                            }
                        }
                    }
                }
            }
        }

        List<List<String>> novaGramatica = new ArrayList<>();
        for (List<String> regra : gramatica) {
            String variavel = regra.get(0);
            if (regrasAlcancaveis.contains(variavel)) {
                novaGramatica.add(regra);
            }
        }
        return novaGramatica;
    }

}
