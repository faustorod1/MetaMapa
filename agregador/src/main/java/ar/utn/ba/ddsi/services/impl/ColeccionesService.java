package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.FiltroOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        dto.setCriterioDePertenencia(criterioOutputDTO(coleccion.getCriterioDePertenencia()));
        dto.setHechos(coleccion.getHechos());
        return dto;
    }

    private CriterioOutputDTO criterioOutputDTO(Criterio criterio) {
        CriterioOutputDTO dto = new CriterioOutputDTO();
        dto.setFiltros(
                criterio.getFiltros()
                .stream()
                .map(this::filtroOutputDTO)
                .toList()
        );
        return dto;
    }

    private FiltroOutputDTO filtroOutputDTO(Filtro filtro) {
        FiltroOutputDTO dto = new FiltroOutputDTO();
        if (filtro instanceof FiltroPorTitulo f) {
            dto.setTipoDeFiltro("titulo");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("titulo", f.getTitulo());
        } else if (filtro instanceof FiltroPorDescripcion f) {
            dto.setTipoDeFiltro("descripcion");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("descripcion", f.getDescripcion());
        } else if (filtro instanceof FiltroPorCategoria f) {
            dto.setTipoDeFiltro("categoria");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("categoria", f.getCategoria());
        } else if (filtro instanceof FiltroPorUbicacion f) {
            dto.setTipoDeFiltro("ubicacion");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("lugar", f.getLugar().comoArray());
        } else if (filtro instanceof FiltroPorFechaHecho f) {
            dto.setTipoDeFiltro("fechaHecho");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("desde", f.getDesde());
            dto.getParametros().put("hasta", f.getHasta());
        } else if (filtro instanceof FiltroPorFechaDeCarga f) {
            dto.setTipoDeFiltro("fechaDeCarga");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("desde", f.getDesde());
            dto.getParametros().put("hasta", f.getHasta());
        } else if (filtro instanceof FiltroPorEliminados f) {
            dto.setTipoDeFiltro("eliminados");
            dto.setParametros(new HashMap<>());
        } else {
            throw new RuntimeException("Tipo de filtro no encontrado");
        }

        return dto;
    }




}
