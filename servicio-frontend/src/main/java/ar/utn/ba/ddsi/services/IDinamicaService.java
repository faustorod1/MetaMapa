package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeModificacionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeModificacionOutputDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDinamicaService {
    void cargarHecho(HechoOutputDTO hecho, List<MultipartFile> imagenes);
    void modificarHecho(Long id_hecho, HechoOutputDTO hecho);
    List<Long> pedirIDsModificacionesPendientes();
    SolicitudDeModificacionDTO pedirSolicitudDeModificacion(Long solicitudId);
    void resolverModificacion(Long hechoId, ResolucionSolicitudDeModificacionOutputDTO resolucion);
    HechoDTO pedirHecho(Long id);
    List<Long> pedirIDsHechos();
}
