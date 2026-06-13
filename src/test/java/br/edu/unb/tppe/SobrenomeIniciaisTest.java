package br.edu.unb.tppe;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Caso2")
public class SobrenomeIniciaisTest {

    private static Stream<Arguments> provedorDadosSobrenomeIniciais() {
        return Stream.of(
            // Conjunto 1: Sobrenome primeiro e depois as iniciais
            Arguments.of(
                List.of(
                    new Registro("28372", "Ana de Mattos Seabra"),
                    new Registro("582585", "Seabra A. M.")
                ),
                "Ana de Mattos Seabra"
            ),
            // Conjunto 2: Sobrenome no final
            Arguments.of(
                List.of(
                    new Registro("28371", "Cassius de Souza"),
                    new Registro("746936", "Souza C.")
                ),
                "Cassius de Souza"
            ),
            // Conjunto 3: Múltiplas partículas e abreviações com pontos
            Arguments.of(
                List.of(
                    new Registro("31303", "Veronica de Oliveira Moreira"),
                    new Registro("608303", "Moreira V. de O.")
                ),
                "Veronica de Oliveira Moreira"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provedorDadosSobrenomeIniciais")
    public void testNormalizarSobrenomeIniciais(List<Registro> listaSuja, String nomeEsperado) {
        Curador curador = new Curador();
        List<Registro> resultado = curador.normalizarSobrenomeIniciais(listaSuja).stream().distinct().toList();

        for (Registro r : resultado) {
            assertEquals(nomeEsperado, r.getNome(), 
                "O nome abreviado não foi normalizado para o padrão-ouro.");
        }
    }
}