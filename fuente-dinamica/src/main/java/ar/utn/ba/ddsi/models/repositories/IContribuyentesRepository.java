package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContribuyentesRepository extends JpaRepository<Contribuyente, Long> {
}
