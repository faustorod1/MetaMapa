package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;
import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;
import lombok.Data;

@Data
public class ColeccionConHechosOutputDTO {
  private String identificador;
  private String titulo;
  private String descripcion;
  private List<HechoOutputDTO> hechos;
  private List<FuenteDTO> fuentes;
}
