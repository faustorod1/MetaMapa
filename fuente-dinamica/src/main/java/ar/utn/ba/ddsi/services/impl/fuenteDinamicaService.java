package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeModificacion;
import ar.utn.ba.ddsi.models.repositories.iHechosRepository;
import ar.utn.ba.ddsi.services.iFuenteDinamicaService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
  public HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO) {
    Hecho hecho = this.DtoToHecho(hechoInputDTO);
    hechosRepository.save(hecho);
    return this.hechoToDTO(hecho);
  }

  @Override
  public HechoOutputDTO modificarHecho(HechoInputDTO amodificar,HechoInputDTO nuevo){ //Todo cambiar a con el hecho nuevo y el id del viejo
    Hecho h = DtoToHecho(nuevo);
    Hecho hvie = DtoToHecho(amodificar);
    if(ChronoUnit.DAYS.between(h.getFechaDeCarga(), LocalDateTime.now()) > 7){
      SolicitudDeModificacion nuevaSolicitudDeModificacion = new SolicitudDeModificacion(hvie,h);
      hvie.setSolicitudDeModificacion(nuevaSolicitudDeModificacion);
      return hechoToDTO(h);
    }
    return null;
  }

  @Override
  public List<HechoOutputDTO> obtenerHechosPendientes(Boolean pendiente) { // true si quiero q me de los pendientes
    return hechosRepository.findByPendiente(pendiente).stream().map(this::hechoToDTO).collect(Collectors.toList());
  }

  @Override
  public List<HechoOutputDTO> obtenerHechosDe(Contribuyente contribuyente){
    return hechosRepository.findByContribuyente(contribuyente).stream().map(this::hechoToDTO).collect(Collectors.toList());
  }

  @Override
  public List<HechoOutputDTO> obtenerTodosHechos(){
    return hechosRepository.findAll().stream().map(this::hechoToDTO).collect(Collectors.toList());
  }

  @Override
  public void procesarPendiente(Hecho hecho, EstadoSolicitud estadoNuevo){ //Todo aca tal vez viene un id
    Hecho h = hechosRepository.findById(hecho.getId());
    h.getSolicitudDeModificacion().resolver(estadoNuevo);
    if(estadoNuevo == ACEPTADA || estadoNuevo == ACEPTADACONSUGERENCIA){
      Hecho hechoNuevo = h.getSolicitudDeModificacion().getHechoNuevo();
      hechoNuevo.setLastUpdate(LocalDateTime.now());
      hechosRepository.update(h, hechoNuevo);
    }
  }

  public Hecho DtoToHecho (HechoInputDTO hechoInputDTO){
    return Hecho.builder()
        .titulo(hechoInputDTO.getTitulo())
        .descripcion(hechoInputDTO.getDescripcion())
        .categoria(hechoInputDTO.getCategoria())
        .contenidoMultimedia(hechoInputDTO.getContenidoMultimedia())
        .lugarAcontecimiento(hechoInputDTO.getLugarAcontecimiento())
        .fechaHecho(hechoInputDTO.getFechaHecho())
        .origen(CONTRIBUYENTE)
        .fechaDeCarga(LocalDateTime.now()) //TODO revisar si puede modificarse una vez q ya se creo al hecho. Sino usar fechaDeUltimaModificacion
        .eliminado(false)
        .contribuyente(hechoInputDTO.getContribuyente())
        .etiquetas(hechoInputDTO.getEtiquetas())
            .lastUpdate(LocalDateTime.now())
        .id(null)
        .build();
  }
  public HechoOutputDTO hechoToDTO (Hecho hecho){
    return HechoOutputDTO.builder()
        .titulo(hecho.getTitulo())
        .descripcion(hecho.getDescripcion())
        .categoria(hecho.getCategoria())
        .contenidoMultimedia(hecho.getContenidoMultimedia())
        .lugarAcontecimiento(hecho.getLugarAcontecimiento())
        .fechaHecho(hecho.getFechaHecho())
        .fechaDeCarga(hecho.getFechaDeCarga())
        .origen(CONTRIBUYENTE)
        .eliminado(hecho.isEliminado())
        .contribuyente(hecho.getContribuyente())
        .solicitudesDeEliminacion(hecho.getSolicitudesDeEliminacion())
        .etiquetas(hecho.getEtiquetas())
        .id(hecho.getId())
            .lastUpdate(hecho.getLastUpdate())
        .build();
  }
}