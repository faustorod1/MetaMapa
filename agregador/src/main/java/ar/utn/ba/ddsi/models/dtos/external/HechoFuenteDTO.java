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
    private Long contribuyenteId;
    private HashSet<String> etiquetas;

    public Hecho toEntity() {
        Hecho hecho = Hecho.builder()
                .idExterno(new IdExterno())
                .titulo(this.getTitulo())
                .descripcion(this.getDescripcion())
                .categoria(new Categoria(this.getCategoria()))
                .origen(this.getOrigen())
                .lugarAcontecimiento(this.getLugarAcontecimiento())
                .fechaHecho(this.getFechaHecho())
                .fechaDeCarga(this.getFechaDeCarga())
                .contribuyenteId(this.contribuyenteId).build();
                hecho.getIdExterno().setIdExterno(this.getId());
        if (this.getContenidosMultimedia() != null) {
            hecho.setContenidosMultimedia(this.getContenidosMultimedia().stream().map(ContenidoMultimedia::new).toList());
        }
        if (this.getEtiquetas() != null) {
            this.etiquetas.forEach(etiqueta -> {
                hecho.etiquetar(new Etiqueta(etiqueta));
            });
        }
        return hecho;
    }
}