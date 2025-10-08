package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> buscarTodos();
    List<HechoOutputDTO> obtenerHechosCargadosDesde(LocalDateTime desde);
    void marcarComoELiminado(Long id);
    void guardarCSVs(List<MultipartFile> archivos);
}
