package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> buscarTodos();
    List<HechoOutputDTO> obtenerHechosCargadosDesde(LocalDateTime desde);
}
