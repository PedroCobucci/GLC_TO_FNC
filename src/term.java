package src;

import java.util.ArrayList;
import java.util.List;

public class term {
    private List<List<String>> gramatica;

    public term(List<List<String>> gramatica) {
        this.gramatica = gramatica;
    }

    public List<List<String>> removerNaoTerminais() {
        List<List<String>> gramaticafnc = remocaoNaoTerminais(gramatica);

        return gramaticafnc;
    }

    private List<List<String>> remocaoNaoTerminais(List<List<String>> gramatica) {
        List<String> terminaisDiretos = new ArrayList<>();

        // Identificar se geram terminais diretamente ou indiretamente
        boolean mudou = true;
        while (mudou) {
            mudou = false;
            for (List<String> regra : gramatica) {
                String variavel = regra.get(0);
                if (terminaisDiretos.contains(variavel)) continue;

                for (int i = 1; i < regra.size(); i++) {
                    String geracao = regra.get(i);
                    boolean geraTerminal = true;

                    // Verifica se gera terminal
                    for (char c : geracao.toCharArray()) {
                        if (Character.isUpperCase(c) && !terminaisDiretos.contains(String.valueOf(c))) {
                            geraTerminal = false;
                            break;
                        }
                    }

                    if (geraTerminal) {
                        terminaisDiretos.add(variavel);
                        mudou = true;
                        break;
                    }
                }
            }
        }

        // Gera a nova gramática apenas com geradoras de terminais
        List<List<String>> novaGramatica = new ArrayList<>();
        for (List<String> regra : gramatica) {
            String variavel = regra.get(0);
            if (terminaisDiretos.contains(variavel)) {
                novaGramatica.add(regra);
            }
        }

        return novaGramatica;
    }
}
