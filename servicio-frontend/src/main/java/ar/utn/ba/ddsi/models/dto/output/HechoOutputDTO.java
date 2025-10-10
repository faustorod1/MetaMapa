package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.dto.input.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoOutputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<String> contenidosMultimedia = new ArrayList<>();
    private Double latitud;
    private Double longitud;
    private LocalDate fechaHecho;
    private Long contribuyenteId = 1l; // TODO: harcodeado
    private List<EtiquetaOutputDTO> etiquetas = new ArrayList<>();
}
