package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.services.iFuenteDinamicaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/fuenteDinamica")
public class fuenteDinamicaController {
  private iFuenteDinamicaService fuenteDinamicaService;

  public fuenteDinamicaController(iFuenteDinamicaService fuenteDinamicaService) {
    this.fuenteDinamicaService = fuenteDinamicaService;
  }

  //TODO: revisar si es correcto este @PostMapping
  @PostMapping
  public void crearHecho(@RequestBody Hecho hecho) { //Todo tal vez hacer dtoHecho para cuando llegue del servicio agregador (pero el agregador se tiene q encargar)
    fuenteDinamicaService.crearHecho(hecho);
  }

  @GetMapping
  public List<Hecho> listarHechos (@RequestParam(required = false) Boolean revisado) {
    return fuenteDinamicaService.obtenerHechos(revisado);
  }

  //hacer una funcion que devuelva los hechos de una persona en particular

  public void modificarHecho (@RequestBody Hecho aModificar,Hecho nuevo){//TODO ver si podemos recibir unicamente el id del hecho viejo (aModificar)
    fuenteDinamicaService.modificarHecho(aModificar,nuevo);
  }



}