package ar.utn.ba.ddsi.models.dto;

import ar.utn.ba.ddsi.models.entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesDTO {
    private String email;
    private Rol role;
}
