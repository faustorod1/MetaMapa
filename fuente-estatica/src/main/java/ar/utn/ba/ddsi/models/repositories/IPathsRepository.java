package ar.utn.ba.ddsi.models.repositories;

import java.util.List;

public interface IPathsRepository {
    public List<String> findAll();
    public String save(String path);
}
