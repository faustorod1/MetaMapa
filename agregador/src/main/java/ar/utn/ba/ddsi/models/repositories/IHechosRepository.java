package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;


@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findAll();

    @Query("""
        SELECT h
        FROM Hecho h
        WHERE h.idExterno LIKE CONCAT(:fuente, ':%')
    """)
    List<Hecho> findFromFuente(String fuente);

    List<Hecho> saveAll(List<Hecho> hechos);

    void deleteAll();

    default List<Hecho> findFromFuentes(List<String> fuentes) {
        return findByIdExternoStartingWith(fuentes.stream().map(f -> f + ":").toList());
    }

    List<Hecho> findByIdExternoStartingWith(List<String> fuentes);
}