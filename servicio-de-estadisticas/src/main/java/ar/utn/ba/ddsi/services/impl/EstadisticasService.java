package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.inputs.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.inputs.HechoInputDTO;
import ar.utn.ba.ddsi.models.dtos.inputs.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EstadisticasService implements IEstadisticasService {
    private WebClient servicioAgregador;

    public EstadisticasService(@Value("${agregador.api.base-url}") String apiAgregadorURL) {
        this.servicioAgregador = WebClient.builder().baseUrl(apiAgregadorURL).build();
    }

    // ----------------------------------------------------------------------------

    public String provinciaConMasHechosDeColeccion(String coleccion_id) {
        List<Hecho> hechos = getHechosDeColeccionFromAgregador(coleccion_id);
        return provinciaConMasHechos(hechos);
    }

    public String provinciaConMasHechosDeCategoria(String categoria) {
        List<Hecho> hechos = getHechosFromAgregador();
        List<Hecho> hechosDeCategoria = hechos.stream()
            .filter(h -> h.getCategoria().equals(categoria))
            .collect(Collectors.toList());
        return provinciaConMasHechos(hechosDeCategoria);
    }

    public String categoriaConMasHechos() {
        List<Hecho> hechos = getHechosFromAgregador();
        return masHechosSegunParametro(hechos, hecho -> hecho.getCategoria().getNombre());
    }


    public LocalTime horarioConMasHechosDeCiertaCategoria(Categoria categoria) {
        List<Hecho> hechos = getHechosFromAgregador();
        List<Hecho> hechosDeCategoriaParticular = hechos.stream().filter(hecho -> hecho.getCategoria().getNombre().equals(categoria.getNombre())).toList();

        int hora = masHechosSegunParametro(hechosDeCategoriaParticular, hecho -> hecho.getFechaHecho().getHour());
        return LocalTime.of(hora, 0);
    }

    public Long solicitudesSpam() {
        List<SolicitudDeEliminacion> solicitudes = getSolicitudesFromAgregador();
        return solicitudes
            .stream().
            filter(solicitud -> solicitud.getEstado().equals(EstadoSolicitud.RECHAZADA_POR_SPAM))
            .count();
    }


    //--------------------------------------------------------- privados ---------------------------------------------------------//


    private <TipoRetorno> TipoRetorno masHechosSegunParametro(List<Hecho> hechos, Function<Hecho, TipoRetorno> criterio) {
        return hechos.stream()
            .collect(Collectors.groupingBy(criterio, Collectors.counting()))
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    private String provinciaConMasHechos(List<Hecho> hechos) {
        return masHechosSegunParametro(hechos, Hecho::getProvincia);
    }

    private List<Hecho> getHechosFromAgregador() {
        return servicioAgregador.get()
            .uri("/api/hechos")
            .retrieve()
            .bodyToFlux(HechoInputDTO.class)
            .map(this::HechoDtoToHecho)
            .collectList()
            .block();
    }

    private List<Hecho> getHechosDeColeccionFromAgregador(String coleccion_id) {
        return servicioAgregador.get()
            .uri("/api/colecciones/%s/hechos".formatted(coleccion_id))
            .retrieve()
            .bodyToFlux(HechoInputDTO.class)
            .map(this::HechoDtoToHecho)
            .collectList()
            .block();
    }

    private List<SolicitudDeEliminacion> getSolicitudesFromAgregador() {
        return servicioAgregador.get()
            .uri("/api/solicitudes")
            .retrieve()
            .bodyToFlux(SolicitudDeEliminacionInputDTO.class)
            .map(this::solicitudDTOtoSolicitud)
            .collectList()
            .block();
    }


    private Hecho HechoDtoToHecho(HechoInputDTO h) {
        Set<Etiqueta> hashDeEtiquteas = h.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toSet());
        List<SolicitudDeEliminacion> solicitudesDeEliminacion = h.getSolicitudesDeEliminacion().stream()
            .map(this::solicitudDTOtoSolicitud)
            .toList();

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

    private SolicitudDeEliminacion solicitudDTOtoSolicitud(SolicitudDeEliminacionInputDTO solicitud) {
        return SolicitudDeEliminacion.builder()
            .id(solicitud.getId())
            .solicitante(solicitud.getSolicitante())
            .hechoId(solicitud.getHechoId())
            .descripcion(solicitud.getDescripcion())
            .estado(solicitud.getEstado())
            .fechaDeResolucion(solicitud.getFechaDeResolucion())
            .fechaDeCarga(solicitud.getFechaDeCarga())
            .administradorQueResolvio(solicitud.getAdministradorQueResolvio())
            .build();
    }

}




