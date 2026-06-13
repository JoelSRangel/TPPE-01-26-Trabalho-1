package br.edu.unb.tppe;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        GrafiaTest.class,
        SobrenomeIniciaisTest.class,
        ParticulasEPontoTest.class,
        CuradorTest.class,
        IdsDuplicadosTest.class
})
public class CuradoriaSuite {

}
