package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.API;
import ar.utn.ba.ddsi.models.entities.APIAdapters.impl.APIAdapterFuenteCatedra;
import ar.utn.ba.ddsi.models.entities.APIAdapters.impl.APIAdapterFuenteMetamapa;
import ar.utn.ba.ddsi.models.repositories.IAPIsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class APIsRepository implements IAPIsRepository {
    private List<API> APIs;
    private Long APIid = 0L;        // IDs autoincremental

    public APIsRepository() {
        APIs = new ArrayList<>();
        // API Cátedra
        save(new API(new APIAdapterFuenteCatedra("ddsi@gmail.com","ddsi2025*"),false));
        // API MetaMapa local
        save(new API(new APIAdapterFuenteMetamapa("http://localhost:8089/api"), true));
    }

    @Override
    public API save(API api){
        this.APIid++;
        api.setId(this.APIid);
        this.APIs.add(api);
        return api;
    }

    @Override
    public List<API> findAll() { return new ArrayList<>(APIs); }

    @Override
    public List<API> findAllAPI(){
        return APIs.stream().filter(api -> !api.isMetamapa()).toList();
    }

    @Override
    public List<API> findAllMetamapa(){
        return APIs.stream().filter(api -> api.isMetamapa()).toList();
    }

    @Override
    public API findByAPIid(Long id){
        return this.APIs.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public int count() {
        return this.APIs.size();
    }
}