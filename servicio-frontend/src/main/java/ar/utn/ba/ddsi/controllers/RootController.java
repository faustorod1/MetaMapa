package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.exceptions.BadCodeException;
import ar.utn.ba.ddsi.exceptions.UsuarioExistenteException;
import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;
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
  public String logIn(Model model) {
    model.addAttribute("datosLogin", new DatosLogin());
    return "landing-page/login";
  }
/*
  @PostMapping("/login")
  public String iniciarSesion(@ModelAttribute("datosLogin") DatosLogin datosLogin, RedirectAttributes redirectAttributes) {
    log.info("Login recibido: {}", datosLogin);
    try {
      rootService.login(datosLogin.getUsername(), datosLogin.getPassword());
      return "redirect:/main";
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", "Correo electrónico y/o contraseña incorrecta");
      return "redirect:/login";
    }
  }
*/

  @GetMapping("/register")
  public String register(Model model) {
    // Si el modelo NO contiene 'datosRegister' (ej. no viene de un error), lo inicializamos
    if (!model.containsAttribute("datosRegister")) {
      model.addAttribute("datosRegister", new DatosRegister());
    }
    return "landing-page/register";
  }

  @PostMapping("/register")
  public String registrarse(
      @ModelAttribute("datosRegister") DatosRegister datosRegister,
      Model model,
      RedirectAttributes redirectAttributes) {

    try {
      // Llama al servicio de registro
      rootService.registrar(
          datosRegister.getNombre(),
          datosRegister.getApellido(),
          datosRegister.getEmail(),
          datosRegister.getContrasenia(),
          datosRegister.getContraseniaRepetida(),
          datosRegister.getCodigoAdministrador()
      );

      // Si tiene éxito, redirige a login con mensaje de éxito
      redirectAttributes.addFlashAttribute("exito", "¡Registro exitoso! Ahora puedes iniciar sesión.");
      return "redirect:/login";

    } catch (BadCodeException ex) {

      model.addAttribute("error", ex.getMessage());
      model.addAttribute("datosRegister", datosRegister);

      return "landing-page/register";

    } catch (UsuarioExistenteException ex) {

      model.addAttribute("error", ex.getMessage());
      model.addAttribute("datosRegister", datosRegister);

      return "landing-page/register";

    } catch (Exception ex) {
      log.error("Error desconocido durante el registro", ex);

      // Error genérico (ej. Contraseñas no coinciden, error de DB, etc.)
      model.addAttribute("error", "Hubo una falla al registrarse. Verifique los datos e intente nuevamente.");
      model.addAttribute("datosRegister", datosRegister);
      return "landing-page/register";
    }
  }
}