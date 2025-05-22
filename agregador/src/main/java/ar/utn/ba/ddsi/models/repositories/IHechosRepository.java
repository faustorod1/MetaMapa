package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IHechosRepository {
    List<Hecho> findAll();

    List<Hecho> saveAll(List<Hecho> hechos);

    void deleteAll();
}
