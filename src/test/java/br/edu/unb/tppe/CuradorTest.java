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
    public void testUnificarIniciaisAgrupadasParaVersaoCompleta() {
        // Arrange
        Curador curador = new Curador();
        
        // Cenário 1: Mesmo ID, um nome completo e um com iniciais agrupadas
        Registro r1 = new Registro("763027", "Vanilda Cristina Junior");
        Registro r2 = new Registro("763027", "VC Junior");
        
        // Cenário 2: IDs diferentes, mas mesma pessoa (completo vs iniciais)
        Registro r3 = new Registro("243350", "Sérgio Henrique Guaraldi");
        Registro r4 = new Registro("954057", "SH Guaraldi");
        
        List<Registro> listaSuja = List.of(r1, r2, r3, r4);

        // Act
        List<Registro> resultado = curador.processarEUnificar(listaSuja);

        // Assert
        assertNotNull(resultado);
        
        // Esperamos que restem apenas 2 registros únicos (um para Vanilda e um para Sérgio)
        assertEquals(2, resultado.size(), "Deveriam restar apenas 2 registros após unificar as iniciais agrupadas.");

        // Verifica se o nome preferido foi o completo (Padrão-Ouro)
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Vanilda Cristina Junior")), 
            "A versão abreviada 'VC Junior' deveria ter sido substituída pela completa.");
            
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Sérgio Henrique Guaraldi")), 
            "A versão abreviada 'SH Guaraldi' deveria ter sido substituída pela completa.");
    }
}