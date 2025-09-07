package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {

  @Modifying
  @Query("""
        UPDATE Hecho h 
        SET h.eliminado = true 
        WHERE h.id = :id
    """)
  void marcarComoEliminado(Long id);
}
