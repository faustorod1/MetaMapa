package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IDepartamentosRepository extends JpaRepository<Departamento, Long> {
    List<Departamento> findAll();
}
