package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Etiqueta;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

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
        if (hecho.getCategoria() != null) dto.setCategoria(hecho.getCategoria().getNombre());
        if (hecho.getContenidoMultimedia() != null) dto.setContenidoMultimedia(hecho.getContenidoMultimedia().getPathImagen());
        if (hecho.getOrigen() != null) dto.setOrigen(hecho.getOrigen().ordinal());
        if (hecho.getLugarAcontecimiento() != null) dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento().comoArray());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        if (hecho.getContribuyente() != null) dto.setContribuyente(hecho.getContribuyente().getId());
        dto.setSolicitudesDeEliminacion(hecho.getSolicitudesDeEliminacion());
        dto.setEtiquetas(
                hecho.getEtiquetas()
                        .stream()
                        .map(Etiqueta::nombre)
                        .collect(Collectors.toCollection(HashSet::new))
        );
        return dto;
    }
}

