package ar.utn.ba.ddsi.models.dtos.inputs;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Etiqueta;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.Departamento;
import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private String idExterno;
    private Long contribuyente;
    private List<SolicitudDeEliminacionInputDTO> solicitudesDeEliminacion;
    private Set<String> etiquetas;
    private Departamento departamento;
    
    public Hecho toEntity() {
        Set<Etiqueta> hashDeEtiquetas = this.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toSet());

        Hecho hecho = Hecho.builder()
            .id(this.getId())
            .titulo(this.getTitulo())
            .categoria(this.getCategoria())
            .descripcion(this.getDescripcion())
            .origen(this.getOrigen())
            .lugarAcontecimiento(this.getLugarAcontecimiento())
            .fechaHecho(this.getFechaHecho())
            .fechaDeCarga(this.getFechaDeCarga())
            .etiquetas(hashDeEtiquetas)
            .contribuyente(this.getContribuyente())
            .build();
        if (this.getDepartamento() != null) {
            hecho.setDepartamento(this.getDepartamento().getNombre());
            if (this.getDepartamento().getProvincia() != null) {
                hecho.setProvincia(this.getDepartamento().getProvincia().getNombre());
            }
        }
        return hecho;
    } 
}
