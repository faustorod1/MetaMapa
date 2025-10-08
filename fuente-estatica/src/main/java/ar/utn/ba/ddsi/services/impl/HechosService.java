package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.models.repositories.impl.PathsRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    @Autowired
    private PathsRepository pathsRepository;

    private final String carpetaDestino = "src/main/resources/updates";


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


    // PROPUESTA: guardado de CSVs
    @Override
    public void guardarCSVs(List<MultipartFile> archivos) {

        for (MultipartFile archivo : archivos) {
            if (archivo.isEmpty()) {
                continue;
            }
            String nombreArchivo = archivo.getOriginalFilename();
            String pathArchivo = carpetaDestino + File.separator + nombreArchivo;
            File archivoDestino = new File(pathArchivo);        // Creación de objeto File para representar ruta del archivo

            try {
                archivo.transferTo(archivoDestino);     // guardado de archivo
                PathDataset pathDataset = new PathDataset(null, pathArchivo, LocalDateTime.now());
                pathsRepository.save(pathDataset);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //---- Métodos de trabajo interno -------------------------------------------------------------------------------

    @Override
    public void marcarComoELiminado(Long id) {
        hechosRepository.marcarComoEliminado(id);
    }


    //---- Conversiones DTO -------------------------------------------------------------------------------

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());
        dto.setTipoDeFuente("estatica");
        dto.setSubFuenteId(hecho.getIdDataset());
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
