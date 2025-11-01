package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudCreadaDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudResueltaDTO;
import ar.utn.ba.ddsi.models.exceptions.NoHaySolicitudPendienteException;
import ar.utn.ba.ddsi.models.exceptions.SolicitudFueraDePlazoException;
import ar.utn.ba.ddsi.models.exceptions.SolicitudYaProcesadaException;
import ar.utn.ba.ddsi.models.exceptions.UnauthorizedException;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("api/solicitudes")
public class SolicitudesController {
  private ISolicitudesService solicitudesService;

  public SolicitudesController(ISolicitudesService solicitudesService) {
    this.solicitudesService = solicitudesService;
  }


  @PutMapping("/{idHecho}")
  public ResponseEntity<?> modificarHecho (@PathVariable Long idHecho, @RequestBody HechoInputDTO hechoNuevo, @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      SolicitudCreadaDTO dto = solicitudesService.crearSolModificacion(idHecho, hechoNuevo, userDetails.getId());
      return ResponseEntity.ok(dto);
    } catch (UnauthorizedException e) {
      return ResponseEntity.status(403).body(Map.of("error", "No está autorizado para modificar este hecho."));
    } catch (SolicitudFueraDePlazoException e) {
      return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
    }
  }

  @PatchMapping("/{idHecho}/estado")
  public ResponseEntity<?> resolverSolicitud (@PathVariable Long idHecho, @RequestBody ResolucionDTO resolucion, @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      SolicitudResueltaDTO dto = solicitudesService.procesarSoliPendiente(idHecho, resolucion, userDetails.getId());
      return ResponseEntity.ok(dto);
    } catch (EntityNotFoundException | NoHaySolicitudPendienteException e) {
      return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    } catch (SolicitudYaProcesadaException e) {
      return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
    }
  }
}
