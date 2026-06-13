package br.edu.unb.tppe;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Caso3")
public class ParticulasEPontoTest {

    private static Stream<Arguments> provedorDadosParticulasEPonto() {
        return Stream.of(
            // Conjunto 1: Focado em Partículas (Exemplo do enunciado)
            Arguments.of(
                List.of(
                    new Registro("746937", "Luiz de Oliveira de Souza"),
                    new Registro("608296", "Luiz Oliveira Souza")
                ),
                List.of("Luiz de Oliveira de Souza")
            ),
            // Conjunto 2: Focado em Pontos e partículas (Exemplo do enunciado)
            Arguments.of(
                List.of(
                    new Registro("549242", "Luiz de O. de Souza"),
                    new Registro("549242", "Luiz de O de Souza")
                ),
                List.of("Luiz de O. de Souza")
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provedorDadosParticulasEPonto")
    public void testNormalizarParticulasEPonto(List<Registro> listaSuja, List<String> nomesEsperados) {
        Curador curador = new Curador();
        List<Registro> resultado = curador.normalizarParticulasEPonto(listaSuja).stream().distinct().toList();

        List<String> nomesObtidos = resultado.stream()
                .map(Registro::getNome)
                .toList();

        assertEquals(nomesEsperados.size(), resultado.size(), "O número de registros unificados está incorreto.");
        assertTrue(nomesObtidos.containsAll(nomesEsperados), "A lista de nomes obtidos não contém todos os nomes esperados.");
    }
}
