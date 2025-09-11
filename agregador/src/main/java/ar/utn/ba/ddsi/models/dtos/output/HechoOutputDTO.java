package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaDTO categoria;
    private List<ContenidoMultimedia> contenidosMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private String idExterno;
    private Long contribuyente;
    private List<SolicitudDeEliminacionOutputDTO> solicitudesDeEliminacion;
    private HashSet<String> etiquetas;
    private Departamento departamento;

    public static HechoOutputDTO fromEntity(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setContenidosMultimedia(hecho.getContenidosMultimedia());
        dto.setOrigen(hecho.getOrigen());
        dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setIdExterno(hecho.getIdExterno());
        dto.setDepartamento(hecho.getDepartamento());
        if (hecho.getCategoria() != null) dto.setCategoria(CategoriaDTO.fromEntity(hecho.getCategoria()));
        if (hecho.getContribuyente() != null) dto.setContribuyente(hecho.getContribuyente().getId());

        dto.setEtiquetas(
                hecho.getEtiquetas()
                        .stream()
                        .map(Etiqueta::getNombre)
                        .collect(Collectors.toCollection(HashSet::new))
        );
        return dto;
    }
}
