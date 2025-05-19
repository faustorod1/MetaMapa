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
  public void modificarHecho(HechoInputDTO amodificar,HechoInputDTO nuevo){ //Todo ver como llega la request de modificar, si con el hecho nuevo y el id viejo o con dos hechos distintos
    if(ChronoUnit.DAYS.between(nuevo.getFechaDeCarga(), LocalDateTime.now()) > 7){
      SolicitudDeModificacion nuevaSolicitudDeModificacion = new SolicitudDeModificacion(amodificar,nuevo);
      amodificar.setSolicitudDeModificacion(nuevaSolicitudDeModificacion);
    }
    //aqui se podria arrojar una excepcion si el usuario intenta modificar luego de los 7 dias.
  }


  @Override
  public List<HechoOutputDTO> obtenerHechos(Boolean pendiente) { // true si quiero q me de los pendientes
    return hechosRepository.findByPendiente(pendiente);
  }

  @Override
  public void procesarPendiente(Hecho hecho, EstadoSolicitud estadoNuevo){
    Hecho h = hechosRepository.findById(hecho.getId());
    h.getSolicitudDeModificacion().resolver(estadoNuevo);
  }

  @Override
  public List<HechoOutputDTO> obtenerHechosDe(Contribuyente contribuyente){
    return hechosRepository.findByContribuyente(contribuyente);
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
        .contribuyente(hechoInputDTO.getContribuyente()) //TODO A VER
        .etiquetas(hechoInputDTO.getEtiquetas())
        .id(null)
        .build();
  }
  public HechoOutputDTO HechoToDTO (Hecho hecho){
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
        .contribuyente(hecho.getContribuyente()) //TODO A VER
        .solicitudesDeEliminacion(hecho.getSolicitudesDeEliminacion())
        .etiquetas(hecho.getEtiquetas())
        .build();
  }
}