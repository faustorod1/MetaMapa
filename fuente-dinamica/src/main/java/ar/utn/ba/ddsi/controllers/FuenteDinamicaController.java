package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.services.iFuenteDinamicaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/hechos")
public class FuenteDinamicaController {
  private iFuenteDinamicaService fuenteDinamicaService;

  public FuenteDinamicaController(iFuenteDinamicaService fuenteDinamicaService) {
    this.fuenteDinamicaService = fuenteDinamicaService;
  }

  @GetMapping
  public List<HechoOutputDTO> listarHechos (){
    return fuenteDinamicaService.getAll();
  }

  @GetMapping(params = "desde") // localhost:8082/api/hechos?desde=algo
  public List<HechoOutputDTO> buscarTodosCargadosDesde(
          @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde) {
    return this.fuenteDinamicaService.getAllDesde(desde);
  }

  @PostMapping
  public HechoOutputDTO crearHecho(@RequestBody HechoInputDTO hecho)   {
    return fuenteDinamicaService.crearHecho(hecho);
  }

  @GetMapping("/pendientes={pendiente}")
  public List<HechoOutputDTO> listarHechosPendientes(@RequestParam(required = false) Boolean pendiente) {
    return fuenteDinamicaService.obtenerHechosPendientes(pendiente);
  }

  @GetMapping("/contribuyente={id}")
  public List<HechoOutputDTO> listarHechosDe (Contribuyente contribuyente) {
    return fuenteDinamicaService.obtenerHechosDe(contribuyente);
  }

  @PutMapping("/{idHecho}")
  public HechoOutputDTO modificarHecho (@PathVariable Long idHecho, @RequestBody HechoInputDTO hechoNuevo)   {
    return fuenteDinamicaService.modificarHecho(idHecho, hechoNuevo);
  }

  @PutMapping("/{idHecho}/estado")
  public HechoOutputDTO resolverSolicitud (@PathVariable Long idHecho, @RequestBody EstadoSolicitud estadoNuevo) {
    return fuenteDinamicaService.procesarPendiente(idHecho,estadoNuevo);
  }

}