package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    @Autowired
    private PathsService pathsService;

    private final static LocalDateTime ancientDate = LocalDateTime.parse("1000-01-01T00:00:00");

    // --- Métodos expuestos al controller -------------------------------------------------------------------------------

    @Override
    public Page<HechoOutputDTO> obtenerHechosCargadosDesde(LocalDateTime desde, Pageable pageable) {
        List<PathDataset> paths = pathsService.obtenerPathsDesde(desde);
        List<HechoOutputDTO> resultadoPagina = new ArrayList<>();

        long totalGlobal = 0;
        long startPage = pageable.getOffset();
        long endPage = pageable.getOffset() + pageable.getPageSize();

        for (PathDataset path : paths) {
            LectorDeCSV lector = new LectorDeCSV(path);

            int cantidadEnArchivo = lector.contarHechos();

            long inicioArchivoGlobal = totalGlobal;
            long finArchivoGlobal = totalGlobal + cantidadEnArchivo;

            boolean hayInterseccion = (finArchivoGlobal > startPage) && (inicioArchivoGlobal < endPage);

            if (hayInterseccion) {
                long skipGlobal = Math.max(startPage, inicioArchivoGlobal);
                long endGlobal = Math.min(endPage, finArchivoGlobal);

                int skipLocal = (int) (skipGlobal - inicioArchivoGlobal);
                int limitLocal = (int) (endGlobal - skipGlobal);

                List<Hecho> hechosParciales = lector.getHechosPaginados(skipLocal, limitLocal);

                resultadoPagina.addAll(hechosParciales.stream().map(this::hechoOutputDTO).toList());
            }

            totalGlobal += cantidadEnArchivo;
        }

        return new PageImpl<>(resultadoPagina, pageable, totalGlobal);
    }



    //---- Conversiones DTO -------------------------------------------------------------------------------

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());
        dto.setTipoDeFuente("ESTATICA");
        dto.setSubfuenteId(hecho.getIdDataset());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria().getNombre());
        dto.setContenidosMultimedia(null);
        dto.setOrigen(OrigenHecho.DATASET);
        dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setFechaUltimaActualizacion(hecho.getFechaDeCarga());
        dto.setEliminado(false);
        dto.setContribuyenteId(null);
        dto.setEtiquetas(new HashSet<>());

        return dto;
    }
}
