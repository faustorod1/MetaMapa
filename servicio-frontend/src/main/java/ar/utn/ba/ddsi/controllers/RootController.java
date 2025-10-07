package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.entities.DatosLogin;
import ar.utn.ba.ddsi.models.entities.DatosRegister;
import ar.utn.ba.ddsi.services.IRootService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/")
public class RootController {
  public IRootService rootService;

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
  public String iniciarSesion(@ModelAttribute("datosLogin") DatosLogin datosLogin, Model model){

    return "landing-page/login";
  }

  @GetMapping("/register")
  public String register() {
    return "landing-page/register";
  }

  @PostMapping("/register")
  public String registrarse(@ModelAttribute("datosRegister") DatosRegister datosRegister, Model model) {
      return "landing-page/register";
  }
}