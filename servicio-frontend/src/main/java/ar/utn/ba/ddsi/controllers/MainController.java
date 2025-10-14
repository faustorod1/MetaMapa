package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

  @Autowired
  private IAgregadorService agregadorService;

  @GetMapping
  public String main() {
    return "main-page/main";
  }

  @GetMapping("mapa")
  public String mapa(Model model) {
    try {
      List<HechoDTO> hechos = agregadorService.buscarHechos();
      model.addAttribute("hechos", hechos);
      System.out.println("HECHOS PEDIDOS");
      return "main-page/mapa";
    } catch (Exception e) {
      model.addAttribute("error", "La carga de hechos ha fallado");
      return "main-page/mapa";

    }
  }
}

    /*
  @GetMapping("buscador")
  public String buscador() {
    return "main-page/buscador";
  }
  */






