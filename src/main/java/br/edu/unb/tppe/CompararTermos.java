package br.edu.unb.tppe;

public class CompararTermos {
    private final String[] longos;
    private final String[] curtos;

    public CompararTermos(String[] longos, String[] curtos) {
        this.longos = longos;
        this.curtos = curtos;
    }

    public boolean executar() {
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
}
