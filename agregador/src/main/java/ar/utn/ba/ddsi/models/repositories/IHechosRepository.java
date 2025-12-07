package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.IdExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
    List<Hecho> findAllByidExterno_Fuente(Fuente fuente);
    List<Hecho> findAllByIdExterno_FuenteIn(List<Fuente> fuentes);

    List<Hecho> findAllByIdIn(List<Long> ids);
    List<Hecho> findAllByIdExternoIn(List<IdExterno> ids);
    List<Hecho> findByContribuyenteId(Long contribuyenteId);
    // Busca hechos donde la fecha sea <= a la fecha dada, ordenados por fecha descendente
    List<Hecho> findByFechaDeCargaLessThanEqualOrderByFechaDeCargaDesc(LocalDateTime fechaDeCarga, Pageable pageable);
}