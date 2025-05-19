package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import java.util.List;

public interface iHechosRepository {
  HechoInputDTO save(Hecho hecho);
  void update(Hecho hechoViejo,Hecho hecho);
  List<Hecho> findAll();
  List<Hecho> findByPendiente(boolean pendiente);
  Hecho findById(Long id);
  List<Hecho> findByContribuyente(Contribuyente contribuyente);
  void delete(Hecho hecho);

}
