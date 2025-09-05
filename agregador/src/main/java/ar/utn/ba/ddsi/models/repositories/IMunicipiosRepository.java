package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.ubicacion.Municipio;

import java.util.List;

public interface IMunicipiosRepository {
    List<Municipio> findAll();
    Municipio findById(Long id);
    void save(Municipio municipio);
    void delete(Long id);
}
