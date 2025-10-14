package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {

  //Métodos expuestos al controller
  List<HechoOutputDTO> getAll_DTO();
  List<HechoOutputDTO> getAllDesde_DTO(LocalDateTime desde);
  HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO, List<MultipartFile> imagenes);
  void marcarComoELiminado(Long id);

  //Métodos para uso interno
  Hecho getById(Long id);
  void update(Hecho hnuevo, Hecho hviejo);
  HechoOutputDTO hechoToDTO (Hecho hecho);
  Hecho DTOToHecho (HechoInputDTO hechoInputDTO);
}
