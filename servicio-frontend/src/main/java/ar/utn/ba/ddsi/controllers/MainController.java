package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
  public String mapa(Model model, RedirectAttributes redirectAttributes) {
    try{
      return "main-page/mapa";
    } catch (Exception e) {
      return "redirect:/404";
    }
  }

  @GetMapping("buscador")
  public String buscador() {
    return "main-page/buscador";
  }







}


