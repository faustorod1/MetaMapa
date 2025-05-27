package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dto.input.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.iHechosRepository;
import ar.utn.ba.ddsi.services.iFuenteDinamicaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.ACEPTADA;
import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.ACEPTADACONSUGERENCIA;
import static ar.utn.ba.ddsi.models.entities.OrigenHecho.CONTRIBUYENTE;

@Service
public class fuenteDinamicaService implements iFuenteDinamicaService {
  private final iHechosRepository hechosRepository;

  public fuenteDinamicaService(iHechosRepository hechosRepository) {
    this.hechosRepository = hechosRepository;
  }

  @Override
  public List<HechoOutputDTO> getAll(){
    return hechosRepository
            .findAll()
            .stream()
            .map(this::hechoToDTO)
            .collect(Collectors.toList());
  }

  public List<HechoOutputDTO> getAllDesde(LocalDateTime desde){       // Según fecha de última actualización
    return hechosRepository
            .findAll()
            .stream()
            .filter(hecho -> hecho.getLastUpdate().isAfter(desde))
            .map(this::hechoToDTO)
            .collect(Collectors.toList());
  }

  @Override
  public HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO) {
    Hecho hecho = this.DtoToHecho(hechoInputDTO);
    hecho.setLastUpdate(hecho.getFechaDeCarga());
    hechosRepository.save(hecho);
    return this.hechoToDTO(hecho);
  }

  @Override
  public HechoOutputDTO modificarHecho(Long id, HechoInputDTO hecho){
    Hecho h = DtoToHecho(hecho);
    Hecho hvie = this.hechosRepository.findById(id);
    if(ChronoUnit.DAYS.between(h.getFechaDeCarga(), LocalDateTime.now()) <= 7 && hvie.getContribuyente().getId().equals(h.getContribuyente().getId())){
      SolicitudDeModificacion nuevaSolicitudDeModificacion = new SolicitudDeModificacion(hvie,h);
      hvie.setSolicitudDeModificacion(nuevaSolicitudDeModificacion);
      return hechoToDTO(h);
    }
    return null;
  }

  @Override
  public List<HechoOutputDTO> obtenerHechosPendientes(Boolean pendiente) { // true si quiero q me de los pendientes
    return hechosRepository
            .findByPendiente(pendiente)
            .stream()
            .map(this::hechoToDTO)
            .collect(Collectors.toList());
  }

  @Override
  public List<HechoOutputDTO> obtenerHechosDe(Contribuyente contribuyente){
    return hechosRepository
            .findByContribuyente(contribuyente)
            .stream()
            .map(this::hechoToDTO)
            .collect(Collectors.toList());
  }



  @Override
  public HechoOutputDTO procesarPendiente(Long id, EstadoSolicitud estadoNuevo){ //Todo aca tal vez viene un id
    Hecho h = hechosRepository.findById(id);
    h.getSolicitudDeModificacion().resolver(estadoNuevo);
    if(estadoNuevo == ACEPTADA || estadoNuevo == ACEPTADACONSUGERENCIA){
      Hecho hechoNuevo = h.getSolicitudDeModificacion().getHechoNuevo();
      hechoNuevo.setLastUpdate(LocalDateTime.now());
      hechosRepository.update(h, hechoNuevo);
      return this.hechoToDTO(hechoNuevo);
    }
    return this.hechoToDTO(h);
  }

  public Hecho DtoToHecho (HechoInputDTO hechoInputDTO){      // Al guardarse el hecho por 1era vez: fechaDeCarga == lastUpdate
    ContribuyenteDTO contribuyenteDTO = hechoInputDTO.getContribuyente();
    return Hecho.builder()
        .titulo(hechoInputDTO.getTitulo())
        .descripcion(hechoInputDTO.getDescripcion())
        .categoria(new Categoria(hechoInputDTO.getCategoria()))
        //.contenidoMultimedia(new ContenidoMultimedia())
        .lugarAcontecimiento(new Coordenada(hechoInputDTO.getLatitud(),hechoInputDTO.getLongitud()))
        .fechaHecho(LocalDate.parse(hechoInputDTO.getFechaHecho(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
        .origen(CONTRIBUYENTE)
        .fechaDeCarga(LocalDateTime.now())
        .eliminado(false)
        .contribuyente(new Contribuyente(contribuyenteDTO.getId(), contribuyenteDTO.getNombre(), contribuyenteDTO.getApellido(),
                LocalDate.parse(contribuyenteDTO.getFechaDeNacimiento(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"))))
        .etiquetas(hechoInputDTO.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toCollection(HashSet::new)))
        .lastUpdate(LocalDateTime.now())
        .id(null)
        .build();
  }

  public HechoOutputDTO hechoToDTO (Hecho hecho){
    return HechoOutputDTO.builder()
        .titulo(hecho.getTitulo())
        .descripcion(hecho.getDescripcion())
        .categoria(hecho.getCategoria())
        //.contenidoMultimedia(hecho.getContenidoMultimedia())
        .lugarAcontecimiento(hecho.getLugarAcontecimiento())
        .fechaHecho(hecho.getFechaHecho())
        .fechaDeCarga(hecho.getFechaDeCarga())
        .fechaUltimaActualizacion(hecho.getLastUpdate())
        .origen(CONTRIBUYENTE)
        .eliminado(hecho.isEliminado())
        .contribuyente(this.contribuyenteToDTO(hecho.getContribuyente()))
        .solicitudesDeEliminacion(hecho.getSolicitudesDeEliminacion())
        .etiquetas(hecho.getEtiquetas())
        .id(String.format("dinamica:%s", hecho.getId()))
        .build();
  }

  public ContribuyenteDTO contribuyenteToDTO (Contribuyente contribuyente){
    ContribuyenteDTO c = new ContribuyenteDTO();
    c.setId(contribuyente.getId());
    c.setNombre(contribuyente.getNombre());
    c.setApellido(contribuyente.getApellido());
    c.setFechaDeNacimiento(contribuyente.getFechaNacimiento().toString());
    return c;
  }
}