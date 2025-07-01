package ar.utn.ba.ddsi;

import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.commons.Coordenada;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FakeRepository {
    private static final Categoria caidaAeronave = new Categoria("Caída de aeronave");

    public static List<Coleccion> colecciones() {
        ArrayList<Coleccion> colecciones = new ArrayList<>();

        Criterio crit1 = Criterio.nuevo()
                .addFiltro(new FiltroPorCategoria(caidaAeronave));
        Criterio crit2 = Criterio.nuevo()
                        .addFiltro(new FiltroPorFechaHecho(null, "31/12/2010"));

//        colecciones.add(
//                new Coleccion("1", "Caídas de aeronaves", "Hechos cuya categoría es 'Caída de aeronave'.", crit1)
//        );
//        colecciones.add(
//                new Coleccion("2", "Hechos viejos", "Hechos anteriores al 2011", crit2)
//        );

        return colecciones;
    }

    public static Coleccion coleccionPorId(String identificador) {
        return colecciones().stream().filter(coleccion -> coleccion.getIdentificador().equals(identificador)).findFirst().orElse(null);
    }

    public static List<Hecho> hechos() {
        ArrayList<Hecho> hechos = new ArrayList<>();
        Categoria accidenteMaquinaIndustrial = new Categoria("Accidente con maquinaria industrial");
        Categoria accidentePasoNivel = new Categoria("Accidente en paso a nivel");
        Categoria derrumbeObraConstruccion = new Categoria("Derrumbe en obra en construcción");


        hechos.add(
                Hecho.builder()
                        .titulo("Caída de aeronave impacta en Olavarría")
                        .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
                        .categoria(caidaAeronave)
                        .lugarAcontecimiento(new Coordenada(-36.868375, -60.343297))
                        .fechaHecho(LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .build()
        );
        hechos.add(
                Hecho.builder()
                        .titulo("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén")
                        .descripcion("Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.")
                        .categoria(accidenteMaquinaIndustrial)
                        .lugarAcontecimiento(new Coordenada(-37.345571, -70.241485))
                        .fechaHecho(LocalDate.parse("16/08/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .build()
        );
        hechos.add(
                Hecho.builder()
                        .titulo("Caída de aeronave impacta en Venado Tuerto, Santa Fe")
                        .descripcion("Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.")
                        .categoria(caidaAeronave)
                        .lugarAcontecimiento(new Coordenada(-33.768051, -61.921032))
                        .fechaHecho(LocalDate.parse("08/08/2008", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .build()
        );
        hechos.add(
                Hecho.builder()
                        .titulo("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires")
                        .descripcion("Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.")
                        .categoria(accidentePasoNivel)
                        .lugarAcontecimiento(new Coordenada(-35.855811, -61.940589))
                        .fechaHecho(LocalDate.parse("27/01/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .build()
        );
        hechos.add(
                Hecho.builder()
                        .titulo("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña")
                        .descripcion("Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.")
                        .categoria(derrumbeObraConstruccion)
                        .lugarAcontecimiento(new Coordenada(-26.780008, -60.458782))
                        .fechaHecho(LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .build()
        );
        return hechos;
    }
}
