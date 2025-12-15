package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.PathDataset;
import ar.utn.ba.ddsi.models.repositories.IPathsRepository;
import ar.utn.ba.ddsi.services.IPathsService;
import ar.utn.ba.ddsi.services.internal.CSVUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PathsService implements IPathsService {

    private final IPathsRepository pathsRepository;
    private final CSVUploaderService csvUploaderService;

    @Autowired
    public PathsService(IPathsRepository pathsRepository, CSVUploaderService csvUploaderService) {
        this.pathsRepository = pathsRepository;
        this.csvUploaderService = csvUploaderService;
    }

    @Override
    public Long guardarCSVs(List<MultipartFile> archivos) {

        for (MultipartFile file : archivos) {
            System.out.println("Procesando archivo: " + file.getOriginalFilename());
        }

        for (MultipartFile archivo : archivos) {
            if (archivo.isEmpty()) {
                continue;
            }

            try {
                Map uploadResult = csvUploaderService.uploadCsv(archivo);
                String urlSegura = (String) uploadResult.get("secure_url");
                System.out.println("Subido exitosamente a: " + urlSegura);

                PathDataset pathDataset = new PathDataset(urlSegura, LocalDateTime.now());
                pathsRepository.save(pathDataset);

            } catch (IOException ex) {
                throw new RuntimeException("Error al subir el archivo: " + archivo.getOriginalFilename(), ex);
            }
        }
        return 1L;
    }


    @Override
    public List<PathDataset> obtenerPathsDesde(LocalDateTime desde) {
        return pathsRepository.findAllByFechaCargaAfter(desde);
    }


}
