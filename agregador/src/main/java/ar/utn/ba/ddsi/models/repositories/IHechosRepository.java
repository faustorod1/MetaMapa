package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long>, HechosRepositoryCustom {
    @Query("""
        SELECT h
        FROM Hecho h
        WHERE h.idExterno LIKE CONCAT(:fuente, ':%')
    """)
    List<Hecho> findFromFuente(String fuente);

    void deleteAll();
}