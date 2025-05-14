package ar.utn.ba.ddsi.MetaMapa.controllers;

import ar.utn.ba.ddsi.MetaMapa.models.entities.Hecho;
import ar.utn.ba.ddsi.MetaMapa.services.iFuenteDinamicaService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/fuentes")
public class fuenteDinamicaController {
  private iFuenteDinamicaService fuenteDinamicaService;

  public fuenteDinamicaController(iFuenteDinamicaService fuenteDinamicaService) {
    this.fuenteDinamicaService = fuenteDinamicaService;
  }

  public void crearHecho(@RequestBody Hecho hecho) {

  }
}