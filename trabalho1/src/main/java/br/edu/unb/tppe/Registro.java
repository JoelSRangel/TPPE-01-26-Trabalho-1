package br.edu.unb.tppe;

import java.util.Objects;

public class Registro {
    private final String id;
    private final String nome;

    public Registro(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registro registro = (Registro) o;
        return Objects.equals(id, registro.id) && Objects.equals(nome, registro.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}