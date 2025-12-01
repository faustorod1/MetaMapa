package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IHechosService {
    List<HechoOutputDTO> buscarTodos(Map<String, String> params);
    HechoOutputDTO buscarHecho(Long id);
    Hecho obtenerPorId(Long id);
    List<Hecho> getFromMetaMapa();
    List<Hecho> actualizarListaConHechosMetamapa(List<Hecho> hechosLocales);
    List<HechoOutputDTO> buscarHechoDe(Long contribuyenteId);
    void actualizarHechos();
    void normalizarCategoria(List<Hecho> hechos);
    void normalizarUbicacion(List<Hecho> hechos);
    List<HechoOutputDTO> buscarHechos(LocalDateTime fecha, Integer cantidad_obtener);
}
