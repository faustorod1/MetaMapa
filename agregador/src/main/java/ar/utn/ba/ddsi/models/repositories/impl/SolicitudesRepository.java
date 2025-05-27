package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Administrador;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import ar.utn.ba.ddsi.models.repositories.ISolicitudesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SolicitudesRepository implements ISolicitudesRepository {
  private List<SolicitudDeEliminacion> solicitudes = new ArrayList<SolicitudDeEliminacion>();
  private Long idActual = 0L;

  @Override
  public SolicitudDeEliminacion findById(Long id) {
    return solicitudes.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
  }

  @Override
  public SolicitudDeEliminacion save(SolicitudDeEliminacion solicitudDeEliminacion) {
    solicitudes.add(solicitudDeEliminacion);
    idActual++;
    solicitudDeEliminacion.setId(idActual);
    return solicitudDeEliminacion;
  }

  @Override
  public SolicitudDeEliminacion resolver(Long id, Administrador administradorQueResolvio, EstadoSolicitud estado) {
    final SolicitudDeEliminacion solicitud = findById(id);
    solicitud.resolver(estado, administradorQueResolvio);
    return solicitud;
  }
}
