package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import java.util.List;

public interface IColeccionesRepository {
    List<Coleccion> findAll();
    Coleccion findByIdentificador(String identificador);
    void save(Coleccion coleccion);
    void delete(String identificador);
}
