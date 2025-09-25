package ar.utn.ba.ddsi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {

  @GetMapping()
  public String home() {
    return "index";
  }

  @GetMapping("/informacion-legal-y-privacidad")
  public String informacionLegalYPrivacidad() {
    return "informacionLegalYPrivacidad";
  }

  @GetMapping("/login")
  public String logIn() {
    return "redirect:login";
  }
  @GetMapping("/register")
  public String register() {
    return "register";
  }

}
