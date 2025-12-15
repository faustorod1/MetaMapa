package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.FiltroInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.*;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.models.entities.Fuente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IColeccionesService {
    List<ColeccionOutputDTO> buscarTodos();
    ColeccionOutputDTO buscarPorId(String identificador);
    Page<HechoOutputDTO> buscarHechosPorColeccion(String identificador, Map<String, String> params, Pageable pageable);
    ColeccionConHechosOutputDTO buscarColeccionConHechos(String identificador, Map<String, String> params, Pageable pageable);
    ColeccionOutputDTO crearColeccion(ColeccionInputDTO input);
    ColeccionOutputDTO updateColeccion(ColeccionInputDTO input);
    ColeccionOutputDTO updateCriterio(String identificador, CriterioInputDTO criterioInputDTO);
    ColeccionOutputDTO updateConsenso(String identificador, String tipoDeConsenso);
    void eliminarColeccion (String identificador);
    ColeccionOutputDTO updateFuentes(String identificador, List<Long> fuentes);
    void consensuarColecciones();
    List<FuenteDTO> buscarFuentes();
    List<ColeccionOutputDTO> buscarUltimasColecciones (LocalDateTime fecha, Integer cantidad_colecciones_destacadas);
    List<ColeccionConHechosCuradosOutputDTO> buscarTodosConHechosCurados();
    List<String> buscarIdentificadores();
    public ColeccionConHechosOutputDTO filtrarColeccion(String identificador, List<FiltroInputDTO> filtros, Pageable pageable);

    }
