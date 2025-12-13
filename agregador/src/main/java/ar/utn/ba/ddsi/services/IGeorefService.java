package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;

import java.util.List;
import java.util.Map;

public interface IGeorefService {
    Map<Coordenada, Departamento> obtenerDepartamentos(List<Coordenada> coordenadas);
}
