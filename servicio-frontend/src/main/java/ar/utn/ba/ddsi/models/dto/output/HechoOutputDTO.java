package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.dto.input.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoOutputDTO {
    private String titulo;
    private CategoriaDTO categoria;
    private CoordenadaDTO coordenadaDTO;
    private String descripcion;
    private LocalDate fecha;
    private Set<EtiquetaDTO> etiquetas;
    private List<ContenidoMultimediaDTO> contenidosMultimedia;

}
