package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.services.ISolicitudesService;
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

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {
  private ISolicitudesService solicitudesService;

  @Autowired
  public SolicitudesController(ISolicitudesService solicitudesService) {
    this.solicitudesService = solicitudesService;
  }

  @PostMapping      // Devuelve status code HTTP
  public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudDeEliminacionInputDTO solicitud) {
    return solicitudesService.crearSolicitud(solicitud);
  }

  @PatchMapping("/{id}/estado")
  public void modificarEstadoSolicitud(@PathVariable Long id, @RequestBody ResolucionSolicitudDeEliminacionDTO resolucion) {
    solicitudesService.modificarEstadoSolicitud(id, resolucion);
  }

  @GetMapping
  public List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudes(){
    return solicitudesService.obtenerSolicitudes();
  }


}
