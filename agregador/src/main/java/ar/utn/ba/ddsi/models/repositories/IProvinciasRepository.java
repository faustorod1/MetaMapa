package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.ubicacion.Provincia;

import java.util.List;

public interface IProvinciasRepository {
    List<Provincia> findAll();
    Provincia findById(Long id);
    void save(Provincia provincia);
    void delete(Long id);
}
