package ar.utn.ba.ddsi.MetaMapa;

import ar.utn.ba.ddsi.MetaMapa.models.entities.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CargaManualTest {
  private FuenteDeCargaManual unaFuente;
  private Criterio unCriterio;
  private Coleccion unaColeccion;

  private Categoria caidaAeronave = new Categoria("Caída de aeronave");

  @BeforeEach
  public void init() {
    unCriterio = Criterio.nuevo();

    Categoria accidenteMaquinaIndustrial = new Categoria("Accidente con maquinaria industrial");
    Categoria accidentePasoNivel = new Categoria("Accidente en paso a nivel");
    Categoria derrumbeObraConstrucción = new Categoria("Derrumbe en obra en construcción");

    unaFuente = new FuenteDeCargaManual();
    unaFuente.addHecho(
        Hecho.builder()
            .titulo("Caída de aeronave impacta en Olavarría")
            .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
            .categoria(caidaAeronave)
            .lugarAcontecimiento(new Coordenada(-36.868375, -60.343297))
            .fechaHecho(LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .build()
    );
    unaFuente.addHecho(
        Hecho.builder()
            .titulo("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén")
            .descripcion("Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
            .categoria(accidenteMaquinaIndustrial)
            .lugarAcontecimiento(new Coordenada(-37.345571, -70.241485))
            .fechaHecho(LocalDate.parse("16/08/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .build()
    );
    unaFuente.addHecho(
        Hecho.builder()
            .titulo("Caída de aeronave impacta en Venado Tuerto, Santa Fe")
            .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.")
            .categoria(caidaAeronave)
            .lugarAcontecimiento(new Coordenada(-33.768051, -61.921032))
            .fechaHecho(LocalDate.parse("08/08/2008", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .build()
    );
    unaFuente.addHecho(
        Hecho.builder()
            .titulo("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires")
            .descripcion("Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.")
            .categoria(accidentePasoNivel)
            .lugarAcontecimiento(new Coordenada(-35.855811, -61.940589))
            .fechaHecho(LocalDate.parse("27/01/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .build()
    );
    unaFuente.addHecho(
        Hecho.builder()
            .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
            .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
            .categoria(derrumbeObraConstrucción)
            .lugarAcontecimiento(new Coordenada(-26.780008, -60.458782))
            .fechaHecho(LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .build()
    );

    unaColeccion = new Coleccion("Coleccion de Prueba", "Esto es una Prueba", unaFuente, unCriterio);
  }

  //Como persona administradora, deseo crear una colección.
  @Test
  public void crearColeccionComoAdministrador(){ // 1.1

    // validar que se puedan obtener los hechos a partir de la coleccion
    Assertions.assertFalse(this.unaColeccion.getHechos().isEmpty());
    Assertions.assertEquals(unaFuente.getHechos(), unaColeccion.getHechos());
  }
  
  @Test
  public void criteriosDePertenencia(){ // 1.2
    List<Hecho> primeros3hechos = unaFuente.getHechos().subList(0,3);

    unaColeccion.getCriterioDePertenencia()
        .addFiltro(new FiltroPorFechaHecho("01/01/2000", "01/01/2010"));
    unaColeccion.recalcularHechos();

    Assertions.assertEquals(3, this.unaColeccion.getHechos().size());
    Assertions.assertTrue(unaColeccion.getHechos().containsAll(primeros3hechos));

    unaColeccion.getCriterioDePertenencia()
        .addFiltro(new FiltroPorCategoria(caidaAeronave));
    unaColeccion.recalcularHechos();

    Assertions.assertEquals(2, this.unaColeccion.getHechos().size());
    Assertions.assertTrue(unaColeccion.getHechos().contains(primeros3hechos.get(0)));
    Assertions.assertTrue(unaColeccion.getHechos().contains(primeros3hechos.get(2)));
  }

  @Test
  public void filtrosDelVisualizador() { // 1.3
    unaColeccion.getCriterioDePertenencia()
        .addFiltro(new FiltroPorCategoria(caidaAeronave))
        .addFiltro(new FiltroPorTitulo("un titulo"));
    unaColeccion.recalcularHechos();

    Assertions.assertTrue(unaColeccion.getHechos().isEmpty());
  }

  @Test
  public void etiquetas(){ // 1.4
    Criterio crit = Criterio.nuevo()
        .addFiltro(new FiltroPorTitulo("Caída de aeronave impacta en Olavarría"));
    ArrayList<Hecho> hechos = new ArrayList<>(crit.aplicarA(unaColeccion.getHechos()));
    Hecho miHecho = hechos.get(0);

    Etiqueta olavarria = new Etiqueta("Olavarría");
    Etiqueta grave = new Etiqueta("Grave");

    miHecho.etiquetar(olavarria);
    miHecho.etiquetar(grave);

    Assertions.assertTrue(miHecho.getEtiquetas().contains(olavarria));
    Assertions.assertTrue(miHecho.getEtiquetas().contains(grave));

  }
}
