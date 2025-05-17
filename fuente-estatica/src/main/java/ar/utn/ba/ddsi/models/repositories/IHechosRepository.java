package ar.utn.ba.ddsi.models.repositories;

import java.util.List;
import ar.utn.ba.ddsi.models.entities.Hecho;

public interface IHechosRepository {
    public Hecho findById(Long id);
    public List<Hecho> findAll();
}
