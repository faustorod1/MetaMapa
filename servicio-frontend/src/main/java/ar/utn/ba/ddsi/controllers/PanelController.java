package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/panel")
public class PanelController {
    @Autowired
    private IAgregadorService agregadorService;

    @GetMapping
    public String panelDeControl(Model model) {
        Long cantidadSolicitudesEliminacionAceptadas = agregadorService.pedirSolicitudesAceptadas();
        Long cantidadSolicitudesEliminacionRechazadas = agregadorService.pedirSolicitudesRechazadas();
        HechoDTO ultimoHechoCargado = agregadorService.pedirUltimoHechoCargado();
        Integer hechosEnElSistema = agregadorService.pedirCantidadDeHechosEnElSistema();
        List<FuenteDTO> fuentes = agregadorService.buscarFuentes();

        model.addAttribute("cantidadSolicitudesEliminacionAceptadas", cantidadSolicitudesEliminacionAceptadas);
        model.addAttribute("cantidadSolicitudesEliminacionRechazadas", cantidadSolicitudesEliminacionRechazadas);
        model.addAttribute("ultimoHechoCargado", ultimoHechoCargado);
        model.addAttribute("hechosEnElSistema", hechosEnElSistema);
        model.addAttribute("fuentes", fuentes);
        return "main-page/panelDeControl";
    }


    @PostMapping("/actualizarHechos")
    public String actualizarHechos() {
        agregadorService.actualizarHechos();
        return "redirect:/panel";
    }

    @PostMapping("/consensuarColecciones")
    public String consensuarColecciones(){
        agregadorService.consensuarColecciones();
        return "redirect:/panel";
    }



}
