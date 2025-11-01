package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

  @PostMapping      // Devuelve status code HTTP
  public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudDeEliminacionInputDTO dto) {
    log.info("Solicitud recibida: " + dto);
    SolicitudDeEliminacion solicitud = solicitudesService.crearSolicitud(dto);

    return switch (solicitud.getEstado()) {
      case PENDIENTE -> ResponseEntity.status(201).body("Solicitud creada con éxito"); // TODO: Devolver ID
      case RECHAZADA_POR_SPAM -> ResponseEntity.status(422).body("Solicitud rechazada por spam");
      case RECHAZADA_POR_FALTA_DE_CARACTERES -> ResponseEntity.status(422).body("Solicitud rechazada por insuficientes carácteres");
      default -> ResponseEntity.internalServerError().body("Error del servidor (ㆆ _ ㆆ)");
    };
  }

  @PatchMapping("/{id}/estado")
  public ResponseEntity<SolicitudDeEliminacionOutputDTO> modificarEstadoSolicitud(@PathVariable Long id, @RequestBody ResolucionSolicitudDeEliminacionDTO resolucion) {
    try {
      SolicitudDeEliminacion solicitud = solicitudesService.modificarEstadoSolicitud(id, resolucion);
      return ResponseEntity.ok().body(SolicitudDeEliminacionOutputDTO.fromEntity(solicitud));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudes(){
    return solicitudesService.obtenerSolicitudes();
  }


}
