package ar.utn.ba.ddsi.services.impl;


import ar.utn.ba.ddsi.models.dto.input.EtiquetaDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudCreadaDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeModificacionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudResueltaDTO;
import ar.utn.ba.ddsi.models.entities.ContenidoMultimedia;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeModificacion;
import ar.utn.ba.ddsi.models.exceptions.NoHaySolicitudPendienteException;
import ar.utn.ba.ddsi.models.exceptions.SolicitudFueraDePlazoException;
import ar.utn.ba.ddsi.models.exceptions.UnauthorizedException;
import ar.utn.ba.ddsi.models.repositories.ISolicitudesRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import ar.utn.ba.ddsi.services.internal.ImageUploaderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudesService implements ISolicitudesService {
  private final IHechosService hechosService;
  private final ISolicitudesRepository solicitudesRepository;
  private final ImageUploaderService imageUploaderService;

  public SolicitudesService(IHechosService hechosService, ISolicitudesRepository solicitudesRepository, ImageUploaderService imageUploaderService) {
    this.hechosService = hechosService;
    this.solicitudesRepository = solicitudesRepository;
    this.imageUploaderService = imageUploaderService;
  }


  // --- Métodos expuestos al controller -------------------------------------------------------------------------------


  @Override
  public SolicitudResueltaDTO procesarSoliPendiente(Long id, ResolucionDTO resolucion, Long adminId) {

    Hecho hechoViejo = hechosService.getById(id);
    if (hechoViejo == null) {
      throw new EntityNotFoundException("Hecho no encontrado");
    }

    if (hechoViejo.getSolicitudDeModificacion() == null) {
      throw new NoHaySolicitudPendienteException(id);
    }

    SolicitudDeModificacion solicitud = hechoViejo.getSolicitudDeModificacion();
    solicitud.resolver(resolucion, adminId);

    hechosService.guardarCambios(hechoViejo);

    return new SolicitudResueltaDTO(solicitud.getId(), solicitud.getEstado(), hechosService.hechoToDTO(hechoViejo));
  }

  @Override
  public SolicitudCreadaDTO crearSolModificacion(Long id, HechoInputDTO hechoInput,List<MultipartFile> imagenesNuevas, Long contribuyenteId){
    Hecho hecho = this.hechosService.getById(id);

    if (!hecho.getContribuyenteId().equals(contribuyenteId)) {
      throw new UnauthorizedException(contribuyenteId);
    }

    if(ChronoUnit.DAYS.between(hecho.getFechaDeCarga(), LocalDateTime.now()) > 7) {
      throw new SolicitudFueraDePlazoException(id);
    }

    List<String> imagenesOriginales = hecho.todoMultimediaString();
    List<String> imagenesViejas = hechoInput.getContenidosMultimedia();

    if (imagenesOriginales != null && !imagenesOriginales.isEmpty()) {
      List<String> imagenesParaBorrar = imagenesOriginales.stream()
          .filter(url -> imagenesViejas == null || !imagenesViejas.contains(url))
          .toList();

      for (String urlBorrar : imagenesParaBorrar) {
        imageUploaderService.deleteFile(urlBorrar);
      }
    }

    List<String> urlsFinales = new ArrayList<>();

    if (imagenesViejas!= null) {
      urlsFinales.addAll(imagenesViejas);
    }

    if (imagenesNuevas != null && !imagenesNuevas.isEmpty()) {
      for (MultipartFile archivo : imagenesNuevas) {
        if (archivo.isEmpty()) continue;

        String urlNueva = imageUploaderService.uploadFile(archivo);
        urlsFinales.add(urlNueva);
      }
    }



    SolicitudDeModificacion nuevaSolicitudDeModificacion =
            SolicitudDeModificacion.builder()
                    .hecho(hecho)
                    .fechaDeCarga(LocalDateTime.now())
                    .tituloNuevo(hechoInput.getTitulo())
                    .descripcionNueva(hechoInput.getDescripcion())
                    .categoriaNueva(hechoInput.getCategoria())
                    .latitudNueva(hechoInput.getLatitud())
                    .longitudNueva(hechoInput.getLongitud())
                    .fechaHechoNueva(hechoInput.getFechaHecho())
                    .etiquetasNuevas(hechoInput.getEtiquetas() != null ? hechoInput.getEtiquetas().stream()
                            .map(EtiquetaDTO::getNombre)
                            .collect(Collectors.toCollection(HashSet::new))
                            : new HashSet<>()
                    )
                    .contenidosMultimediaNuevos(urlsFinales)
                    .estado(EstadoSolicitud.PENDIENTE)
                    .build();

    hecho.setSolicitudDeModificacion(nuevaSolicitudDeModificacion);
    hechosService.guardarCambios(hecho);

    return new SolicitudCreadaDTO(hecho.getSolicitudDeModificacion().getId(), hecho.getId());
  }


  @Override
  public List<SolicitudDeModificacionOutputDTO> obtenerSolicitudesDeModificacion(){
    return solicitudesRepository
            .findAll()
            .stream()
            .map(SolicitudDeModificacionOutputDTO::fromEntity)
            .toList();
  }

  @Override
  public List<SolicitudDeModificacionOutputDTO> obtenerSolicitudesDeModificacionPendientes(){
    return solicitudesRepository
            .findAllByEstado(EstadoSolicitud.PENDIENTE)
            .stream()
            .map(SolicitudDeModificacionOutputDTO::fromEntity)
            .toList();
  }

  @Override
  public SolicitudDeModificacionOutputDTO obtenerSolicitudDeModificacionPorID(Long id){
    return obtenerSolicitudesDeModificacionPendientes().stream()
            .filter(solicitud -> solicitud.getId().equals(id))
            .findFirst()
            .orElse(null);
  }

  @Override
  public List<Long> obtenerIDsModificacionPendientes(){
      return obtenerSolicitudesDeModificacionPendientes().stream().map(SolicitudDeModificacionOutputDTO::getId).toList();
  }

}
