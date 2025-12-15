package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByAPIidAndIdExternoIn(Long apiId, List<String> idsExternos);
    Page<Hecho> findByFechaObtencionAfter(LocalDateTime fechaObtencion, Pageable pageable);
    List<Hecho> findByAPIidIn(List<Long> apiIds);
    List<Hecho> findByAPIidInAndFechaObtencionAfter(List<Long> apiIds, LocalDateTime fechaObtencion);

    // Trae el contenido multimedia y etiquetas de forma eager
    @Query("SELECT h FROM Hecho h " +
            "LEFT JOIN FETCH h.etiquetas " +
            "LEFT JOIN FETCH h.contenidoMultimedia " +
            "WHERE h.APIid = :apiId AND h.idExterno IN :idsExternos")
    List<Hecho> findByAPIidAndIdExternoInWithCollections(@Param("apiId") Long apiId, @Param("idsExternos") List<String> idsExternos);
}
