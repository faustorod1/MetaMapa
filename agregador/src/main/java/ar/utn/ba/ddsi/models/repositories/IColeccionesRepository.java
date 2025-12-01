package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IColeccionesRepository extends JpaRepository<Coleccion, Long> {
    Coleccion findByIdentificador(String identificador);
    void deleteByIdentificador(String identificador);
    List<Coleccion> findByFechaDeCreacionLessThanEqualOrderByFechaDeCreacionDesc(LocalDateTime fecha, Pageable pageable);
}
