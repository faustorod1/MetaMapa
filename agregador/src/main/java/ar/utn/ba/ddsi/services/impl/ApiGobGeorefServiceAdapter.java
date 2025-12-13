package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.commons.DivisorEnLotes;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorefRequestDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorefRequestMultipleDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorreferenciacionDTO;
import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;
import ar.utn.ba.ddsi.models.repositories.IDepartamentosRepository;
import ar.utn.ba.ddsi.services.IGeorefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiGobGeorefServiceAdapter implements IGeorefService {
    private final WebClient webClientGeoref;
    private final IDepartamentosRepository departamentosRepository;

    @Autowired
    public ApiGobGeorefServiceAdapter(@Value("${georef.api.base-url}") String baseUrl,
                                      IDepartamentosRepository departamentosRepository) {
        this.webClientGeoref = WebClient.builder().baseUrl(baseUrl).build();
        this.departamentosRepository = departamentosRepository;
    }


    @Override
    public Map<Coordenada, Departamento> obtenerDepartamentos(List<Coordenada> coordenadas) {
        if (coordenadas == null || coordenadas.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Departamento> departamentosConocidos = departamentosRepository.findAll();

        List<List<Coordenada>> lotes = DivisorEnLotes.dividir(coordenadas, 300);

        List<Map<Coordenada, Departamento>> resultadosParciales = Flux.fromIterable(lotes)
                .concatMap(lote -> consultarApiYMapear(lote, departamentosConocidos))
                .collectList()
                .block();

        Map<Coordenada, Departamento> resultadoFinal = new HashMap<>();
        if (resultadosParciales != null) {
            resultadosParciales.forEach(resultadoFinal::putAll);
        }

        return resultadoFinal;
    }


    private Mono<Map<Coordenada, Departamento>> consultarApiYMapear(List<Coordenada> lote, List<Departamento> departamentosRef) {
        List<GeorefRequestDTO> requests = lote.stream()
                .map(GeorefRequestDTO::fromCoordenada)
                .toList();

        GeorefRequestMultipleDTO body = new GeorefRequestMultipleDTO();
        body.setUbicaciones(requests);

        return webClientGeoref.post()
                .uri("/ubicacion")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GeorreferenciacionDTO.class)
                .map(response -> mapearRespuesta(response, departamentosRef));
    }


    private Map<Coordenada, Departamento> mapearRespuesta(GeorreferenciacionDTO dto, List<Departamento> departamentosRef) {
        Map<Coordenada, Departamento> mapa = new HashMap<>();
        if (dto.getResultados() == null) return mapa;

        dto.getResultados().forEach(res -> {
            var ubicacion = res.getUbicacion();
            if (ubicacion != null && ubicacion.getLat() != 0 && ubicacion.getLon() != 0) {

                Departamento deptoEncontrado = departamentosRef.stream()
                        .filter(d -> d.getNombre().equalsIgnoreCase(ubicacion.getDepartamento_nombre()) &&
                                d.getProvincia().getNombre().equalsIgnoreCase(ubicacion.getProvincia_nombre()))
                        .findFirst()
                        .orElse(null);

                if (deptoEncontrado != null) {
                    Coordenada coord = new Coordenada(ubicacion.getLat(), ubicacion.getLon());
                    mapa.put(coord, deptoEncontrado);
                }
            }
        });
        return mapa;
    }
}
