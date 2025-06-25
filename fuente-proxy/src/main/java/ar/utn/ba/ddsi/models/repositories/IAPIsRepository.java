package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.API;

import java.util.List;

public interface IAPIsRepository {
    List<API> findAllAPI();
    List<API> findAllMetamapa();
    API save(API api);
    API findByAPIid(Long id);

}
