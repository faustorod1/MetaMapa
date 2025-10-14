package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.IdExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long>, HechosRepositoryCustom {
    List<Hecho> findAllByidExterno_Fuente(Fuente fuente);
    List<Hecho> findAllByIdExterno_FuenteIn(List<Fuente> fuentes);

    List<Hecho> findAllByIdIn(List<Long> ids);
    List<Hecho> findAllByIdExternoIn(List<IdExterno> ids);

  List<HechoOutputDTO> findByContribuyenteId(Long contribuyenteId);
}