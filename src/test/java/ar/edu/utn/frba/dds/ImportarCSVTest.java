package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ImportarCSVTest {
    @Test
    public void test() {
        // Busca este archivo en la carpeta "resources" del proyecto
        String path = getClass().getClassLoader().getResource("dataset_prueba.csv").getPath();
        FuenteEstatica unaFuente = new FuenteEstatica(path);
        ArrayList<Hecho> hechos = unaFuente.getHechos();

        Assertions.assertFalse(hechos.isEmpty());
        // Revisamos que el primer hecho que cargó es el mismo del csv de prueba
        Assertions.assertEquals("Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones", hechos.get(0).getTitulo());
    }
}
