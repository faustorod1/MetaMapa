package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dto.input.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ar.utn.ba.ddsi.models.entities.OrigenHecho.CONTRIBUYENTE;

@Service
public class HechosService implements IHechosService {
  private final IHechosRepository hechosRepository;

  public HechosService(IHechosRepository hechosRepository) {
    this.hechosRepository = hechosRepository;
  }


  // --- Métodos expuestos al controller -------------------------------------------------------------------------------


  @Override
  public List<HechoOutputDTO> getAll_DTO() {
    return hechosRepository
        .findAll()
        .stream()
        .map(this::hechoToDTO)
        .toList();
  }

  @Override
  public List<HechoOutputDTO> getAllDesde_DTO(LocalDateTime desde) {
    return hechosRepository
        .findAll()
        .stream()
        .filter(hecho -> hecho.getFechaUltimaActualizacion().isAfter(desde))
        .map(this::hechoToDTO)
        .toList();
  }

  @Override
  public HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO) {
    Hecho hecho = this.DTOToHecho(hechoInputDTO);
    hechosRepository.save(hecho);
    return this.hechoToDTO(hecho);
  }

  @Override
  public void marcarComoELiminado(Long id) {
    hechosRepository.marcarComoEliminado(id);
  }


  // --- Métodos para uso interno --------------------------------------------------------------------------------------


  @Override
  public Hecho getById(Long id){return hechosRepository.findById(id).orElse(null);}

  @Override
  public void update(Hecho h, Hecho hViejo){
    hechosRepository.update(h,hViejo);
  }


  // --- Conversiones DTO ----------------------------------------------------------------------------------------------


  public Hecho DTOToHecho (HechoInputDTO hechoInputDTO){      // Al guardarse el hecho por 1era vez: fechaDeCarga == lastUpdate
    ContribuyenteDTO contribuyenteDTO = hechoInputDTO.getContribuyente();
    return Hecho.builder()
        .titulo(hechoInputDTO.getTitulo())
        .descripcion(hechoInputDTO.getDescripcion())
        .categoria(new Categoria(hechoInputDTO.getCategoria()))
            .contenidosMultimedia(hechoInputDTO.getContenidosMultimedia().stream().map(ContenidoMultimedia::new).collect(Collectors.toList()))
        .lugarAcontecimiento(new Coordenada(hechoInputDTO.getLatitud(),hechoInputDTO.getLongitud()))
        .fechaHecho(LocalDateTime.parse(hechoInputDTO.getFechaHecho(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
        .eliminado(false)
        .contribuyente(new Contribuyente(contribuyenteDTO.getId(), contribuyenteDTO.getNombre(), contribuyenteDTO.getApellido(),
                LocalDate.parse(contribuyenteDTO.getFechaDeNacimiento(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"))))
        .etiquetas(hechoInputDTO.getEtiquetas())
                .build();
  }

  public HechoOutputDTO hechoToDTO (Hecho hecho){
    return HechoOutputDTO.builder()
        .titulo(hecho.getTitulo())
        .descripcion(hecho.getDescripcion())
        .categoria(hecho.getCategoria())
        .contenidosMultimedia(hecho.getContenidosMultimedia().stream().map(ContenidoMultimedia::getPath).toList())
        .lugarAcontecimiento(hecho.getLugarAcontecimiento())
        .fechaHecho(hecho.getFechaHecho())
        .fechaDeCarga(hecho.getFechaDeCarga())
        .fechaUltimaActualizacion(hecho.getFechaUltimaActualizacion())
        .origen(CONTRIBUYENTE)
        .eliminado(hecho.isEliminado())
        .contribuyente(this.contribuyenteToDTO(hecho.getContribuyente()))
        .etiquetas(hecho.getEtiquetas())
        .id(String.format("dinamica:%s", hecho.getId()))
        .build();
  }

  private ContribuyenteDTO contribuyenteToDTO (Contribuyente contribuyente){
    ContribuyenteDTO c = new ContribuyenteDTO();
    c.setId(contribuyente.getId());
    c.setNombre(contribuyente.getNombre());
    c.setApellido(contribuyente.getApellido());
    c.setFechaDeNacimiento(contribuyente.getFechaNacimiento().toString());
    return c;
  }
}