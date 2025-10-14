package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;
import ar.utn.ba.ddsi.models.dto.external.UserRolesDTO;

public interface IRootService {
    AuthResponseDTO login(String email, String password);
    UserRolesDTO getRole(String accessToken);
    AuthResponseDTO registrar(String nombre, String apellido, String email, String password, String repetedPassword);
}
