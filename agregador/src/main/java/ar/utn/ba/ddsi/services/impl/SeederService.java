package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.models.entities.FiltroPorCategoria;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.ISeederService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeederService implements ISeederService {
  private final IColeccionesRepository coleccionesRepository;
  private final IHechosRepository hechosRepository;

  @Override
  public void init(){
        Categoria catNieve = new Categoria("Copiosa caída de nieve");

        Hecho nevadaBariloche = Hecho.builder()
            .id(1L)
            .titulo("Nevada histórica en Bariloche")
            .descripcion("Cayeron 50 cm de nieve en 24 horas.")
            .categoria(catNieve)
            .fechaHecho(LocalDate.of(2009, 8, 3))
            .fechaDeCarga(LocalDateTime.now())
            .fechaUltimaActualizacion(LocalDateTime.now())
            .revisado(true)
            .build();

        Hecho nevadaUshuaia = Hecho.builder()
            .id(2L)
            .titulo("Nevada récord en Ushuaia")
            .descripcion("Se paralizó la ciudad durante dos días.")
            .categoria(catNieve)
            .fechaHecho(LocalDate.of(2005, 6, 19))
            .fechaDeCarga(LocalDateTime.now())
            .fechaUltimaActualizacion(LocalDateTime.now())
            .revisado(true)
            .build();

        hechosRepository.saveAll(List.of(nevadaBariloche, nevadaUshuaia));

        Criterio critNieve = Criterio.nuevo()
            .addFiltro(new FiltroPorCategoria(catNieve));

        Coleccion colNieve = new Coleccion(
            "NIEVE-001",
            "Hechos con copiosa caída de nieve",
            "Reúne todos los hechos cuya categoría sea «Copiosa caída de nieve».",
            critNieve
        );

        coleccionesRepository.save(colNieve);

        //Filtrar hechos según el criterio de la colección
        colNieve.filtrarHechos(hechosRepository.findAll());

        //Registrar en la tabla puente sólo  los que quedaron dentro
        colNieve.getHechos().forEach(
            h -> coleccionesRepository.agregarHechoAColeccion(
                colNieve.getIdentificador(), h.getId())
        );

  }
}
