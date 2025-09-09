package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IHechosService {
    Mono<List<HechoOutputDTO>> buscarTodos(Map<String, String> params);
    Hecho obtenerPorId(Long id);
    Mono<List<Hecho>> getFromMetaMapa();
    Mono<Void> actualizarHechos();
    HechoOutputDTO hechoOutputDTO(Hecho hecho);
    void normalizarCategoria(List<Hecho> hechos);
    Mono<List<Hecho>> normalizarUbicacion(List<Hecho> hechos);
    Mono<List<Hecho>> normalizarTodosLosHechos();
}
