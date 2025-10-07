package ar.utn.ba.ddsi.controllers;


import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
public class ColeccionesController {

    @Autowired
    private IAgregadorService agregadorService;

    @GetMapping("/formulario-de-carga")
    public String formularioCrearColeccion(Model model){
        List<FuenteDTO> fuentes = agregadorService.buscarFuentes();

        model.addAttribute("coleccionOutputDTO", new ColeccionOutputDTO());
        model.addAttribute("fuentes", fuentes);
        return "main/crearColeccion";
    }

    @PostMapping("/crear")
    public String crearColeccion(@ModelAttribute("coleccionOutputDTO") ColeccionOutputDTO coleccionOutputDTO, Model model){


    }

}
