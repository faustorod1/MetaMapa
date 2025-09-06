package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.ubicacion.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProvinciasRepository extends JpaRepository<Provincia, Long> {
    List<Provincia> findAll();
    void delete(Long id);
}
