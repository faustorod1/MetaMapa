package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.DescripcionSolicitudException;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import ar.utn.ba.ddsi.models.repositories.ISolicitudesRepository;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SolicitudesService implements ISolicitudesService {
  private final HechosService hechosService;
  private ISolicitudesRepository solicitudesRepository;

  private static final Set<String> PALABRAS_SPAM = Set.of(
      "gratis", "urgente", "dinero", "oferta", "promoción", "click", "regalo", "ganá", "millón", "suscribite"
  );

  public SolicitudesService(ISolicitudesRepository solicitudesRepository, HechosService hechosService) {
    this.solicitudesRepository = solicitudesRepository ;
    this.hechosService = hechosService;
  }

  private boolean esSpam(String descripcion){
    String[] palabras = descripcion.toLowerCase().split("\\W+");

    Map<String, Integer> frecuencia = new HashMap<>();
    int total = 0;
    int palabrasSpamDetectadas = 0;

    for (String palabra : palabras) {
      if (palabra.isBlank()) continue;
      total++;
      frecuencia.put(palabra, frecuencia.getOrDefault(palabra, 0) + 1);
      if (PALABRAS_SPAM.contains(palabra)) palabrasSpamDetectadas++;
    }

    double repeticionMaxima = frecuencia.values().stream().mapToInt(i -> i).max().orElse(0) / (double) total;
    double promedioPalabrasSpam = palabrasSpamDetectadas / (double) total;

    return repeticionMaxima > 0.4 || promedioPalabrasSpam > 0.25 || frecuencia.size() <= 5;
  }

  @Override
  public boolean crearSolicitud(SolicitudDeEliminacionInputDTO solicitudDto) {
    if (esSpam(solicitudDto.getDescripcion())) {
      return false;
    }

    try {
      SolicitudDeEliminacion solicitud = solicitudDeEliminacionFromDTO(solicitudDto);
      solicitudesRepository.save(solicitud);
      return true;

    } catch (DescripcionSolicitudException e) {
      return false;
    }
  }

  @Override
  public void modificarEstadoSolicitud(Long id, ResolucionSolicitudDeEliminacionDTO resolucionDto) {
    solicitudesRepository.resolver(id, resolucionDto.getAdministradorQueResolvio(), resolucionDto.getEstadoSolicitud());
  }

  @Override
  public SolicitudDeEliminacion solicitudDeEliminacionFromDTO(SolicitudDeEliminacionInputDTO dto) throws DescripcionSolicitudException {
    Hecho hecho = hechosService.obtenerPorId(dto.getHechoId());
    return new SolicitudDeEliminacion(hecho, dto.getDescripcion(), contribuyenteFromContribuyenteDTO(dto.getSolicitante()));
  }

  private Contribuyente contribuyenteFromContribuyenteDTO(ContribuyenteDTO dto) {
    return new Contribuyente(dto.getId(), dto.getNombre(), dto.getApellido(), LocalDate.parse(dto.getFechaDeNacimiento(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
  }
  private ContribuyenteDTO contribuyenteToDTO(Contribuyente contribuyente) {
    ContribuyenteDTO dto = new ContribuyenteDTO();
    dto.setId(contribuyente.getId());
    dto.setNombre(contribuyente.getNombre());
    dto.setApellido(contribuyente.getApellido());
    dto.setFechaDeNacimiento(contribuyente.getFechaNacimiento().toString());
    return dto;
  }

  @Override
  public SolicitudDeEliminacionOutputDTO solicititudDeEliminacionToDTO(SolicitudDeEliminacion solicitud) {
    SolicitudDeEliminacionOutputDTO dto = new SolicitudDeEliminacionOutputDTO();
    dto.setId(solicitud.getId());
    dto.setDescripcion(solicitud.getDescripcion());
    dto.setHechoId(solicitud.getHecho().getId());
    dto.setFechaDeCarga(solicitud.getFechaDeCarga());
    dto.setFechaDeResolucion(solicitud.getFechaDeResolucion());
    dto.setEstado(solicitud.getEstado());
    dto.setSolicitante(contribuyenteToDTO(solicitud.getSolicitante()));
    dto.setAdministradorQueResolvio(solicitud.getAdministradorQueResolvio());
    return dto;
  }
}
