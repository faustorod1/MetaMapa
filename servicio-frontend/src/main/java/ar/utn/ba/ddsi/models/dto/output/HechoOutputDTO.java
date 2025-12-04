package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.dto.input.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoOutputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHecho;
    private Long contribuyenteId;
    private List<EtiquetaOutputDTO> etiquetas = new ArrayList<>();




    public static HechoOutputDTO fromDTOtoOutput(HechoDTO hecho) {
        List<EtiquetaOutputDTO> etiquetasOutput = hecho.getEtiquetas().stream()
                .map(EtiquetaOutputDTO::new)
                .collect(Collectors.toList());

        return HechoOutputDTO.builder()
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria().getNombre())
                .fechaHecho(hecho.getFechaHecho())
                .contribuyenteId(hecho.getContribuyenteId())
                .latitud(hecho.getLugarAcontecimiento().getLatitud())
                .longitud(hecho.getLugarAcontecimiento().getLongitud())
                .etiquetas(etiquetasOutput)
                .build();
    }







}
