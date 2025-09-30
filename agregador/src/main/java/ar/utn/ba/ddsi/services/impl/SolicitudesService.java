package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.ISolicitudesRepository;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    solicitudesRepository.save(solicitud);

    return solicitud;
  }

  @Override
  public SolicitudDeEliminacion modificarEstadoSolicitud(Long id, ResolucionSolicitudDeEliminacionDTO resolucionDto) {
    SolicitudDeEliminacion solicitud = solicitudesRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + id));

    solicitud.resolver(resolucionDto.getEstadoSolicitud(), resolucionDto.getAdministradorQueResolvio());
    solicitudesRepository.save(solicitud);
    hechosService.guardarCambios(solicitud.getHecho());
    hechosService.eliminarHechoEnLasFuentes(solicitud.getHecho());

    return solicitud;
  }

  @Override
  public List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudes(){
      return solicitudesRepository.
          findAll()
          .stream()
          .map(SolicitudDeEliminacionOutputDTO::fromEntity)
          .toList();
  }
}
