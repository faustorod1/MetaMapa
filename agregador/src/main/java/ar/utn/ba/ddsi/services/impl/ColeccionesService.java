package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColeccionesService implements IColeccionesService {
    @Autowired
    private IColeccionesRepository coleccionesRepository;

    @Override
    public List<ColeccionOutputDTO> buscarTodos() {
        return coleccionesRepository.findAll();
    }

}
