package br.edu.unb.tppe;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Caso5")
public class IdsDuplicadosTest {

    private static Stream<Arguments> provedorDadosIds() {
        return Stream.of(
            // Conjunto 1: Caso de Sérgio Henrique Guaraldi com IDs de 713897 a 113897. Menor ID: 113897
            Arguments.of(
                List.of(
                    new Registro("713897", "Sérgio Henrique Guaraldi"),
                    new Registro("613897", "Sérgio Henrique Guaraldi"),
                    new Registro("513897", "Sérgio Henrique Guaraldi"),
                    new Registro("413897", "Sérgio Henrique Guaraldi"),
                    new Registro("313897", "Sérgio Henrique Guaraldi"),
                    new Registro("113897", "Sérgio Henrique Guaraldi")
                ),
                "113897"
            ),
            // Conjunto 2: Caso de Vanilda Cristina Junior. Menor ID: 213897
            Arguments.of(
                List.of(
                    new Registro("213897", "Vanilda Cristina Junior"),
                    new Registro("313897", "Vanilda Cristina Junior"),
                    new Registro("813897", "Vanilda Cristina Junior")
                ),
                "213897"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provedorDadosIds")
    public void testNormalizarIdsDuplicados(List<Registro> listaSuja, String idEsperado) {
        Curador curador = new Curador();
        List<Registro> resultado = curador.normalizarIdsDuplicados(listaSuja);

        assertNotNull(resultado, "A lista de retorno não deveria ser nula.");
        assertEquals(1, resultado.size(), "Os registros duplicados deveriam ser unificados em um único registro.");

        for (Registro r : resultado) {
            assertEquals(idEsperado, r.getId(), "O ID do registro não foi normalizado para o menor valor.");
        }
    }
}
