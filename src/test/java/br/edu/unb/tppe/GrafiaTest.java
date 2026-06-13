package br.edu.unb.tppe;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Caso1")
public class GrafiaTest {

    private static Stream<Arguments> provedorDadosGrafia() {
        return Stream.of(
            // Conjunto 1: Casos de Sérgio Henrique Guaraldi e Mônica Hirata Sant'anna
            Arguments.of(
                List.of(
                    new Registro("712897", "Sérgio Henrique Guaraldi"),
                    new Registro("712897", "Sergio Henrique Guaraldi"),
                    new Registro("712898", "Monica Hirata Sant`anna"),
                    new Registro("712898", "Mônica Hirata Sant’anna")
                ),
                List.of(
                    "Sérgio Henrique Guaraldi",
                    "Mônica Hirata Sant'anna"
                )
            ),
            // Conjunto 2: Caso de Lílian Luíza Viana Vieira
            Arguments.of(
                List.of(
                    new Registro("712899", "Lilian Luíza Viana Vieira"),
                    new Registro("712899", "Lílian Luíza Viana Vieira")
                ),
                List.of(
                    "Lílian Luíza Viana Vieira"
                )
            ),
            // Conjunto 3: Caso de registros idênticos (Yuri Vieira Faria)
            Arguments.of(
                List.of(
                    new Registro("713897", "Yuri Vieira Faria"),
                    new Registro("713897", "Yuri Vieira Faria")
                ),
                List.of(
                    "Yuri Vieira Faria"
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provedorDadosGrafia")
    public void testNormalizarGrafia(List<Registro> listaSuja, List<String> nomesEsperados) {
        Curador curador = new Curador();
        List<Registro> resultado = curador.normalizarGrafia(listaSuja).stream().distinct().toList();

        List<String> nomesObtidos = resultado.stream()
                .map(Registro::getNome)
                .toList();

        assertEquals(nomesEsperados.size(), resultado.size(), "O número de registros unificados está incorreto.");
        assertTrue(nomesObtidos.containsAll(nomesEsperados), "A lista de nomes obtidos não contém todos os nomes esperados.");
    }
}
