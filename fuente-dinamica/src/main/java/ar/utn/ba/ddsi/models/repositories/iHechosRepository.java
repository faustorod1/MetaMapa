package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import java.util.List;

public interface iHechosRepository {
  void save(Hecho hecho);
  void update(Hecho hechoViejo,Hecho hecho);
  List<Hecho> findAll();
  List<Hecho> findByPendiente(boolean pendiente);
  Hecho findById(Long id);
  void delete(Hecho hecho);

}
