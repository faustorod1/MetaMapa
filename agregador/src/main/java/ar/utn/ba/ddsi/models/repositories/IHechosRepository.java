package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import java.util.List;

public interface IHechosRepository {
    public List<Hecho> findall();
}
