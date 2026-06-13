package br.edu.unb.tppe;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TabelaEnunciadoTest {

    @Test
    public void testTabelaCompletaEnunciado() {
        List<Registro> listaCompleta = List.of(
            new Registro("28372", "Ana de Mattos Seabra"),
            new Registro("243349", "Ana de Mattos Seabra"),
            new Registro("582585", "A. M. Seabra"),
            new Registro("582585", "Seabra A. M."),
            new Registro("582585", "AM Seabra"),
            new Registro("582585", "Ana Mattos Seabra"),
            new Registro("28371", "Cassius de Souza"),
            new Registro("746936", "Cassius Souza"),
            new Registro("746936", "Souza C."),
            new Registro("746936", "C. Souza"),
            new Registro("746936", "Souza, Cassius de"),
            new Registro("31303", "Veronica de Oliveira Moreira"),
            new Registro("243352", "Verônica de Oliveira Moreira"),
            new Registro("608303", "V. de O. Moreira"),
            new Registro("608303", "Moreira V O"),
            new Registro("608303", "Moreira V. de O."),
            new Registro("746941", "Verônica de Oliveira Noreira"),
            new Registro("746937", "Luiz de Oliveira de Souza"),
            new Registro("608296", "Luiz Oliveira Souza"),
            new Registro("549242", "Souza, Luiz de Oliveira"),
            new Registro("549242", "Luiz de O. de Souza"),
            new Registro("31297", "Souza, L. O."),
            new Registro("31299", "Monica Hirata Sant`anna"),
            new Registro("433095", "Mônica Hirata Sant’anna"),
            new Registro("746942", "Mônica Hirata St'anna"),
            new Registro("549244", "Sant'anna M. H."),
            new Registro("608298", "M. H. Sant'anna"),
            new Registro("763027", "Vanilda Cristina Junior"),
            new Registro("763027", "Vanilda Cristina Junior"),
            new Registro("335284", "Vanilda Cristina Júnior"),
            new Registro("335284", "Vanilda Cristina Júnior"),
            new Registro("335284", "Vanilda Cristina Júnior"),
            new Registro("554799", "Sergio Henrique Guaraldi"),
            new Registro("243350", "Sérgio Henrique Guaraldi"),
            new Registro("954057", "SH Guaraldi"),
            new Registro("954057", "Sérgio Henrique Guaraldi"),
            new Registro("954057", "Sérgio Henrique Guaraldi"),
            new Registro("954057", "Sérgio Henrique Guaraldi"),
            new Registro("31298", "Raphael Goncalves Viana"),
            new Registro("433094", "Raphael Gonçalves Viana"),
            new Registro("549243", "Raphael Gonçalves Viana"),
            new Registro("608297", "Raphael Gonçalves Viana"),
            new Registro("746938", "Raphael Gonçalves Viana"),
            new Registro("899639", "Lilian Luíza Viana Vieira"),
            new Registro("243351", "Lílian Luíza Viana Vieira"),
            new Registro("663795", "Lílian Luíza Viana Vieira"),
            new Registro("663795", "Lílian Luíza Viana Vieira"),
            new Registro("663795", "Lílian Luíza Viana Vieira"),
            new Registro("663795", "Lilian Luíza Viana Vieira"),
            new Registro("713897", "Yuri Vieira Faria"),
            new Registro("713897", "Yuri Vieira Faria"),
            new Registro("713897", "Yuri Vieira Faria"),
            new Registro("713897", "Yuri Vieira Faria")
        );

        Curador curador = new Curador();
        List<Registro> resultado = curador.processarEUnificar(listaCompleta);

        // Asserções para garantir que a deduplicação completa reduziu para os 10 autores ouro
        assertEquals(10, resultado.size(), "A lista final deveria conter exatamente 10 registros únicos (autores padrão-ouro).");

        // Verifica os 10 autores chaves unificados sob a grafia completa e menor ID
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Ana de Mattos Seabra") && r.getId().equals("28372")), "Falha ao unificar Ana de Mattos Seabra");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Cassius de Souza") && r.getId().equals("28371")), "Falha ao unificar Cassius de Souza");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Verônica de Oliveira Moreira") && r.getId().equals("31303")), "Falha ao unificar Verônica de Oliveira Moreira");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Luiz de Oliveira de Souza") && r.getId().equals("31297")), "Falha ao unificar Luiz de Oliveira de Souza");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Mônica Hirata Sant'anna") && r.getId().equals("31299")), "Falha ao unificar Mônica Hirata Sant'anna");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Vanilda Cristina Júnior") && r.getId().equals("335284")), "Falha ao unificar Vanilda Cristina Júnior");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Sérgio Henrique Guaraldi") && r.getId().equals("243350")), "Falha ao unificar Sérgio Henrique Guaraldi");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Raphael Gonçalves Viana") && r.getId().equals("31298")), "Falha ao unificar Raphael Gonçalves Viana");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Lílian Luíza Viana Vieira") && r.getId().equals("243351")), "Falha ao unificar Lílian Luíza Viana Vieira");
        assertTrue(resultado.stream().anyMatch(r -> r.getNome().equals("Yuri Vieira Faria") && r.getId().equals("713897")), "Falha ao unificar Yuri Vieira Faria");
    }
}
