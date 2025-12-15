package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeModificacionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeModificacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.services.IDinamicaService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DinamicaService implements IDinamicaService {
    WebClient dinamicaWebClient;
    String dinamicaBaseUrl;
    WebApiCallerService webApiCallerService;

    public DinamicaService(@Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaUrl, WebApiCallerService webApiCallerService) {
        dinamicaBaseUrl = fuenteDinamicaUrl;
        dinamicaWebClient = WebClient.builder().baseUrl(fuenteDinamicaUrl).build();
        this.webApiCallerService = webApiCallerService;
    }

    public void cargarHecho(HechoOutputDTO hecho, List<MultipartFile> imagenes) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("hecho", hecho)
                .header("Content-Type", "application/json");

        if (imagenes != null) {
            for (MultipartFile img : imagenes) {
                builder.part("contenidosMultimedia", img.getResource()).filename(Objects.requireNonNull(img.getOriginalFilename())).contentType(MediaType.MULTIPART_FORM_DATA);
                ;
            }
        }

        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        webApiCallerService.postMultipart(
                dinamicaBaseUrl + "/api/hechos",
                multipartBody,
                String.class
        );
    }

    public HechoDTO pedirHecho(Long id) {             // Al front solo traemos hechos disponibles. Los eliminados no
        return webApiCallerService.get(
                dinamicaBaseUrl + "/api/hechos/" + id,
                HechoDTO.class);

    }

    public List<Long> pedirIDsHechos() {
        return webApiCallerService.getList(
                dinamicaBaseUrl + "/api/hechos/ids",
                Long.class);
    }

    public void modificarHecho(Long id_hecho, HechoOutputDTO hecho, List<MultipartFile> imagenesNuevas){

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("hechoNuevo", hecho)
            .header("Content-Type", "application/json");

        if (imagenesNuevas != null) {
            for (MultipartFile img : imagenesNuevas) {
                if (!img.isEmpty()) {
                    builder.part("fotos", img.getResource())
                        .filename(Objects.requireNonNull(img.getOriginalFilename()))
                        .contentType(MediaType.MULTIPART_FORM_DATA);
                }
            }
        }
        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        String rta = webApiCallerService.putMultipart(dinamicaBaseUrl + "/api/solicitudes/" + id_hecho, multipartBody, String.class);
    }


    public List<Long> pedirIDsModificacionesPendientes(){
        return webApiCallerService.getList(
                dinamicaBaseUrl + "/api/solicitudes/idsModificacionesPendientes",
                Long.class
        );
    }

    public SolicitudDeModificacionDTO pedirSolicitudDeModificacion(Long solicitudId){
        return webApiCallerService.get(
                dinamicaBaseUrl + "/api/solicitudes/modificacion/" + solicitudId,
                SolicitudDeModificacionDTO.class
        );
    }

    public void resolverModificacion(Long hechoId, ResolucionSolicitudDeModificacionOutputDTO resolucion){
        webApiCallerService.patch(
           dinamicaBaseUrl + "/api/solicitudes/" + hechoId + "/estado",
               resolucion,
               Void.class
        );
    }


}