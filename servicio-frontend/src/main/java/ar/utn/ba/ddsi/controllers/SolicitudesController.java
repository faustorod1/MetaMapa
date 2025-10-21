package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

    @Autowired
    private IAgregadorService agregadorService;

    @GetMapping("eliminacion/{id}")
    public String formularioSolicitudEliminacion(@PathVariable Long id, Model model) {
        SolicitudDeEliminacionOutputDTO solicitud = new SolicitudDeEliminacionOutputDTO();
        solicitud.setHechoId(id);
        model.addAttribute("solicitud", solicitud);
        HechoDTO hecho = agregadorService.pedirHecho(id);
        model.addAttribute("hecho", hecho);
        return "main-page/tratarSolicitudEliminacion";
    }

    @PostMapping("solicitarEliminacion")
    public String solicitarEliminacion(@ModelAttribute("solicitud") SolicitudDeEliminacionOutputDTO solicitud, Model model, RedirectAttributes redirectAttributes) {
      try {
          //agregadorService.solicitarEliminacion(solicitud);
          redirectAttributes.addFlashAttribute("exito", "La solicitud ha sido creada");
          return "redirect:/api/solicitudes/eliminacion/{id}";
      } catch (Exception ex){
          redirectAttributes.addFlashAttribute("error", "Error en la solicitud");
          return "redirect:/solicitudes/eliminacion/{id}";
          }
    }



}
