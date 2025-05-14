package ar.utn.ba.ddsi.MetaMapa.models.repositories;

import ar.utn.ba.ddsi.MetaMapa.models.entities.Hecho;
import java.util.List;
import java.util.Optional;

public interface iHechosRepository {
  void save(Hecho hecho);
  void update(Hecho hechoViejo,Hecho hecho);
  List<Hecho> findAll();
  List<Hecho> findByPendiente(boolean pendiente);
  Hecho findById(Long id);
  void delete(Hecho hecho);

}
