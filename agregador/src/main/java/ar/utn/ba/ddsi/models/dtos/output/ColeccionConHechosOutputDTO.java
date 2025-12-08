package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ColeccionConHechosOutputDTO {
  private String identificador;
  private String titulo;
  private String descripcion;
  private Page<HechoOutputDTO> hechos;
  private List<FuenteDTO> fuentes;

  public static ColeccionConHechosOutputDTO fromEntity(Coleccion coleccion, Page<HechoOutputDTO> hechos) {
    ColeccionConHechosOutputDTO dto = new ColeccionConHechosOutputDTO();
    dto.setIdentificador(coleccion.getIdentificador());
    dto.setTitulo(coleccion.getTitulo());
    dto.setDescripcion(coleccion.getDescripcion());
    dto.setFuentes(coleccion.getFuentes().stream().map(FuenteDTO::fromEntity).toList());
    dto.hechos = hechos;
    return dto;
  }
}
