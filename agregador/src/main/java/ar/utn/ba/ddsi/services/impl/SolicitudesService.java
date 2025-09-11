package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.ISolicitudesRepository;
import ar.utn.ba.ddsi.services.ISolicitudesService;
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

  //ResponseEntity.ok(cuerpo);
  @Override
  public ResponseEntity<String> crearSolicitud(SolicitudDeEliminacionInputDTO solicitudDto) {
    Hecho hecho = hechosService.obtenerPorId(solicitudDto.getHechoId());
    SolicitudDeEliminacion solicitud = solicitudDto.toEntity(hecho);
    solicitudesRepository.save(solicitud);
    return switch (solicitud.getEstado()) {
      case PENDIENTE -> ResponseEntity.ok("Solicitud creada con éxito");
      case RECHAZADA_POR_SPAM -> ResponseEntity.status(422).body("Solicitud rechazada por spam");
      case RECHAZADA_POR_FALTA_DE_CARACTERES -> ResponseEntity.status(422).body("Solicitud rechazada por insuficientes carácteres");
      default -> ResponseEntity.internalServerError().body("Error del servidor (ㆆ _ ㆆ)");
    };
  }

  @Override
  public void modificarEstadoSolicitud(Long id, ResolucionSolicitudDeEliminacionDTO resolucionDto) {
    solicitudesRepository.findById(id).ifPresent(solicitudDeEliminacion -> {
      solicitudDeEliminacion.resolver(resolucionDto.getEstadoSolicitud(), resolucionDto.getAdministradorQueResolvio());
      solicitudesRepository.save(solicitudDeEliminacion);
      hechosService.guardarCambios(solicitudDeEliminacion.getHecho());
      hechosService.eliminarHechoEnLasFuentes(solicitudDeEliminacion.getHecho());
    });
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
