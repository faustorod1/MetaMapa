package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;


@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    @Query("""
        SELECT h
        FROM Hecho h
        WHERE h.idExterno LIKE CONCAT(:fuente, ':%')
    """)
    List<Hecho> findFromFuente(String fuente);

    List<Hecho> saveAll(List<Hecho> hechos);

    default List<Hecho> findFromFuentes(List<String> fuentes) {
        return findByIdExternoStartingWith(fuentes.stream().map(f -> f + ":").toList());
    }
    /*
    @Query("""
    SELECT h
    FROM Hecho h
    WHERE
        """ +
            " :#{#fuentes.size() == 0 ? '1=0' : ''} " + // evita error si lista vacía
            " OR " +
            " (" +
            "   " +
            "   " +
            "   " +
            "   " +
            " )"
    )
    List<Hecho> findFromFuentes(@Param("fuentes") List<String> fuentes);
    *//*
    public List<Hecho> findFromFuentes(List<String> fuentes) {
        return fuentes.stream()
                .map(f -> findFromFuente(f)) // usa el método que sí funciona
                .flatMap(List::stream)
                .toList();
    }*/

    List<Hecho> findByIdExternoStartingWith(List<String> fuentes);
}