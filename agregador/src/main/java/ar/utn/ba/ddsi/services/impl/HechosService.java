package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;

    @Autowired
    public HechosService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    @Override
    public List<HechoOutputDTO> buscarTodos(){
        return hechosRepository
                .findAll()
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setContenidoMultimedia(hecho.getContenidoMultimedia());
        dto.setOrigen(hecho.getOrigen());
        dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setEliminado(hecho.isEliminado());
        dto.setSolicitudesDeEliminacion(hecho.getSolicitudesDeEliminacion());
        dto.setEtiquetas(hecho.getEtiquetas());
        return dto;
    }
}

