package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import ar.utn.ba.ddsi.services.IDinamicaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

  @Autowired
  private IAgregadorService agregadorService;
  @Autowired
  private IDinamicaService dinamicaService;


  // ENDPOINTS: Contribuyente
  @GetMapping("/eliminacion/{id}")
  public String formularioSolicitudEliminacion(@PathVariable("id") Long id, Model model) {
    SolicitudDeEliminacionOutputDTO solicitud = new SolicitudDeEliminacionOutputDTO();
    solicitud.setHechoId(id);
    model.addAttribute("solicitud", solicitud);
    HechoDTO hecho = agregadorService.pedirHecho(id);
    model.addAttribute("hecho", hecho);
    return "crearSolicitudDeEliminacion";
  }

  @PostMapping("/solicitarEliminacion")
  public String solicitarEliminacion(@ModelAttribute("solicitud") SolicitudDeEliminacionOutputDTO solicitud, RedirectAttributes redirectAttributes) {
    Long id = solicitud.getHechoId();

    log.info("Solicitud recibida: " + solicitud);
    try {
      agregadorService.solicitarEliminacion(solicitud);
      redirectAttributes.addFlashAttribute("exito", "La solicitud ha sido creada");
      return "redirect:/api/solicitudes/eliminacion/" + id;
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", "Error en la solicitud");
      return "redirect:/solicitudes/eliminacion/" + id;
    }
  }

  @GetMapping("/modificacion/{id_hecho}")
  public String formularioSolicitarModificacion(@PathVariable("id_hecho") Long id_hecho, Model model, RedirectAttributes redirectAttributes){
     List<HechoDTO> hechosDelContribuyente = agregadorService.pedirHechosDeContribuyente(id_hecho);
    try{
        HechoDTO hecho = hechosDelContribuyente.stream().filter(h -> h.getId().equals(id_hecho)).findAny().get();
        model.addAttribute("hecho", hecho);
        return "main-page/crearSolicitudDeModificacion";
    }catch (Exception ex){
      return "redirect:/main";    // Acá podríamos agregar un :403 calculo
    }
  }

  @PostMapping("/solicitarModificacion")
  public String solicitarModificacion(@ModelAttribute("hecho") HechoOutputDTO hecho, @RequestParam Long id_hecho, RedirectAttributes redirectAttributes) {
      log.info("DTO recibido: {}", hecho);
      try{
        dinamicaService.modificarHecho(id_hecho, hecho);
        redirectAttributes.addFlashAttribute("exito", "La solicitud ha sido creada");
        return "redirect:/api/solicitudes/modificacion/" + id_hecho;
      }catch (Exception ex){
        redirectAttributes.addFlashAttribute("error", "Error al crear la solicitud");
        return "redirect:/solicitudes/modificacion/" + id_hecho;
      }

  }


    // ENDPOINTS: administrador
    /*
    @GetMapping("/eliminacionPendiente/{id}")
    public String formularioTratarSolicitudDeEliminacion(@PathVariable("id") Long id, Model model) {

    }
    */


  }

