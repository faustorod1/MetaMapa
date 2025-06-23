package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;

public interface IHechosRepository {
    List<Hecho> findAll();
    Hecho findById(Long id, Long APIid);
    List<Hecho> saveAll(List<Hecho> hechos);
    void marcarComoEliminado(Long id,Long APIid);

}
