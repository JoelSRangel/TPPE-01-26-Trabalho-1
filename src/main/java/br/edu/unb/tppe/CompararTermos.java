package br.edu.unb.tppe;

public class CompararTermos {
    private final String[] longos;
    private final String[] curtos;

    public CompararTermos(String[] longos, String[] curtos) {

        if (longos == null || curtos == null) {
            throw new IllegalArgumentException("Os arrays de termos não podem ser nulos.");
        }
        if (longos.length == 0 || curtos.length == 0) {
            throw new IllegalArgumentException("Os arrays de termos não podem estar vazios.");
        }

        this.longos = longos;
        this.curtos = curtos;
        
    }

    public boolean executar() {

        int qtdLongos = longos.length;
        int qtdCurtos = curtos.length;

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

        assert qtdLongos != qtdCurtos && qtdCurtos != 2 : "Erro de fluxo lógico no algoritmo";

        return false;
    }
}
