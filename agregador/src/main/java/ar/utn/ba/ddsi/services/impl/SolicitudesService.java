package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.DetectorDeSpam;
import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.ISolicitudesRepository;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SolicitudesService implements ISolicitudesService {
  private final HechosService hechosService;
  private final ISolicitudesRepository solicitudesRepository;

  public SolicitudesService(ISolicitudesRepository solicitudesRepository, HechosService hechosService) {
    this.solicitudesRepository = solicitudesRepository;
    this.hechosService = hechosService;
  }

  @Override
  public SolicitudDeEliminacion crearSolicitud(SolicitudDeEliminacionInputDTO solicitudDto) {
    Hecho hecho = hechosService.obtenerPorId(solicitudDto.getHechoId());


    SolicitudDeEliminacion solicitud = solicitudDto.toEntity(hecho);
    String descripcion = solicitudDto.getDescripcion();

    if (descripcion == null || descripcion.length() < 500) {
      solicitud.setEstado(EstadoSolicitud.RECHAZADA_POR_FALTA_DE_CARACTERES);
    } else if (DetectorDeSpam.esSpam(descripcion)) {
      solicitud.setEstado(EstadoSolicitud.RECHAZADA_POR_SPAM);
    } else {
      solicitud.setEstado(EstadoSolicitud.PENDIENTE);
      solicitudesRepository.save(solicitud);
    }
    return solicitud;
  }

  @Override
  public SolicitudDeEliminacion modificarEstadoSolicitud(Long id, ResolucionSolicitudDeEliminacionDTO resolucionDto) {
    SolicitudDeEliminacion solicitud = solicitudesRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + id));

    solicitud.resolver(resolucionDto.getEstadoSolicitud(), resolucionDto.getAdministradorQueResolvioId());
    solicitudesRepository.save(solicitud);
    hechosService.guardarCambios(solicitud.getHecho());
    hechosService.eliminarHechoEnLasFuentes(solicitud.getHecho());

    return solicitud;
  }

  @Override
  public List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudesDeEliminacion(){
      return solicitudesRepository
          .findAll()
          .stream()
          .map(SolicitudDeEliminacionOutputDTO::fromEntity)
          .toList();
  }

  @Override
  public List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudesDeEliminacionPendientes(){
      return obtenerSolicitudesDeEliminacion().stream()
              .filter(solicitud -> solicitud.getEstado().equals(EstadoSolicitud.PENDIENTE))
              .toList();
  }

  @Override
  public SolicitudDeEliminacionOutputDTO obtenerSolicitudDeEliminacionPorID(Long id){
    return obtenerSolicitudesDeEliminacionPendientes().stream()
            .filter(solicitud -> solicitud.getId().equals(id))
            .findFirst()
            .orElse(null);
  }

  @Override
  public List<Long> obtenerIDsEliminacionPendientes(){
    return obtenerSolicitudesDeEliminacionPendientes().stream()
            .map(SolicitudDeEliminacionOutputDTO::getId).toList();
  }

  @Override
  public Long obtenerCantidadAceptadas(){
    return solicitudesRepository.countByEstado(EstadoSolicitud.ACEPTADA);
  }

  @Override
  public Long obtenerCantidadRechazadas(){
    return solicitudesRepository.countByEstado(EstadoSolicitud.RECHAZADA);
  }
}