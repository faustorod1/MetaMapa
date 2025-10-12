package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDinamicaService {
    void cargarHecho(HechoOutputDTO hecho, List<MultipartFile> imagenes);
}
