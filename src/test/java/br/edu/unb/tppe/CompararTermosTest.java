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
}
