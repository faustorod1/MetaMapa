package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColeccionesService implements IColeccionesService {
    private IColeccionesRepository coleccionesRepository;

    @Autowired
    public ColeccionesService(IColeccionesRepository coleccionesRepository) {
        this.coleccionesRepository = coleccionesRepository;
    }

    @Override
    public List<ColeccionOutputDTO> buscarTodos() {
        return coleccionesRepository
                .findAll()
                .stream()
                .map(this::coleccionOutputDTO)
                .toList();
    }

    private ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        ColeccionOutputDTO dto = new ColeccionOutputDTO();

        dto.setIdentificador(coleccion.getIdentificador());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
        dto.setHechos(coleccion.getHechos());
        return dto;
    }

}
