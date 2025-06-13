package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SolicitudesController {
  private ISolicitudesService solicitudesService;

  public SolicitudesController(ISolicitudesService solicitudesService) {
    this.solicitudesService = solicitudesService;
  }


  @PutMapping("/{idHecho}")
  public HechoOutputDTO modificarHecho (@PathVariable Long idHecho, @RequestBody HechoInputDTO hechoNuevo)   {
    return solicitudesService.crearSolModificacion(idHecho, hechoNuevo);
  }

  @PatchMapping("/{idHecho}/estado")
  public HechoOutputDTO resolverSolicitud (@PathVariable Long idHecho, @RequestBody ResolucionDTO resolucion) {
    return solicitudesService.procesarSoliPendiente(idHecho,resolucion);
  }
}
