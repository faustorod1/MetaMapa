package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByAPIidAndIdExternoIn(Long apiId, List<String> idsExternos);
    Page<Hecho> findByFechaUltimaActualizacionAfter(LocalDateTime fechaActualizacion, Pageable pageable);
    List<Hecho> findByAPIidIn(List<Long> apiIds);
    List<Hecho> findByAPIidInAndFechaUltimaActualizacionAfter(List<Long> apiIds, LocalDateTime fechaActualizacion);
}
