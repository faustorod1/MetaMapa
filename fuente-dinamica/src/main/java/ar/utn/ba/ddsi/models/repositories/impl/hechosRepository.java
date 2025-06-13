package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.ACEPTADA;
import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.PENDIENTE;

@Repository
public class HechosRepository implements IHechosRepository {
  private List<Hecho> hechos;
  private Long idActual = 0L;

  public HechosRepository() {
    hechos = new ArrayList<>();
  }

  @Override
  public Hecho save(Hecho hecho) {
    idActual++;
    hecho.setId(idActual);
    hecho.setFechaDeCarga(LocalDateTime.now());
    hecho.setLastUpdate(hecho.getFechaDeCarga());
    this.hechos.add(hecho);
    return hecho;
  }

  @Override
  public List<Hecho> findAll() {
    return hechos.stream().filter(h-> !h.isEliminado()).collect(Collectors.toList());
  }

  @Override
  public Hecho findById(Long id) {
    return this.hechos.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
  }

  @Override
  public void marcarComoEliminado(Long id) {
    findById(id).setEliminado(true);
  }

  @Override
  public void update(Hecho hechoViejo, Hecho hecho) {
    hechoViejo.setTitulo(hecho.getTitulo());
    hechoViejo.setDescripcion(hecho.getDescripcion());
    hechoViejo.setCategoria(hecho.getCategoria());
    hechoViejo.setContenidoMultimedia(hecho.getContenidoMultimedia());
    hechoViejo.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
    hechoViejo.setFechaHecho(hecho.getFechaHecho());
    hechoViejo.setEliminado(hecho.isEliminado());
    hechoViejo.setEtiquetas(hecho.getEtiquetas());

    hechoViejo.setLastUpdate(LocalDateTime.now());
  }
}
