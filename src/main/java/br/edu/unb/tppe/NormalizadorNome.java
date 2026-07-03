package br.edu.unb.tppe;

import java.text.Normalizer;

public class NormalizadorNome {

    public static String padronizarNomeBasico(String nome) {
        String n = tratarFormatoVirgula(nome);
        n = n.replaceAll("[´`’]", "'");
        n = n.replaceAll("(?i)\\bSt'anna\\b", "Sant'anna");
        n = n.replaceAll("(?i)\\bNoreira\\b", "Moreira");
        return n;
    }

    private static String tratarFormatoVirgula(String nome) {
        if (nome.contains(",")) {
            String[] partes = nome.split(",");
            if (partes.length == 2) {
                return partes[1].trim() + " " + partes[0].trim();
            }
        }
        return nome;
    }

    public static boolean saoMesmaGrafia(String nome1, String nome2) {
        return obterChaveGrafia(nome1).equalsIgnoreCase(obterChaveGrafia(nome2));
    }

    public static boolean saoMesmoNomeSemParticulas(String nome1, String nome2) {
        return obterChaveParticulasEPonto(nome1).equalsIgnoreCase(obterChaveParticulasEPonto(nome2));
    }

    private static String obterChaveGrafia(String palavra) {
        String resultado = palavra.replaceAll("[´`’]", "'");
        resultado = Normalizer.normalize(resultado, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        resultado = resultado.replaceAll("\\s+", " ").trim();
        return resultado;
    }

    private static String obterChaveParticulasEPonto(String palavra) {
        String resultado = obterChaveGrafia(palavra);
        resultado = resultado.replaceAll("\\.", "");
        resultado = resultado.replaceAll("(?i)\\b(de|do|da|dos|das)\\b", "");
        resultado = resultado.replaceAll("\\s+", " ").trim();
        return resultado;
    }

    public static boolean ehAbreviacao(String nomeCompleto, String nomeAbreviado) {
        String[] longos = obterChaveParticulasEPonto(nomeCompleto).toUpperCase().split(" ");
        String[] curtos = obterChaveParticulasEPonto(nomeAbreviado).toUpperCase().split(" ");

        if (longos.length < 2 || curtos.length < 2) return false;

        return compararTermos(longos, curtos) || compararTermos(longos, moverPrimeiroParaFim(curtos));
    }

    private static boolean compararTermos(String[] longos, String[] curtos) {
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

    private static String[] moverPrimeiroParaFim(String[] termos) {
        String[] inv = new String[termos.length];
        System.arraycopy(termos, 1, inv, 0, termos.length - 1);
        inv[termos.length - 1] = termos[0];
        return inv;
    }

    public static boolean saoMesmaPessoaIniciais(String nomeA, String nomeB) {
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

    private static boolean checarIniciaisDoNome(String blocoIniciais, String[] nomeCompleto) {
        if (blocoIniciais.length() != nomeCompleto.length - 1) return false;

        for (int i = 0; i < blocoIniciais.length(); i++) {
            if (blocoIniciais.charAt(i) != nomeCompleto[i].charAt(0)) {
                return false;
            }
        }
        return true;
    }
}
