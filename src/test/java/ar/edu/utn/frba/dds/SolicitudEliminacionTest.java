package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SolicitudEliminacionTest {
    private FuenteDeCargaManual unaFuente = new FuenteDeCargaManual();
    private String justificacionEliminacion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque sodales sit amet felis id mollis. Sed consequat erat finibus dictum interdum. Phasellus dictum tempus dolor, sit amet consectetur ipsum. Fusce in rhoncus ligula, non molestie tellus. Etiam et nulla nisl. Nam et porta massa, id cursus nulla. Sed sed lobortis ligula. Etiam et orci auctor, elementum ipsum et, bibendum nisi. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus luctus vel eros ut dignissim. Integer dui.";
    private SolicitudDeEliminacion solicitud;
    private Hecho hecho;
    private LocalDateTime fechaActualFake = LocalDateTime.now();

    @BeforeEach
    public void init() {
        hecho = Hecho.builder()
                .titulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")
                .descripcion("Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.")
                .categoria(new Categoria("Evento sanitario"))
                .lugarAcontecimiento(new Coordenada(-32.786098, -60.741543))
                .fechaHecho(LocalDate.parse("05/07/2005", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
        unaFuente.addHecho(hecho);
        solicitud = hecho.solicitarEliminacion(justificacionEliminacion);
    }

    @Test
    public void rechazadaTest() {
        fechaActualFake = fechaActualFake.plusDays(1);
        solicitud.rechazar(fechaActualFake);
        Criterio criterio = Criterio.nuevo();
        Coleccion coleccion = new Coleccion("colección de prueba", "para probar", unaFuente, criterio);

        Assertions.assertTrue(coleccion.contiene(hecho)); // Verificando esto, podemos afirmar que la sol. de elim. del hecho fue rechazada!
        Assertions.assertEquals(SolicitudDeEliminacion.Estado.RECHAZADA, solicitud.getEstado());
    }

    @Test
    public void aceptadaTest() {
        fechaActualFake = fechaActualFake.plusHours(2);
        solicitud.aceptar(fechaActualFake);
        Criterio criterio = Criterio.nuevo();
        Coleccion coleccion = new Coleccion("colección de prueba", "para probar", unaFuente, criterio);

        Assertions.assertFalse(coleccion.contiene(hecho));
        Assertions.assertEquals(SolicitudDeEliminacion.Estado.ACEPTADA, solicitud.getEstado());
    }


}
