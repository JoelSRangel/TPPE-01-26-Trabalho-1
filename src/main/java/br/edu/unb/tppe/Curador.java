package br.edu.unb.tppe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.text.Normalizer;

public class Curador {
    /**
     * Executa o fluxo completo de curadoria e unificação dos registros,
     * passando por todas as etapas de normalização (Grafia, Partículas/Pontos,
     * Iniciais de Sobrenomes, Iniciais Agrupadas e IDs Duplicados).
     *
     * @param registros A lista de registros a ser processada.
     * @return Uma lista de registros unificada e livre de duplicatas.
     */
    public List<Registro> processarEUnificar(List<Registro> registros) {
        if (registros == null) {
            return List.of();
        }

        List<Registro> resultado = normalizarGrafia(registros);
        resultado = normalizarParticulasEPonto(resultado);
        resultado = normalizarSobrenomeIniciais(resultado);
        resultado = unificarIniciaisAgrupadas(resultado);
        resultado = normalizarIdsDuplicados(resultado);

        return resultado;
    }

    /**
     * Caso 1: Normaliza diferenças de grafia, acentuação e caracteres especiais.
     * Compara os pares de registros e adota a grafia mais completa para nomes equivalentes.
     *
     * @param registros A lista de registros.
     * @return A lista de registros com a grafia unificada (sem deduplicação).
     */
    public List<Registro> normalizarGrafia(List<Registro> registros) {

        padronizarNomesDosRegistros(registros);

        for (int i = 0; i < registros.size(); i++) {
            for (int j = 0; j < registros.size(); j++) {
                if (i != j) {
                    Registro r1 = registros.get(i);
                    Registro r2 = registros.get(j);

                    int len1 = Normalizer.normalize(r1.getNome(), Normalizer.Form.NFKD).length();
                    int len2 = Normalizer.normalize(r2.getNome(), Normalizer.Form.NFKD).length();

                    // Se r1 for mais completo ou em caso de empate, substitui r2 por r1
                    if (len1 > len2 || (len1 == len2 && i < j)) {
                        if (saoMesmaGrafia(r1.getNome(), r2.getNome())) {

                            unificadorDeRegistros(r2, r1);
                        }
                    }
                }
            }
        }
        return registros;
    }

    /**
     * Caso 3: Unifica nomes com e sem partículas de ligação (de, do, da, etc.)
     * e com/sem pontos nas iniciais de abreviações.
     *
     * @param registros A lista de registros.
     * @return A lista de registros com partículas e abreviações com ponto normalizadas (sem deduplicação).
     */
    public List<Registro> normalizarParticulasEPonto(List<Registro> registros) {

        padronizarNomesDosRegistros(registros);

        for (int i = 0; i < registros.size(); i++) {
            for (int j = 0; j < registros.size(); j++) {
                if (i != j) {
                    Registro r1 = registros.get(i);
                    Registro r2 = registros.get(j);

                    DeduplicarRegistros(r1, r2, i, j);
                }
            }
        }
        return registros;
    }

    /**
     * Caso 2: Unifica variações que misturam o sobrenome com as iniciais dos prenomes
     * (ex: "Seabra A. M." -> "Ana de Mattos Seabra"). Adota o nome completo como padrão-ouro.
     *
     * @param registros A lista de registros.
     * @return A lista de registros com os nomes abreviados unificados (sem deduplicação).
     */
    public List<Registro> normalizarSobrenomeIniciais(List<Registro> registros) {

        padronizarNomesDosRegistros(registros);

        for (int i = 0; i < registros.size(); i++) {
            for (int j = 0; j < registros.size(); j++) {
                if (i != j) {
                    Registro r1 = registros.get(i);
                    Registro r2 = registros.get(j);
                    
                    // Compara apenas se r1 for maior que r2 (r1 = potencial nome completo)
                    if (r1.getNome().length() > r2.getNome().length()) {
                        if (ehAbreviacao(r1.getNome(), r2.getNome())) {

                            unificadorDeRegistros(r2, r1);
                        }
                    }
                }
            }
        }
        return registros;
    }

    /**
     * Caso 4: Unifica registros que possuem as iniciais dos prenomes agrupadas ao sobrenome
     * (ex: "VC Junior" -> "Vanilda Cristina Junior"). Prefere a versão completa do nome.
     *
     * @param registros A lista de registros.
     * @return A lista de registros com as iniciais agrupadas unificadas (sem deduplicação).
     */
    public List<Registro> unificarIniciaisAgrupadas(List<Registro> registros) {
        for (int i = 0; i < registros.size(); i++) {
            for (int j = 0; j < registros.size(); j++) {
                if (i != j) {
                    Registro r1 = registros.get(i);
                    Registro r2 = registros.get(j);

                    if (r1.getNome().length() > r2.getNome().length()) {
                        if (saoMesmaPessoaIniciais(r1.getNome(), r2.getNome())) {

                            unificadorDeRegistros(r2, r1);
                        }
                    }
                }
            }
        }
        return registros;
    }

    /**
     * Caso 5: IDs diferentes para o mesmo autor.
     * Mapeia todos os IDs para o ID de menor valor numérico associado àquele autor e realiza a deduplicação.
     *
     * @param registros A lista de registros.
     * @return A lista de registros unificada e livre de duplicatas.
     */
    public List<Registro> normalizarIdsDuplicados(List<Registro> registros) {
        // Agrupa IDs associados a cada nome
        HashMap<String, List<String>> mapaId = new HashMap<>();
        for (Registro r : registros) {
            String nome = r.getNome();
            mapaId.computeIfAbsent(nome, k -> new ArrayList<>()).add(r.getId());
        }

        // Associa o menor ID de cada grupo ao registro
        HashMap<String, String> mapaMenorId = mapearMenorId(mapaId);
        for (Registro r : registros) {
            String chave = r.getNome();
            String menorId = mapaMenorId.get(chave);
            r.setId(menorId);
        }

        return registros.stream().distinct().toList();
    }

    // Métodos extraidos
    private void padronizarNomesDosRegistros(List<Registro> registros) {
        for (Registro r : registros) {
            r.setNome(padronizarNomeBasico(r.getNome()));
        }
    }

    private void unificadorDeRegistros(Registro r2, Registro r1) {
        r2.setNome(r1.getNome());

        // Adota o menor ID entre os dois registros para preservar o menor ID
        String menorId = obterMenorId(r1.getId(), r2.getId());
        r1.setId(menorId);
        r2.setId(menorId);
    }

    private void DeduplicarRegistros(Registro r1, Registro r2, int i, int j) {
        int len1 = Normalizer.normalize(r1.getNome(), Normalizer.Form.NFKD).length();
        int len2 = Normalizer.normalize(r2.getNome(), Normalizer.Form.NFKD).length();

        // Se r1 for mais completo ou em caso de empate, substitui r2 por r1
        if (len1 > len2 || (len1 == len2 && i < j)) {
            if (saoMesmoNomeSemParticulas(r1.getNome(), r2.getNome())) {

                unificadorDeRegistros(r2, r1);
            }
        }
    }

    // --- Métodos Auxiliares e Utilitários (Privados) ---

    private String obterMenorId(String id1, String id2) {
        long val1 = Long.parseLong(id1);
        long val2 = Long.parseLong(id2);
        return String.valueOf(Math.min(val1, val2));
    }

    private String padronizarNomeBasico(String nome) {
        String n = tratarFormatoVirgula(nome);
        n = n.replaceAll("[´`’]", "'");
        n = n.replaceAll("(?i)\\bSt'anna\\b", "Sant'anna");
        n = n.replaceAll("(?i)\\bNoreira\\b", "Moreira");
        return n;
    }

    private boolean saoMesmaGrafia(String nome1, String nome2) {
        return obterChaveGrafia(nome1).equalsIgnoreCase(obterChaveGrafia(nome2));
    }

    private boolean saoMesmoNomeSemParticulas(String nome1, String nome2) {
        return obterChaveParticulasEPonto(nome1).equalsIgnoreCase(obterChaveParticulasEPonto(nome2));
    }

    private String tratarFormatoVirgula(String nome) {
        if (nome.contains(",")) {
            String[] partes = nome.split(",");
            if (partes.length == 2) {
                return partes[1].trim() + " " + partes[0].trim();
            }
        }
        return nome;
    }

    private String obterChaveGrafia(String palavra) {
        String resultado = palavra.replaceAll("[´`’]", "'");
        resultado = Normalizer.normalize(resultado, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        resultado = resultado.replaceAll("\\s+", " ").trim();
        return resultado;
    }

    private String obterChaveParticulasEPonto(String palavra) {
        String resultado = obterChaveGrafia(palavra);
        resultado = resultado.replaceAll("\\.", "");
        resultado = resultado.replaceAll("(?i)\\b(de|do|da|dos|das)\\b", "");
        resultado = resultado.replaceAll("\\s+", " ").trim();
        return resultado;
    }

    private boolean ehAbreviacao(String nomeCompleto, String nomeAbreviado) {
        String[] longos = obterChaveParticulasEPonto(nomeCompleto).toUpperCase().split(" ");
        String[] curtos = obterChaveParticulasEPonto(nomeAbreviado).toUpperCase().split(" ");

        if (longos.length < 2 || curtos.length < 2) return false;

        return compararTermos(longos, curtos) || compararTermos(longos, moverPrimeiroParaFim(curtos));
    }

    private boolean compararTermos(String[] longos, String[] curtos) {
        if (longos.length == curtos.length) {
            if (!longos[longos.length - 1].equals(curtos[curtos.length - 1])) {
                return false;
            }
            for (int i = 0; i < longos.length - 1; i++) {
                String l = longos[i];
                String c = curtos[i];
                if (!c.equals(l) && !(c.length() == 1 && c.charAt(0) == l.charAt(0))) {
                    return false;
                }
            }
            return true;
        }

        if (curtos.length == 2) {
            String iniciaisAgrupadas = curtos[0];
            String sobrenomeCurto = curtos[1];
            String sobrenomeLongo = longos[longos.length - 1];

            if (!sobrenomeCurto.equals(sobrenomeLongo)) {
                return false;
            }

            if (iniciaisAgrupadas.length() != longos.length - 1) {
                return false;
            }

            for (int i = 0; i < iniciaisAgrupadas.length(); i++) {
                if (iniciaisAgrupadas.charAt(i) != longos[i].charAt(0)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private String[] moverPrimeiroParaFim(String[] termos) {
        String[] inv = new String[termos.length];
        System.arraycopy(termos, 1, inv, 0, termos.length - 1);
        inv[termos.length - 1] = termos[0];
        return inv;
    }

    private HashMap<String, String> mapearMenorId(HashMap<String, List<String>> mapaId) {
        HashMap<String, String> mapaMenorId = new HashMap<>();

        for (String chave : mapaId.keySet()) {
            List<String> ids = mapaId.get(chave);
            long menorId = Long.parseLong(ids.getFirst());
            for (String idAtual : ids) {
                long idTemp = Long.parseLong(idAtual);
                if (idTemp < menorId) menorId = idTemp;
            }
            String valor = String.valueOf(menorId);
            mapaMenorId.computeIfAbsent(chave, k -> valor);
        }

        return mapaMenorId;
    }

    private boolean saoMesmaPessoaIniciais(String nomeA, String nomeB) {
        String nA = obterChaveParticulasEPonto(nomeA).toUpperCase();
        String nB = obterChaveParticulasEPonto(nomeB).toUpperCase();

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
}