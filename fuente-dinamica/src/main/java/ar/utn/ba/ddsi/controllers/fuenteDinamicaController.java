package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.services.iFuenteDinamicaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/hechos")
public class fuenteDinamicaController {
  private iFuenteDinamicaService fuenteDinamicaService;

  public fuenteDinamicaController(iFuenteDinamicaService fuenteDinamicaService) {
    this.fuenteDinamicaService = fuenteDinamicaService;
  }

  @PostMapping
  public HechoOutputDTO crearHecho(@RequestBody HechoInputDTO hecho) { //Todo tal vez hacer dtoHecho para cuando llegue del servicio agregador (pero el agregador se tiene q encargar)
    return fuenteDinamicaService.crearHecho(hecho);
  }

  @GetMapping("/Pendientes={pendiente}")
  public List<HechoOutputDTO> listarHechosPendientes(@RequestParam(required = false) Boolean pendiente) {
    return fuenteDinamicaService.obtenerHechosPendientes(pendiente);
  }

  @GetMapping
  public List<HechoOutputDTO> listarHechos () {
    return fuenteDinamicaService.obtenerTodosHechos();
  }

  @GetMapping("/contribuyente={id}")
  public List<HechoOutputDTO> listarHechosDe (Contribuyente contribuyente) {
    return fuenteDinamicaService.obtenerHechosDe(contribuyente);
  }

  @PutMapping
  public HechoOutputDTO modificarHecho (@RequestBody HechoInputDTO aModificar,HechoInputDTO nuevo){//TODO ver si podemos recibir unicamente el id del hecho viejo (aModificar)
    return fuenteDinamicaService.modificarHecho(aModificar,nuevo);
  }

}