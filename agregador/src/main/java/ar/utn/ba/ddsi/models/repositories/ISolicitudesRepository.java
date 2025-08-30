package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Administrador;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;

import java.util.List;

public interface ISolicitudesRepository{
  SolicitudDeEliminacion findById(Long id);
  SolicitudDeEliminacion save(SolicitudDeEliminacion solicitudDeEliminacion);
  SolicitudDeEliminacion resolver(Long id, Administrador administradorQueResolvio, EstadoSolicitud estado);
  List<SolicitudDeEliminacion> findAll();
}
