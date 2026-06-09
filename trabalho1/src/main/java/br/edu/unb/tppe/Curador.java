package br.edu.unb.tppe;

import java.util.List;

public class Curador {
    
    public List<Registro> processarEUnificar(List<Registro> registros) {
        if (registros == null) {
            return List.of();
        }
        return registros.stream()
                .distinct()
                .toList();
    }
}