package ar.utn.ba.ddsi.models.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatosRegisterDTO {
  private String nombre;
  private String apellido;
  private String email;
  private String contrasenia;
  private String codigoAdministrador;
}
