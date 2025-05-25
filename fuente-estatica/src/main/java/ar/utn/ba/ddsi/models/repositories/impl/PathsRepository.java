package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.repositories.IPathsRepository;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Data
@Repository
public class PathsRepository implements IPathsRepository {
    private List<String> paths; //Por ahora son strings, mas adelante puede ser que debamos cambiarlo

    public PathsRepository(){
        this.paths = new ArrayList<>();
    }

    @Override
    public List<String> findAll(){
        return this.paths;
    }

    @Override
    public String save(String path){
        this.paths.add(path);
        return path;
    }
}
