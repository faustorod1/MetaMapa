package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IHechosService {
    Page<HechoOutputDTO> buscarTodos(Map<String, String> params, Pageable pageable);
    HechoOutputDTO buscarHecho(Long id);
    HechoOutputDTO buscarHechoNoEliminado(Long id);
    Hecho obtenerPorId(Long id);
    Page<Hecho> obtenerPorColeccion(String identificadorColeccion, Map<String, String> params, Pageable pageable);
    Hecho obtenerNoEliminadoPorId(Long id);
    List<Hecho> getFromMetaMapa();
    List<Hecho> actualizarListaConHechosMetamapa(List<Hecho> hechosLocales);
    List<HechoOutputDTO> buscarHechoDe(Long contribuyenteId);
    void actualizarHechos();
    void normalizarCategoria(List<Hecho> hechos);
    void normalizarUbicacion(List<Hecho> hechos);
    List<HechoOutputDTO> buscarHechos(LocalDateTime fecha, Integer cantidad_obtener);
    List<Long> buscarIdsExternosDinamica();
    HechoOutputDTO buscarHechoDinamica (Long id_externo);
}
