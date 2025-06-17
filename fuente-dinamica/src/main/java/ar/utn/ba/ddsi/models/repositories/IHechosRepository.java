package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import java.util.List;

public interface IHechosRepository {
  Hecho save(Hecho hecho);
  void update(Hecho hechoViejo,Hecho hecho);
  List<Hecho> findAll();
  Hecho findById(Long id);
  void marcarComoEliminado(Long id);

}
