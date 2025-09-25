package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.entities.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFuentesRepository extends JpaRepository<Fuente, Long> {
  List<Fuente> findAllByIdIn(List<Long> ids);
}
