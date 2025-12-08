package ar.utn.ba.ddsi.models.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionDTO implements Serializable {
  private String identificador;
  private String titulo;
  private String descripcion;
  private CriterioDTO criterioDePertenencia;
  private List<FuenteDTO> fuentes;
  private String algoritmoDeConsenso;


}
