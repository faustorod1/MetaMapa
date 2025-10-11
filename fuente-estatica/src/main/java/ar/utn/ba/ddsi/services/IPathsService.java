package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.entities.PathDataset;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


public interface IPathsService {
    List<PathDataset> obtenerPathsDesde(LocalDateTime desde);
    void guardarCSVs(List<MultipartFile> archivos);
}
