package br.edu.unb.tppe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.text.Normalizer;

public class Curador {
    public List<Registro> processarEUnificar(List<Registro> registros) {
        if (registros == null) {
            return List.of();
        }
        return normalizarGrafia(registros);
    }

    public String removerAcentos(String palavra){
        String resultado = palavra.replaceAll("[´`’]", "'");
        return Normalizer.normalize(resultado, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
    }

    public String definirNomeCorreto(List<String> nomes){
        // Considero que o nome correto é aquele que tem a maior quantidade de caracteres, pois é o mais completo.
        String maiorNome = "";
        for(String n : nomes){
            String nomeNormalizado = Normalizer.normalize(n, Normalizer.Form.NFKD);
            if(nomeNormalizado.length() > maiorNome.length()) maiorNome = n;
        }

        return maiorNome.replaceAll("[´`’]", "'");
    }

    // Caso 1
    /*
        acontece quando há diferença na codificação utilizada ou na grafia dos nomes. São exemplos desses erros
        presença/ausência de acentuação no mesmo caractere em registros diferentes, uso de acentuação diferente
        representar o mesmo item (apóstrofo, crase ou acento agudo), presença / ausência de cedilha ou acentuação,
        vários outros.
    */
    public List<Registro> normalizarGrafia(List<Registro> registros){
        // conjunto de todas as variações possíveis para um nome
        HashMap<String, List<String>> mapaNomes = new HashMap<>();

        HashMap<String, String> mapaNomesCorretos = new HashMap<>();

        // criando o mapa com as variações dos nomes
        for(Registro r : registros){
            String nome = r.getNome();
            String nomeLimpo = removerAcentos(nome);
            mapaNomes.computeIfAbsent(nomeLimpo, k -> new ArrayList<>()).add(nome);
        }

        // escolhendo os nomes corretos entre as variações possíveis
        for(String chave : mapaNomes.keySet()){
            String nomeCorreto = definirNomeCorreto(mapaNomes.get(chave));
            mapaNomesCorretos.computeIfAbsent(chave, k -> nomeCorreto);
        }

        // corrigindo os nomes para cada registro
        for(Registro r : registros){
            String nomeLimpo = removerAcentos(r.getNome());
            r.setNome(mapaNomesCorretos.get(nomeLimpo));
        }

        return registros.stream().
                distinct().
                toList();
    }
}