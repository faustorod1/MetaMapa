package ar.utn.ba.ddsi.models.dtos.external;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Data
public class HechoFuenteDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private List<String> contenidosMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private boolean eliminado;
    private ContribuyenteDTO contribuyente;
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private HashSet<Etiqueta> etiquetas;

    public Hecho toEntity() {
        Contribuyente contribuyente = null;
        if (this.getContribuyente() != null) {
            contribuyente = this.getContribuyente().toEntity();
        }

        // Cambiar

        Hecho hecho = Hecho.builder()
                .idExterno(this.getId())
                .titulo(this.getTitulo())
                .descripcion(this.getDescripcion())
                .categoria(this.getCategoria())
                .origen(this.getOrigen())
                .lugarAcontecimiento(this.getLugarAcontecimiento())
                .fechaHecho(this.getFechaHecho())
                .fechaDeCarga(this.getFechaDeCarga())
                .contribuyente(contribuyente)
                .solicitudesDeEliminacion(this.getSolicitudesDeEliminacion()) // Cambiar
                .build();
        if (this.getContenidosMultimedia() != null) {
            hecho.setContenidosMultimedia(this.getContenidosMultimedia().stream().map(ContenidoMultimedia::new).toList());
        }
        return hecho;
    }
}