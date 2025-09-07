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
    SolicitudDeEliminacion solicitud = solicitudDeEliminacionFromDTO(solicitudDto);
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
          .map(this::solicititudDeEliminacionToDTO)
          .toList();

  }

  @Override
  public SolicitudDeEliminacion solicitudDeEliminacionFromDTO(SolicitudDeEliminacionInputDTO dto) throws DescripcionSolicitudException {
    Hecho hecho = hechosService.obtenerPorId(dto.getHechoId());
    return SolicitudDeEliminacion.builder()
            .descripcion(dto.getDescripcion())
            .hecho(hecho)
            .solicitante(contribuyenteFromContribuyenteDTO(dto.getSolicitante()))
            .build();
     //new SolicitudDeEliminacion(hecho, dto.getDescripcion(), contribuyenteFromContribuyenteDTO(dto.getSolicitante()));
  }

  private Contribuyente contribuyenteFromContribuyenteDTO(ContribuyenteDTO dto) {
    return new Contribuyente(dto.getId(), dto.getNombre(), dto.getApellido(), LocalDate.parse(dto.getFechaDeNacimiento(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
  }
  private ContribuyenteDTO contribuyenteToDTO(Contribuyente contribuyente) {
    ContribuyenteDTO dto = new ContribuyenteDTO();
    dto.setId(contribuyente.getId());
    dto.setNombre(contribuyente.getNombre());
    dto.setApellido(contribuyente.getApellido());
    dto.setFechaDeNacimiento(contribuyente.getFechaNacimiento().toString());
    return dto;
  }

  private SolicitudDeEliminacionOutputDTO solicititudDeEliminacionToDTO(SolicitudDeEliminacion solicitud) {
    SolicitudDeEliminacionOutputDTO dto = new SolicitudDeEliminacionOutputDTO();
    dto.setId(solicitud.getId());
    dto.setDescripcion(solicitud.getDescripcion());
    dto.setHechoId(solicitud.getHecho().getId());
    dto.setFechaDeCarga(solicitud.getFechaDeCarga());
    dto.setFechaDeResolucion(solicitud.getFechaDeResolucion());
    dto.setEstado(solicitud.getEstado());
    dto.setSolicitante(contribuyenteToDTO(solicitud.getSolicitante()));
    dto.setAdministradorQueResolvio(solicitud.getAdministradorQueResolvio());
    return dto;
  }
}
