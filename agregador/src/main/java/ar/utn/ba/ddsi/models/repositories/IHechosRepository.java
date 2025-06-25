package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IHechosRepository {
    List<Hecho> findAll();
    Hecho findById(Long id);
    List<Hecho> findFromFuente(String fuente);
    List<Hecho> findFromFuentes(List<String> fuentes);
    List<Hecho> saveAll(List<Hecho> hechos);

    void deleteAll();
}
