package br.edu.unb.tppe;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Caso4")
public class IniciaisAgrupadasTest {

    private static Stream<Arguments> provedorDadosIniciaisAgrupadas() {
        return Stream.of(
            // Conjunto 1: Mesmo ID, um nome completo e um com iniciais agrupadas (Vanilda)
            Arguments.of(
                List.of(
                    new Registro("763027", "Vanilda Cristina Junior"),
                    new Registro("763027", "VC Junior")
                ),
                List.of("Vanilda Cristina Junior")
            ),
            // Conjunto 2: IDs diferentes, mas mesma pessoa (Sérgio)
            Arguments.of(
                List.of(
                    new Registro("243350", "Sérgio Henrique Guaraldi"),
                    new Registro("954057", "SH Guaraldi")
                ),
                List.of("Sérgio Henrique Guaraldi")
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provedorDadosIniciaisAgrupadas")
    public void testUnificarIniciaisAgrupadas(List<Registro> listaSuja, List<String> nomesEsperados) {
        Curador curador = new Curador();
        List<Registro> resultado = curador.unificarIniciaisAgrupadas(listaSuja).stream().distinct().toList();

        List<String> nomesObtidos = resultado.stream()
                .map(Registro::getNome)
                .toList();

        assertEquals(nomesEsperados.size(), resultado.size(), "O número de registros unificados está incorreto.");
        assertTrue(nomesObtidos.containsAll(nomesEsperados), "A versão abreviada com iniciais agrupadas não foi substituída pela completa.");
    }
}
