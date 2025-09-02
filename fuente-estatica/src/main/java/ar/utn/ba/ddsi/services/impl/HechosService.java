package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    @Autowired
    private IHechosRepository hechosRepository;
    @Autowired
    private PathsService pathsService;

    // --- Métodos expuestos al controller -------------------------------------------------------------------------------

    @Override
    public List<HechoOutputDTO> obtenerHechosCargadosDesde(LocalDateTime desde) {
        List<PathDataset> paths = pathsService.obtenerPathsDesde(desde);
        List<Hecho> hechos = Collections.synchronizedList(new ArrayList<>());

        paths.parallelStream().forEach(path -> {
            hechos.addAll(hechosRepository.findAllFrom(path));
        });
        return hechos
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    @Override
    public List<HechoOutputDTO> buscarTodos() {
        return this.obtenerHechosCargadosDesde(LocalDateTime.MIN);
    }

    //---- Métodos de trabajo interno -------------------------------------------------------------------------------

    @Override
    public void marcarComoELiminado(Long id) {
        hechosRepository.marcarComoEliminado(id);
    }


    //---- Conversiones DTO -------------------------------------------------------------------------------

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(String.format("estatica:%s:%s", hecho.getIdDataset(), hecho.getId()));
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setContenidoMultimedia(null);
        dto.setOrigen(OrigenHecho.DATASET);
        dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setFechaUltimaActualizacion(hecho.getFechaDeCarga());
        dto.setEliminado(false);
        dto.setContribuyenteId(null);
        dto.setEtiquetas(new HashSet<Etiqueta>());

        return dto;
    }
}
