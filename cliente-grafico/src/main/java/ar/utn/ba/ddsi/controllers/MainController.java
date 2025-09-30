package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoDto;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

  @Autowired
  private IHechosService hechosService;

  @GetMapping
  public String main() {
    return "main-page/main";
  }

  @GetMapping("mapa")
  public String mapa() {
    List<HechoDto> hechos = hechosService.buscarTodos();
    return "main-page/mapa";
  }

  @GetMapping("buscador")
  public String buscador() {
    return "main-page/buscador";
  }


}