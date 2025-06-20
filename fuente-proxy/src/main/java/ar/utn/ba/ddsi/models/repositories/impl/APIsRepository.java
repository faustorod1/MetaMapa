package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.API;
import ar.utn.ba.ddsi.models.repositories.IAPIsRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class APIsRepository implements IAPIsRepository {
    private List<API> APIs;
    private Long APIid = 0L;        // IDs autoincremental

    public APIsRepository() {
        APIs = new ArrayList<>();
    }

    @Override
    public API save(API api){
        this.APIid++;
        api.setId(this.APIid);
        this.APIs.add(api);
        return api;
    }

    @Override
    public List<API> findAll() {
        return this.APIs;
    }


    @Override
    public API findById(Long id){
        return this.APIs.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }
}
