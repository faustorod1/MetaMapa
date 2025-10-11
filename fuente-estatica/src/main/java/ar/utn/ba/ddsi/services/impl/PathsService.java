package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.PathDataset;
import ar.utn.ba.ddsi.models.repositories.IPathsRepository;
import ar.utn.ba.ddsi.services.IPathsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PathsService implements IPathsService {

    @Autowired
    private IPathsRepository pathsRepository;
    private final String datasetFolder;

    public PathsService(@Value("${dataset.folder}") String datasetFolder) {
        this.datasetFolder = datasetFolder;
    }

    @Override
    public void guardarCSVs(List<MultipartFile> archivos) {

        for (MultipartFile file : archivos) {
            System.out.println("Archivo recibido: " + file.getOriginalFilename());
        }

        for (MultipartFile archivo : archivos) {
            if (archivo.isEmpty()) {
                continue;
            }
            String nombreArchivo = archivo.getOriginalFilename();
            String pathArchivo = this.datasetFolder + File.separator + nombreArchivo;
            File archivoDestino = new File(pathArchivo);        // Creación de objeto File para representar ruta del archivo

            try {
                archivo.transferTo(archivoDestino);     // guardado de archivo
                PathDataset pathDataset = new PathDataset(pathArchivo, LocalDateTime.now());
                pathsRepository.save(pathDataset);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public List<PathDataset> obtenerPathsDesde(LocalDateTime desde) {
        return pathsRepository.findAllByFechaCargaAfter(desde);
    }


}
