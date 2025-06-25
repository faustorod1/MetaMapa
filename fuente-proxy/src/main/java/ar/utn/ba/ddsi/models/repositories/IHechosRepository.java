package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosRepository {
    List<Hecho> findAll();
    List<Hecho> findAllAPI();
    List<Hecho> findAllAfterAPI(LocalDateTime desde);
    Hecho findById(Long id, Long APIid);
    List<Hecho> APIsaveAll(List<Hecho> hechos);
    void marcarComoEliminado(Long id,Long APIid);
    List<Hecho> findAllAfterMetamapa(LocalDateTime desde);
    List<Hecho> metaSaveAll(List<Hecho> hechosNuevos);
    List<Hecho> findAllMetaMapa();
}
