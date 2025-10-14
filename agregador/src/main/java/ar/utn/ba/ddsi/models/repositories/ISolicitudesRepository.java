package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudesRepository extends JpaRepository<SolicitudDeEliminacion, Long> {
}
