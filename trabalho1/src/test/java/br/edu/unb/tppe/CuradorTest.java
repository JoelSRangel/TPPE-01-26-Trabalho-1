package br.edu.unb.tppe;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CuradorTest {

    @Test
    public void testRemoverDuplicatasIdenticas() {
        Curador curador = new Curador();
        Registro r1 = new Registro("713897", "Yuri Vieira Faria");
        Registro r2 = new Registro("713897", "Yuri Vieira Faria"); 
        List<Registro> listaSuja = List.of(r1, r2);

        List<Registro> resultado = curador.processarEUnificar(listaSuja);

        assertNotNull(resultado, "A lista de retorno não deveria ser nula.");
        assertEquals(1, resultado.size(), "Registros idênticos deveriam ser unificados.");
    }

    @Test
    public void testCorrigirGrafiaAcentuacaoDiferentes() {
        Curador curador = new Curador();
        Registro r1 = new Registro("712897", "Sérgio Henrique Guaraldi");
        Registro r2 = new Registro("712897", "Sergio Henrique Guaraldi");
        Registro r4 = new Registro("712898", "Monica Hirata Sant`anna");
        Registro r3 = new Registro("712898", "Mônica Hirata Sant´anna");
        Registro r5 = new Registro("712899", "Lilian Luíza Viana Vieira");
        Registro r6 = new Registro("712899", "Lílian Luíza Viana Vieira");

        List<String> NOMES_CORRETOS = List.of(r1.getNome(), r3.getNome().replaceAll("[´`]","'"), r6.getNome());

        List<Registro> listaSuja = List.of(r1, r2, r3, r4, r5, r6);

        List<Registro> resultado = curador.normalizarGrafia(listaSuja);
        List<String> nomesObtidos = resultado.stream()
                .map(Registro::getNome)
                .toList();

        assertEquals(3, resultado.size());
        assertTrue(nomesObtidos.containsAll(NOMES_CORRETOS));
    }
}