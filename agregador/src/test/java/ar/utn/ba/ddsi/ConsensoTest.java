package ar.utn.ba.ddsi;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.ConsensoAbsoluta;
import ar.utn.ba.ddsi.models.entities.ConsensoMayoriaSimple;
import ar.utn.ba.ddsi.models.entities.ConsensoMultiplesMenciones;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.IAlgoritmoDeConsenso;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConsensoTest {

  private List<String> listaFuentes;
  private List<Hecho> listaHechos;

  @BeforeEach
  public void init(){
    listaFuentes = new ArrayList<String>();

    listaFuentes.add("estatica");
    listaFuentes.add("proxy:1");
    listaFuentes.add("proxy:2");

    listaHechos = new ArrayList<Hecho>();

    Categoria caidaAeronave = new Categoria("Caida de aeronave");
    Categoria accidenteMaquinaIndustrial = new Categoria("Accidente de Máquina Industrial");
    Categoria accidentePasoNivel = new Categoria("Accidente de Paso Nivel");
    Categoria derrumbeObraConstrucción = new Categoria("Derrumbe obra construcción");

    listaHechos.add(
        Hecho.builder()
            .titulo("Caída de aeronave impacta en Olavarría")
            .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
            .categoria(caidaAeronave)
            .lugarAcontecimiento(new Coordenada(-36.868375, -60.343297))
            .fechaHecho(LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("proxy:1:1")
            .build()
    );
    listaHechos.add(
        Hecho.builder()
            .titulo("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén")
            .descripcion("Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
            .categoria(accidenteMaquinaIndustrial)
            .lugarAcontecimiento(new Coordenada(-37.345571, -70.241485))
            .fechaHecho(LocalDate.parse("16/08/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("proxy:1:2")
            .build()
    );
    listaHechos.add(
        Hecho.builder()
            .titulo("Caída de aeronave impacta en Venado Tuerto, Santa Fe")
            .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.")
            .categoria(caidaAeronave)
            .lugarAcontecimiento(new Coordenada(-33.768051, -61.921032))
            .fechaHecho(LocalDate.parse("08/08/2008", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("proxy:1:3")
            .build()
    );
    listaHechos.add(
        Hecho.builder()
            .titulo("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires")
            .descripcion("Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.")
            .categoria(accidentePasoNivel)
            .lugarAcontecimiento(new Coordenada(-35.855811, -61.940589))
            .fechaHecho(LocalDate.parse("27/01/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("proxy:1:4")
            .build()
    );
    listaHechos.add(
        Hecho.builder()
            .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
            .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
            .categoria(derrumbeObraConstrucción)
            .lugarAcontecimiento(new Coordenada(-26.780008, -60.458782))
            .fechaHecho(LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("proxy:1:5")
            .build()
    );
    listaHechos.add(
        Hecho.builder()
            .titulo("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires")
            .descripcion("Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.")
            .categoria(accidentePasoNivel)
            .lugarAcontecimiento(new Coordenada(-35.855811, -61.940589))
            .fechaHecho(LocalDate.parse("27/01/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("estatica:1:1")
            .build()
    );
    listaHechos.add(
        Hecho.builder()
            .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
            .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
            .categoria(derrumbeObraConstrucción)
            .lugarAcontecimiento(new Coordenada(-26.780008, -60.458782))
            .fechaHecho(LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("estatica:1:2")
            .build()
    );
    listaHechos.add(
        Hecho.builder()
            .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
            .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
            .categoria(derrumbeObraConstrucción)
            .lugarAcontecimiento(new Coordenada(-26.780008, -60.458782))
            .fechaHecho(LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("proxy:2:1")
            .build()
    );

  }


  @Test
  public void perteneceAFuente() throws Exception {
    Hecho hecho = Hecho.builder()
        .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
        .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
        .categoria(null)
        .lugarAcontecimiento(new Coordenada(-26.780008, -60.458782))
        .fechaHecho(LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        .idExterno("estatica:1:2")
        .build();

    Assertions.assertThat(hecho.perteneceALaFuente("estatica")).isTrue();
    Assertions.assertThat(hecho.perteneceALaFuente("estatica:1")).isTrue();
    Assertions.assertThat(hecho.perteneceALaFuente("estatica:2")).isFalse();
    Assertions.assertThat(hecho.perteneceALaFuente("proxy")).isFalse();
    Assertions.assertThat(hecho.perteneceALaFuente("proxy:1")).isFalse();
  }

  @Test
  public void SonIguales() throws Exception {
    Assertions.assertThat(listaHechos.get(3).hechoIgualA(listaHechos.get(5))).isTrue();
    Assertions.assertThat(listaHechos.get(1).hechoIgualA(listaHechos.get(5))).isFalse();
  }

  @Test
  public void absoluta() throws Exception {
    IAlgoritmoDeConsenso algoritmoAbs = new ConsensoAbsoluta();

    List<Hecho> hechosConsensuados = algoritmoAbs.consensuar(listaHechos, listaFuentes);

    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña"))).isTrue();
    Assertions.assertThat(hechosConsensuados.size()).isEqualTo(1);

    System.out.println(hechosConsensuados.size());
  }

  @Test
  public void mayoriaSimple() throws Exception {
    IAlgoritmoDeConsenso algoritmoMaySimple = new ConsensoMayoriaSimple();

    List<Hecho> hechosConsensuados = algoritmoMaySimple.consensuar(listaHechos, listaFuentes);
    System.out.println(hechosConsensuados.size());

    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires"))).isTrue();
    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña"))).isTrue();
    Assertions.assertThat(hechosConsensuados.size()).isEqualTo(2);


  }

  @Test
  public void multiplesMenciones() throws Exception{
    IAlgoritmoDeConsenso algoritmoMultiplesMenciones = new ConsensoMultiplesMenciones();

    List<Hecho> hechosConsensuados = algoritmoMultiplesMenciones.consensuar(listaHechos, listaFuentes);
    System.out.println(hechosConsensuados.size());

    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires"))).isTrue();
    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña"))).isTrue();
    Assertions.assertThat(hechosConsensuados.size()).isEqualTo(2);

  }

  private void cambiarUltimoHechoParaQueNoCoincida() {
    listaHechos.remove(listaHechos.size()-1);
    listaHechos.add(
        Hecho.builder()
            .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
            .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
            .categoria(new Categoria("categoria distinta"))
            .lugarAcontecimiento(new Coordenada(-26.780008, -60.458782))
            .fechaHecho(LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .idExterno("proxy:2:1")
            .build()
    );
  }

  @Test
  public void absolutaConDistintos() throws Exception {
    cambiarUltimoHechoParaQueNoCoincida();
    IAlgoritmoDeConsenso algoritmoAbs = new ConsensoAbsoluta();

    List<Hecho> hechosConsensuados = algoritmoAbs.consensuar(listaHechos, listaFuentes);
    System.out.println(hechosConsensuados.size());

    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña"))).isFalse();
    Assertions.assertThat(hechosConsensuados.size()).isEqualTo(0);
  }
  @Test
  public void mayoriaSimpleConDistintos() throws Exception {
    cambiarUltimoHechoParaQueNoCoincida();
    IAlgoritmoDeConsenso algoritmoMay = new ConsensoMayoriaSimple();

    List<Hecho> hechosConsensuados = algoritmoMay.consensuar(listaHechos, listaFuentes);
    System.out.println(hechosConsensuados.size());

    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires"))).isTrue();
    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña"))).isFalse();
    Assertions.assertThat(hechosConsensuados.size()).isEqualTo(1);
  }
  @Test
  public void multiplesMencionesConDistintos() throws Exception {
    cambiarUltimoHechoParaQueNoCoincida();
    IAlgoritmoDeConsenso algoritmoMult = new ConsensoMultiplesMenciones();

    List<Hecho> hechosConsensuados = algoritmoMult.consensuar(listaHechos, listaFuentes);
    System.out.println(hechosConsensuados.size());

    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires"))).isTrue();
    Assertions.assertThat(hechosConsensuados.stream().anyMatch(h -> h.getTitulo().equals("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña"))).isFalse();
    Assertions.assertThat(hechosConsensuados.size()).isEqualTo(1);
  }
}