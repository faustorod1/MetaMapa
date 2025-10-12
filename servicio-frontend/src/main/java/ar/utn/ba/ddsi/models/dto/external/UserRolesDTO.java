package ar.utn.ba.ddsi.models.dto.external;

import ar.utn.ba.ddsi.models.dto.Rol;
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
