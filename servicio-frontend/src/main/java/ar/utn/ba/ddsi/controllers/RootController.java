package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.entities.DatosLogin;
import ar.utn.ba.ddsi.models.entities.DatosRegister;
import ar.utn.ba.ddsi.services.IRootService;
import ar.utn.ba.ddsi.services.impl.RootService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/")
public class RootController {
  public IRootService rootService;

  @Autowired
  public RootController(IRootService rootService) {
    this.rootService = rootService;
  }

  @GetMapping()
  public String home() {
    return "landing-page/landing";
  }

  @GetMapping("/informacion-legal-y-privacidad")
  public String informacionLegalYPrivacidad() {
    return "landing-page/informacionLegalYPrivacidad";
  }

  @GetMapping("/login")
  public String logIn(@ModelAttribute("datosLogin") DatosLogin datosLogin) {
    return "landing-page/login";
  }

  @PostMapping("/login")
  public String iniciarSesion(@ModelAttribute("datosLogin") DatosLogin datosLogin, Model model) {
    rootService.login(datosLogin.getUsername(), datosLogin.getPassword());
    return "landing-page/login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("datosRegister", new DatosRegister());
    return "landing-page/register";
  }

  @PostMapping("/register")
  public String registrarse(@ModelAttribute("datosRegister") DatosRegister datosRegister, Model model, RedirectAttributes redirectAttributes) {

    try {
      rootService.registrar(datosRegister.getNombre(), datosRegister.getApellido(), datosRegister.getEmail(), datosRegister.getContrasenia(), datosRegister.getContraseniaRepetida());
      redirectAttributes.addFlashAttribute("exito", "Se ha registrado correctamente");
      return "redirect:/login";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", "Hubo una falla al registrarse");
      return "redirect:/login";

    }
  }
}