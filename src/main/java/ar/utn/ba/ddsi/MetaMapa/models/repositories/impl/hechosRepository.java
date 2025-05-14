package ar.utn.ba.ddsi.MetaMapa.models.repositories.impl;

import ar.utn.ba.ddsi.MetaMapa.models.entities.Hecho;
import ar.utn.ba.ddsi.MetaMapa.models.repositories.iHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class hechosRepository implements iHechosRepository {
  private List<Hecho> hechos;
  // para que no se repitan hechos en el repositorio podriamos implementar algo del tipo Map<Long, Hecho>
  //set? {}

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
    return hechos.stream().filter(hecho-> hecho.isRevisado() == !pendiente ).toList();
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
