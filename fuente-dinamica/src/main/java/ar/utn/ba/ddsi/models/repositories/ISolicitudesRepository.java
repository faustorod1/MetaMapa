package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.SolicitudDeModificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudesRepository extends JpaRepository<SolicitudDeModificacion, Long> {
    List<SolicitudDeModificacion> findAllByEstado(EstadoSolicitud estado);

}
