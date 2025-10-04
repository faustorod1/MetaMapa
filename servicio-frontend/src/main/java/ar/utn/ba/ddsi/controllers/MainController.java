package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
    List<HechoDTO> hechos = hechosService.buscarTodos();
    return "main-page/mapa";
  }

  @GetMapping("buscador")
  public String buscador() {
    return "main-page/buscador";
  }


  @GetMapping("/formulario-cargar-hecho")
  public String formularioCargarHecho(Model model) {
    model.addAttribute("hecho", new HechoOutputDTO());
    return "main-page/cargar-hecho";      // Ahora ese endpoint tiene el HechoOutputDTO para cargarle los campos
  }

  /*
  @PostMapping("/cargar-hecho")
  public String cargarHecho(@ModelAttribute("hechoOutputDTO") HechoOutputDTO hechoOutputDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

    try{
      HechoDTO hechoDTO = hechosService.cargarHecho(hechoOutputDTO);

    }


  }
*/


}