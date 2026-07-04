package br.edu.unb.tppe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompararTermosTest {
    
    @Test
    void testCompararTermos() {
        String[] longos = {"João", "da", "Silva"};
        String[] curtos = {"J.", "Silva"};
        CompararTermos comparador = new CompararTermos(longos, curtos);
        assertNotNull(comparador);
    }

    @Test
    void deveRetornarTrueQuandoTermosForemIdenticos() {
        String[] longos = {"Maria", "de", "Souza"};
        String[] curtos = {"Maria", "Souza"};
        CompararTermos comparador = new CompararTermos(longos, curtos);
        assertTrue(comparador.executar());
    }

    @Test
    void deveRetornarTrueQuandoCurtosForemIniciaisDosLongos() {
        String[] longos = {"José", "da", "Silva"};
        String[] curtos = {"J.", "Silva"};
        
        CompararTermos comparador = new CompararTermos(longos, curtos);
        assertTrue(comparador.executar());
    }

    @Test
    void deveRetornarFalseQuandoSobrenomesForemDiferentes() {
        String[] longos = {"Carlos", "da", "Silva"};
        String[] curtos = {"Carlos", "da", "Souza"};

        CompararTermos comparador = new CompararTermos(longos, curtos);
        assertFalse(comparador.executar());
    }
}
