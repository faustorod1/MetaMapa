package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.API;

import java.util.List;

public interface IAPIsRepository {
    List<API> findAll();
    API save(API api);
    API findById(Long id);
}
