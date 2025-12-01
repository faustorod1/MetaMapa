package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;
import ar.utn.ba.ddsi.models.dto.external.UserRolesDTO;
import ar.utn.ba.ddsi.models.dto.input.ColeccionDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;

import java.util.List;

public interface IRootService {
    AuthResponseDTO login(String email, String password);
    UserRolesDTO getRole(String accessToken);
    AuthResponseDTO registrar(String nombre, String apellido, String email, String password, String repetedPassword,String code);
    List<HechoDTO> getHechosDestacados(Integer cantidadHechos);
  List<ColeccionDTO> getColeccionesDestacadas(Integer cantidadColecciones);
}
