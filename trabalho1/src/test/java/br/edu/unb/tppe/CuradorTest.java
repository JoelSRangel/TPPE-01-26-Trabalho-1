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
}