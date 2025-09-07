package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;

public interface HechosRepositoryCustom {
    List<Hecho> findFromFuentes(List<String> fuentes);
}
