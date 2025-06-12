package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SolicitudesController {
  private IHechosService fuenteDinamicaService;

  public SolicitudesController(IHechosService fuenteDinamicaService) {
    this.fuenteDinamicaService = fuenteDinamicaService;
  }

}
