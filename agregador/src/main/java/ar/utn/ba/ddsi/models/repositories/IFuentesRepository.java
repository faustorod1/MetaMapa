package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFuentesRepository extends JpaRepository<Fuente, Long> {
  
}
