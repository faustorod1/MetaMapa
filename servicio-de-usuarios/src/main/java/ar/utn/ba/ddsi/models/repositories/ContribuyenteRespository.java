package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContribuyenteRespository extends JpaRepository<Contribuyente, Long> {
}
