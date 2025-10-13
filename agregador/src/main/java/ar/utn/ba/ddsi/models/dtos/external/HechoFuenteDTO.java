package ar.utn.ba.ddsi.models.dtos.external;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class HechoFuenteDTO {
    private Long id;
    private String tipoDeFuente;
    private Long subfuenteId;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<String> contenidosMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private boolean eliminado;
    private ContribuyenteDTO contribuyente;
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private HashSet<String> etiquetas;

    public Hecho toEntity() {
        Contribuyente contribuyente = null;
        if (this.getContribuyente() != null) {
            contribuyente = this.getContribuyente().toEntity();
        }


        Hecho hecho = Hecho.builder()
                .idExterno(new IdExterno())
                .titulo(this.getTitulo())
                .descripcion(this.getDescripcion())
                .categoria(new Categoria(this.getCategoria()))
                .etiquetas(this.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toSet()))
                .origen(this.getOrigen())
                .lugarAcontecimiento(this.getLugarAcontecimiento())
                .fechaHecho(this.getFechaHecho())
                .fechaDeCarga(this.getFechaDeCarga())
                .contribuyente(contribuyente).build();
        if(this.getSolicitudesDeEliminacion() != null) {
                hecho.setSolicitudesDeEliminacion(this.getSolicitudesDeEliminacion()); // Cambiar
        }
                hecho.getIdExterno().setIdExterno(this.getId());
        if (this.getContenidosMultimedia() != null) {
            hecho.setContenidosMultimedia(this.getContenidosMultimedia().stream().map(ContenidoMultimedia::new).toList());
        }
        return hecho;
    }
}