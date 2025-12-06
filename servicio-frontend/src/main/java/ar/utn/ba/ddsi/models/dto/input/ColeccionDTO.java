package ar.utn.ba.ddsi.models.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ColeccionDTO implements Serializable {
  private String identificador;
  private String titulo;
  private String descripcion;
  private CriterioDTO criterioDePertenencia;
  private List<FuenteDTO> fuentes;
}
