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
        // Primeiro limpa acentos e grafia (Caso 1)
        List<Registro> posGrafia = normalizarGrafia(registros);
        
        // Agora agrupa as iniciais (Caso 4) e retorna
        return unificarIniciaisAgrupadas(posGrafia);
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


    // Caso 4
    
    public List<Registro> unificarIniciaisAgrupadas(List<Registro> registros) {
        List<Registro> resultado = new ArrayList<>();

        for (Registro atual : registros) {
            boolean duplicado = false;

            for (int i = 0; i < resultado.size(); i++) {
                Registro salvo = resultado.get(i);

                if (saoMesmaPessoaIniciais(atual.getNome(), salvo.getNome())) {
                    duplicado = true;
                    // Padrão-Ouro: mantém o nome mais longo (mais completo)
                    if (atual.getNome().length() > salvo.getNome().length()) {
                        resultado.set(i, atual);
                    }
                    break;
                }
            }

            if (!duplicado) {
                resultado.add(atual);
            }
        }
        return resultado;
    }

    private boolean saoMesmaPessoaIniciais(String nomeA, String nomeB) {
        // Usa o método que você já criou para tirar os acentos e padronizar as caixas
        String nA = removerAcentos(nomeA).toUpperCase().trim();
        String nB = removerAcentos(nomeB).toUpperCase().trim();

        String[] partesA = nA.split("\\s+");
        String[] partesB = nB.split("\\s+");

        if (partesA.length < 2 || partesB.length < 2) return false;

        // Valida se o último sobrenome bate (ex: GUARALDI == GUARALDI)
        String sobrenomeA = partesA[partesA.length - 1];
        String sobrenomeB = partesB[partesB.length - 1];
        if (!sobrenomeA.equals(sobrenomeB)) return false;

        // Se a primeira string for o bloco de iniciais (ex: "VC") compara com o nome B
        if (partesA.length == 2 && partesA[0].length() > 1) {
            return checarIniciaisDoNome(partesA[0], partesB);
        }
        // Se a segunda string for o bloco de iniciais (ex: "SH") compara com o nome A
        if (partesB.length == 2 && partesB[0].length() > 1) {
            return checarIniciaisDoNome(partesB[0], partesA);
        }

        return false;
    }

    private boolean checarIniciaisDoNome(String blocoIniciais, String[] nomeCompleto) {
        // O bloco de iniciais precisa ter o mesmo tamanho da quantidade de prenomes disponíveis
        if (blocoIniciais.length() != nomeCompleto.length - 1) return false;

        for (int i = 0; i < blocoIniciais.length(); i++) {
            if (blocoIniciais.charAt(i) != nomeCompleto[i].charAt(0)) {
                return false;
            }
        }
        return true;
    }
}