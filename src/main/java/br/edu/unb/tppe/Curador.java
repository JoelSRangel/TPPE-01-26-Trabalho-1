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
                        if (NormalizadorNome.saoMesmaGrafia(r1.getNome(), r2.getNome())) {

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
                        if (NormalizadorNome.ehAbreviacao(r1.getNome(), r2.getNome())) {

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
                        if (NormalizadorNome.saoMesmaPessoaIniciais(r1.getNome(), r2.getNome())) {

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

    // --- Métodos Auxiliares e Utilitários (Privados) ---

    // ----------------------------------- Métodos extraidos ----------------------------------
    private void padronizarNomesDosRegistros(List<Registro> registros) {
        for (Registro r : registros) {
            r.setNome(NormalizadorNome.padronizarNomeBasico(r.getNome()));
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
            if (NormalizadorNome.saoMesmoNomeSemParticulas(r1.getNome(), r2.getNome())) {

                unificadorDeRegistros(r2, r1);
            }
        }
    }
    // ----------------------------------------------------------------------------------------

    private String obterMenorId(String id1, String id2) {
        long val1 = Long.parseLong(id1);
        long val2 = Long.parseLong(id2);
        return String.valueOf(Math.min(val1, val2));
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
}