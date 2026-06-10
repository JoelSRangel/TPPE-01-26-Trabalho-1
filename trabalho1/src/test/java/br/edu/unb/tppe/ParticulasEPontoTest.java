package br.edu.unb.tppe;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ParticulasEPontoTest {

    @Test
    public void testUnificacaoParticulasEPontos() {
        Curador curador = new Curador();
        
        // Conjunto de Dados 1: Focado em Partículas (Exemplo do enunciado)
        Registro r1 = new Registro("746937", "Luiz de Oliveira de Souza");
        Registro r2 = new Registro("608296", "Luiz Oliveira Souza");
        
        List<Registro> lista1 = List.of(r1, r2);
        List<Registro> resultado1 = curador.processarEUnificar(lista1);

        assertEquals(1, resultado1.size(), "Deveria unificar nomes com e sem partículas.");
        assertEquals("Luiz de Oliveira de Souza", resultado1.get(0).getNome());

        // Conjunto de Dados 2: Focado em Pontos (Exemplo do enunciado)
        Registro r3 = new Registro("549242", "Luiz de O. de Souza");
        Registro r4 = new Registro("549242", "Luiz de O de Souza");
        
        List<Registro> lista2 = List.of(r3, r4);
        List<Registro> resultado2 = curador.processarEUnificar(lista2);

        assertEquals(1, resultado2.size(), "Deveria unificar nomes com e sem ponto na abreviação.");
        assertTrue(resultado2.get(0).getNome().contains("O."));
    }
}
