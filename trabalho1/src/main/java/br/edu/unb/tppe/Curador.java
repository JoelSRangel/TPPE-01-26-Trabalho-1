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

        // Caso 1 e Caso 3 básicos
        List<Registro> resultado = normalizarGrafia(registros);
        
        // Caso 2: Sobrenome + Iniciais (Ex: Seabra A.M. -> Ana de Mattos Seabra)
        resultado = normalizarSobrenomeIniciais(resultado);

        // Caso 4: Iniciais Agrupadas (Ex: VC Junior -> Vanilda Cristina Junior)
        resultado = unificarIniciaisAgrupadas(resultado);
        
        // Caso 5: IDs Duplicados
        resultado = normalizarIdsDuplicados(resultado);

        return resultado;
    }

    public String removerAcentos(String palavra){
        String resultado = palavra.replaceAll("[´`’]", "'");
        resultado = Normalizer.normalize(resultado, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        
        // Caso 3: remover pontos e partículas
        resultado = resultado.replaceAll("\\.", "");
        resultado = resultado.replaceAll("(?i)\\b(de|do|da|dos|das)\\b", "");
        resultado = resultado.replaceAll("\\s+", " ").trim();
        
        return resultado;
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
        HashMap<String, String> mapaIdsCorretos = new HashMap<>();

        // criando o mapa com as variações dos nomes e guardando um ID de referência
        for(Registro r : registros){
            String nome = r.getNome();
            String nomeLimpo = removerAcentos(nome);
            mapaNomes.computeIfAbsent(nomeLimpo, k -> new ArrayList<>()).add(nome);
            mapaIdsCorretos.putIfAbsent(nomeLimpo, r.getId());
        }

        // escolhendo os nomes corretos entre as variações possíveis
        for(String chave : mapaNomes.keySet()){
            String nomeCorreto = definirNomeCorreto(mapaNomes.get(chave));
            mapaNomesCorretos.put(chave, nomeCorreto);
        }

        // corrigindo os nomes e IDs para cada registro para permitir a unificação
        for(Registro r : registros){
            String nomeLimpo = removerAcentos(r.getNome());
            r.setNome(mapaNomesCorretos.get(nomeLimpo));
            r.setId(mapaIdsCorretos.get(nomeLimpo));
        }

        return registros.stream().
                distinct().
                toList();
    }


    // Caso 2: Sobrenome + Iniciais dos nomes
   /*
        Ocorre quando há variações na representação do nome do autor que combinam o sobrenome principal
        com as iniciais dos prenomes (abreviações com ou sem pontos, ordenadas antes ou depois do sobrenome).
        A unificação identifica a equivalência e adota o nome completo como o padrão-ouro.
    */

    public List<Registro> normalizarSobrenomeIniciais(List<Registro> registros) {
        for (int i = 0; i < registros.size(); i++) {
            for (int j = 0; j < registros.size(); j++) {
                if (i != j) {
                    Registro r1 = registros.get(i);
                    Registro r2 = registros.get(j);
                    
                    // Compara apenas se r1 for maior que r2 (r1 = potencial nome completo)
                    if (r1.getNome().length() > r2.getNome().length()) {
                        if (ehAbreviacao(r1.getNome(), r2.getNome())) {
                            // Se for abreviação, padroniza r2 com o nome de r1
                            r2.setNome(r1.getNome());
                        }
                    }
                }
            }
        }
        return registros.stream().distinct().toList();
    }

    private boolean ehAbreviacao(String nomeCompleto, String nomeAbreviado) {
        // Limpa e prepara o nome longo (remove acentos e partículas comuns)
        String nLongo = removerAcentos(nomeCompleto).toUpperCase()
                .replaceAll("\\b(?:DE|DA|DO|DAS|DOS)\\b", "").trim();
        nLongo = nLongo.replaceAll("\\s+", " "); // Normaliza espaços
        
        // Limpa e prepara o nome curto (remove acentos, pontos e espaços para facilitar match exato)
        String nCurto = removerAcentos(nomeAbreviado).toUpperCase()
                .replaceAll("\\b(?:DE|DA|DO|DAS|DOS)\\b", "")
                .replace(".", "").replaceAll("\\s+", "");

        String[] partes = nLongo.split(" ");
        if (partes.length < 2) return false;

        // O último termo é tratado como o sobrenome principal
        String sobrenome = partes[partes.length - 1];
        
        // Extrai a primeira letra dos demais nomes
        StringBuilder iniciais = new StringBuilder();
        for (int i = 0; i < partes.length - 1; i++) {
            iniciais.append(partes[i].charAt(0));
        }

        // Monta os dois padrões possíveis sem espaços: "SOBRENOME + INICIAIS" ou "INICIAIS + SOBRENOME"
        String padrao1 = sobrenome + iniciais.toString();
        String padrao2 = iniciais.toString() + sobrenome;

        return nCurto.equals(padrao1) || nCurto.equals(padrao2);
    }

    public HashMap<String, String> mapearMenorId(HashMap<String, List<String>> mapaId){
        HashMap<String, String> mapaMenorId = new HashMap<>();

        for(String chave : mapaId.keySet()){
            List<String> ids = mapaId.get(chave);
            long menorId = Long.parseLong(ids.getFirst());
            for(String idAtual : ids){
                long idTemp = Long.parseLong(idAtual);
                if(idTemp < menorId) menorId = idTemp;
            }
            String valor = String.valueOf(menorId);
            mapaMenorId.computeIfAbsent(chave, k -> valor);
        }

        return  mapaMenorId;
    }


    // Caso 4
    /* Há casos em que as iniciais do nome e dos primeiros sobrenomes são agrupadas restando por extenso apenas o último nome
    a versão completa do nome deve ser preferida em relação à versão com abreviações. */
    public List<Registro> unificarIniciaisAgrupadas(List<Registro> registros) {
        List<Registro> resultado = new ArrayList<>();

        for (Registro atual : registros) {
            boolean duplicado = false;

            for (int i = 0; i < resultado.size(); i++) {
                Registro salvo = resultado.get(i);

                if (saoMesmaPessoaIniciais(atual.getNome(), salvo.getNome())) {
                    duplicado = true;
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
        String nA = removerAcentos(nomeA).toUpperCase().replaceAll("\\b(?:DE|DA|DO|DAS|DOS)\\b", "").replaceAll("\\s+", " ").trim();
        String nB = removerAcentos(nomeB).toUpperCase().replaceAll("\\b(?:DE|DA|DO|DAS|DOS)\\b", "").replaceAll("\\s+", " ").trim();

        String[] partesA = nA.split("\\s+");
        String[] partesB = nB.split("\\s+");

        if (partesA.length < 2 || partesB.length < 2) return false;

        String sobrenomeA = partesA[partesA.length - 1];
        String sobrenomeB = partesB[partesB.length - 1];
        if (!sobrenomeA.equals(sobrenomeB)) return false;

        if (partesA.length == 2 && partesA[0].length() > 1) {
            return checarIniciaisDoNome(partesA[0], partesB);
        }
        if (partesB.length == 2 && partesB[0].length() > 1) {
            return checarIniciaisDoNome(partesB[0], partesA);
        }

        return false;
    }

    private boolean checarIniciaisDoNome(String blocoIniciais, String[] nomeCompleto) {
        if (blocoIniciais.length() != nomeCompleto.length - 1) return false;

        for (int i = 0; i < blocoIniciais.length(); i++) {
            if (blocoIniciais.charAt(i) != nomeCompleto[i].charAt(0)) {
                return false;
            }
        }
        return true;
    }

    // Caso 5: IDs diferentes para o mesmo autor
    /*
        Por fim, devido às diversas fontes de dados, os registros de publicação e autorias
        duplicados, sendo um registro para cada fonte. Nesses casos, todos os registros
        ser mapeados para o mesmo id, sendo o id de menor valor eleito para ser utilizado
        deduplicação
    */
    public List<Registro> normalizarIdsDuplicados(List<Registro> registros){
        // criando um mapa de (nome, lista<ids>)
        HashMap<String, List<String>> mapaId = new HashMap<>();
        for(Registro r : registros){
            String nome = r.getNome();
            mapaId.computeIfAbsent(nome, k -> new ArrayList<>()).add(r.getId());
        }

        // atribui o menor id para cada registro de uma mesma pessoa
        HashMap<String, String> mapaMenorId = mapearMenorId(mapaId);
        for(Registro r : registros){
            String chave = r.getNome();
            String menorId = mapaMenorId.get(chave);
            r.setId(menorId);
        }

        return registros.stream().distinct().toList();
    }
}