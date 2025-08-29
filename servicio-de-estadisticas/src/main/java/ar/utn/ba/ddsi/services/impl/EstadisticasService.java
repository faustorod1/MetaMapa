package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;



import java.time.LocalTime;
import java.util.stream.Collectors;

public class EstadisticasService {

    private WebClient servicioAgregador;

    public EstadisticasService(@Value("${agregador.api.base-url}") String apiAgregadorURL) {
        this.servicioAgregador = WebClient.builder().baseUrl(apiAgregadorURL).build();
    }


    public String categoriaConMasHechos() {
        List<Hecho> hechos = getHechosFromAgregador();

        return hechos.stream()
                .collect(Collectors.groupingBy(h -> h.getCategoria().getNombre(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }



    public LocalTime horarioConMasHechosDeCiertaCategoria(Categoria categoria){
        List<Hecho> hechos = getHechosFromAgregador();
        List<Hecho> hechosDeCategoriaParticular = hechos.stream().filter(hecho -> hecho.getCategoria().getNombre().equals(categoria.getNombre())).toList();

        return hechosDeCategoriaParticular.stream()
                .collect(Collectors.groupingBy(h -> h.getFechaHecho().getHour(), Collectors.counting())   // OJO: fechaHecho no tiene Time, solo Date
                .entrySet()
                .stream()       // Ahora tenemos un par de entradas: <Hora_del_dia, cantidad_de_hechos_ocurridos_en_esa_hora>
                .max(Map.Entry.comparingByValue())      // Obtiene la hora (entry) con mayor cantidad de hechos (value) asociados
                .map(entry -> LocalTime.of(entry.getKey(), 0)));  // Convertimos esa hora (int) en un LocalTime hh:00
    }



    //--------------------------------------------------------- privados ---------------------------------------------------------//

    private List<Hecho> getHechosFromAgregador() {
        return servicioAgregador.get()
                .uri("/api/hechos")
                .retrieve()
                .bodyToMono()
                .map(HechoInputDTO.class)
                .
    }


    private Hecho HechoDtoToHecho(HechoInputDTO h){
        Set<Etiqueta> hashDeEtiquteas = h.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toSet());
        List<SolicitudDeEliminacion> solicitudesDeEliminacion = h.getSolicitudesDeEliminacion().stream().map( solicitud -> new SolicitudDeEliminacion().builder()
                .solicitante()
                .build());

                /*
                * private Long id;
                * private String descripcion;
                * private Long hechoId;
                * private LocalDateTime fechaDeCarga;
                * private LocalDateTime fechaDeResolucion;
                * private EstadoSolicitud estado;
                * private ContribuyenteDTO solicitante;
                * private Administrador administradorQueResolvio;
                * */

        return Hecho.builder()
                .id(h.getId())
                .titulo(h.getTitulo())
                .descripcion(h.getDescripcion())
                .origen(h.getOrigen())
                .lugarAcontecimiento(h.getLugarAcontecimiento())
                .fechaHecho(h.getFechaHecho())
                .fechaDeCarga(h.getFechaDeCarga())
                .etiquetas(hashDeEtiquteas)
                .contenidoMultimedia(h.getContenidoMultimedia())
                .contribuyente(h.getContribuyente())

                .build();
    }


}

