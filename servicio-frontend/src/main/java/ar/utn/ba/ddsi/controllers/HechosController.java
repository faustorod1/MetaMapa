package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.exceptions.HechoMalCargadoException;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.DatosLogin;
import ar.utn.ba.ddsi.services.IDinamicaService;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/hechos")
public class HechosController {

    @Autowired
    private IDinamicaService dinamicaService;

    @GetMapping("/formulario-de-carga")
    public String formularioCargarHecho(Model model) {
        model.addAttribute("hechoOutputDTO", new HechoOutputDTO());
        return "main-page/cargarHecho";      // Ahora ese endpoint tiene el HechoOutputDTO para cargarle los campos
    }


    @PostMapping("/cargar")
    public String cargarHecho(@ModelAttribute("hechoOutputDTO") HechoOutputDTO hechoOutputDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        try {
            dinamicaService.cargarHecho(hechoOutputDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Hecho creado con éxito");
            return "redirect:/main/mapa";
        } catch (HechoMalCargadoException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("hechoOutputDTO", hechoOutputDTO);
            return "/main-page/cargarHecho";

        }

    }

}
