package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {

  //Métodos expuestos al controller
  Page<HechoOutputDTO> getAll_DTO(Pageable pageable);
  Page<HechoOutputDTO> getAllDesde_DTO(LocalDateTime desde, Pageable pageable);
  HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO, List<MultipartFile> imagenes);
  void marcarComoELiminado(Long id);
  HechoOutputDTO buscarHechoNoEliminado(Long id);
  List<Long> buscarIdsHechos();
  //Métodos para uso interno
  Hecho getById(Long id);
  HechoOutputDTO hechoToDTO (Hecho hecho);

  void guardarCambios(Hecho h);

  Hecho DTOToHecho (HechoInputDTO hechoInputDTO);
}
