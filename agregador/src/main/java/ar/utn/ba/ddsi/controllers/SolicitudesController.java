package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {
  private ISolicitudesService solicitudesService;

  @Autowired
  public SolicitudesController(ISolicitudesService solicitudesService) {
    this.solicitudesService = solicitudesService;
  }

  @PostMapping
  public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudDeEliminacionInputDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
    Long contribuyenteId = userDetails.getId();
    dto.setSolicitanteId(contribuyenteId);
    SolicitudDeEliminacion solicitud = solicitudesService.crearSolicitud(dto);

    return switch (solicitud.getEstado()) {
      case PENDIENTE -> ResponseEntity.status(201).body("Solicitud creada con éxito"); // TODO: Devolver ID
      case RECHAZADA_POR_SPAM -> ResponseEntity.status(422).body("Solicitud rechazada por spam");
      case RECHAZADA_POR_FALTA_DE_CARACTERES -> ResponseEntity.status(422).body("Solicitud rechazada por insuficientes carácteres");
      default -> ResponseEntity.internalServerError().body("Error del servidor");
    };
  }

  @PatchMapping("eliminacion/{id}/estado")
  public ResponseEntity<SolicitudDeEliminacionOutputDTO> modificarEstadoSolicitud(@PathVariable Long id, @RequestBody ResolucionSolicitudDeEliminacionDTO resolucion, @AuthenticationPrincipal CustomUserDetails userDetails) {
    resolucion.setAdministradorQueResolvioId(userDetails.getId());
    try {
      SolicitudDeEliminacion solicitud = solicitudesService.modificarEstadoSolicitud(id, resolucion);
      return ResponseEntity.ok().body(SolicitudDeEliminacionOutputDTO.fromEntity(solicitud));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/eliminacion")
  public List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudesDeEliminacion(){
    return solicitudesService.obtenerSolicitudesDeEliminacion();
  }

  @GetMapping("/eliminacion/{id}")
  public SolicitudDeEliminacionOutputDTO obtenerSolicitudDeEliminacionPorId(@PathVariable Long id){
    return solicitudesService.obtenerSolicitudDeEliminacionPorID(id);
  }

  @GetMapping("/idsEliminacionPendientes")
  public List<Long> obtenerIDsEliminacionPendientes(){
    return solicitudesService.obtenerIDsEliminacionPendientes();
  }


}
