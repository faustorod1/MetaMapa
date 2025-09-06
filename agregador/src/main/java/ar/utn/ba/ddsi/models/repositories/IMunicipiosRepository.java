package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.ubicacion.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IMunicipiosRepository extends JpaRepository<Municipio, Long> {
    List<Municipio> findAll();
    void delete(Long id);
}
