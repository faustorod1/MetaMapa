package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.iHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.ACEPTADA;
import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.PENDIENTE;

@Repository
public class hechosRepository implements iHechosRepository {
  private List<Hecho> hechos;
  // para que no se repitan hechos en el repositorio podriamos implementar algo del tipo Map<Long, Hecho>

  @Override
  public void save(Hecho hecho) {
    if (hecho.getId() == null) {
      //es un nuevo hecho
      hecho.setId((long) hechos.size());
      hechos.set(Math.toIntExact(hecho.getId()), hecho);
    } else {
      //es una modificacion
      hechos.set(Math.toIntExact(hecho.getId()), hecho);
    }
  }

  public void update(Hecho hechoViejo,Hecho hecho) {
    hecho.setId(hechoViejo.getId());
    hechos.set(Math.toIntExact(hechoViejo.getId()), hecho);
  }

  @Override
  public List<Hecho> findAll() {
    return this.hechos;
  }

  @Override
  public List<Hecho>findByPendiente(boolean pendiente){
    if(pendiente){
      return hechos.stream().filter(hecho-> hecho.getSolicitudDeModificacion().getEstado() == PENDIENTE ).toList();
    }
    return hechos.stream().filter(hecho-> hecho.getSolicitudDeModificacion().getEstado() == ACEPTADA ).toList();

    //si viene true en pendiente te devuelve los que estan pendientes de revision
    //si viene false en pendiente te devuelve los que ya se revisaron
  }

  @Override
  public Hecho findById(Long id) {
    return this.hechos.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
  }

  @Override
  public void delete(Hecho hecho) {
    hechos.remove(hecho);
  }
}
