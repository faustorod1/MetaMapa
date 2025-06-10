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


@Service
public class SolicitudesService implements ISolicitudesService {
  private final HechosService hechosService;
  private ISolicitudesRepository solicitudesRepository;

  public SolicitudesService(ISolicitudesRepository solicitudesRepository, HechosService hechosService) {
    this.solicitudesRepository = solicitudesRepository;
    this.hechosService = hechosService;
  }

  @Override
  public String crearSolicitud(SolicitudDeEliminacionInputDTO solicitudDto) {
    SolicitudDeEliminacion solicitud = solicitudDeEliminacionFromDTO(solicitudDto);
    solicitudesRepository.save(solicitud);
    return switch (solicitud.getEstado()) {
      case PENDIENTE -> "Solicitud creada correctamente";
      case RECHAZADA_POR_SPAM -> "Solicitud rechazada por spam";
      case RECHAZADA_POR_FALTA_DE_CARACTERES -> "Solicitud rechazada por insuficientes caracteres";
      default -> "guat? (ㆆ _ ㆆ)";
    };

    /*
  @Service
  public class ElementoService {

    @Autowired
    private WebClient webClient;

    @Async
    public void eliminarYNotificar(Long id) {
        // Lógica local
        eliminarLocalmente(id);

        // Llamada no bloqueante a otra API
        webClient.delete()
                .uri("http://otra-api.com/api/elementos/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> System.out.println("Notificación exitosa"))
                .doOnError(error -> System.err.println("Error notificando: " + error.getMessage()))
                .subscribe(); // Muy importante: activa la llamada no bloqueante
    }

    private void eliminarLocalmente(Long id) {
        System.out.println("Elemento " + id + " eliminado localmente.");
    }
}
   */
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
