package ar.utn.ba.ddsi.controllers;


import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
public class ColeccionesController {

    @Autowired
    private IAgregadorService agregadorService;

    @GetMapping("/formulario-de-carga")
    public String formularioCrearColeccion(Model model) {
        try {
            List<FuenteDTO> fuentes = agregadorService.buscarFuentes();
            model.addAttribute("coleccionOutputDTO", new ColeccionOutputDTO());
            model.addAttribute("fuentes", fuentes);
            return "main-page/crearColeccion";
        } catch (Exception e) {
            model.addAttribute("errorFuentes", "Error al intentar cargar la página");
            // Quizás podría haber un redirect a una página de status 400
            return "redirect:/403";
        }

    }
}