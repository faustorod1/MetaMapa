package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class AgregadorService implements IAgregadorService {
  WebClient agregadorWebClient;
  String agregadorBaseUrl;
  WebApiCallerService webApiCallerService;

  public AgregadorService(@Value("${servicio.agregador.api.base-url}") String agregadorUrl, WebApiCallerService webApiCallerService) {
    agregadorBaseUrl = agregadorUrl;
    agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
    this.webApiCallerService = webApiCallerService;
  }

  public List<HechoDTO> buscarHechos() {
    return agregadorWebClient.get()
        .uri("/api/hechos")
        .retrieve()
        .bodyToFlux(HechoDTO.class)
        .collectList()
        .block();
  }


  public List<FuenteDTO> buscarFuentes(){
    return webApiCallerService.getList(
            agregadorBaseUrl + "/api/colecciones/fuentes",
            FuenteDTO.class);
  }


  public HechoDTO pedirHecho(Long id) {             // Al front solo traemos hechos disponibles. Los eliminados no
    return agregadorWebClient.get()
            .uri("/api/hechos/disponible/{id}", id)
            .retrieve()
            .bodyToMono(HechoDTO.class)
            .block();
   }

   public List<HechoDTO> pedirHechosDeContribuyente() {
     return webApiCallerService.getList(
         agregadorBaseUrl + "/api/hechos/contribuyente",
         HechoDTO.class);
   }

   public List<String> pedirIdentificadoresDeColecciones(){
      return webApiCallerService.getList(
              agregadorBaseUrl + "/api/colecciones/identificadores",
              String.class);
   }

    public void cargarColeccion(ColeccionOutputDTO coleccion){
      webApiCallerService.post(
              agregadorBaseUrl + "/api/colecciones/cargar",
              coleccion,
              void.class);
  }


    public List<ColeccionConHechosDTO> pedirColeccionesConHechos(){
      return webApiCallerService.getList(
                agregadorBaseUrl + "/api/colecciones/con-hechos",
              ColeccionConHechosDTO.class);
  }

    public List<ColeccionConHechosDTO> pedirColeccionesConHechosCurados(){
        return webApiCallerService.getList(
                agregadorBaseUrl + "/api/colecciones/con-hechos-curados",
                ColeccionConHechosDTO.class);
    }


   public List<CategoriaDTO> pedirCategorias() {
      return webApiCallerService.getList(
              agregadorBaseUrl + "/api/categorias",
              CategoriaDTO.class
      );
   }

   public CategoriaDTO pedirCategoriaPorID(Long id){
      return agregadorWebClient.get()
              .uri("/api/categorias/{id}", id)
              .retrieve()
              .bodyToMono(CategoriaDTO.class)
              .block();
   }

   public void solicitarEliminacion(SolicitudDeEliminacionOutputDTO solicitud) {
      webApiCallerService.post(
              agregadorBaseUrl + "/api/solicitudes",
                solicitud,
                String.class);
   }

   public List<Long> pedirIDsEliminacionesPendientes(){
      return webApiCallerService.getList(
              agregadorBaseUrl + "/api/solicitudes/idsEliminacionPendientes",
              Long.class
      );
   }

    public SolicitudDeEliminacionDTO pedirSolicitudDeEliminacion(Long id) {
        return webApiCallerService.get(
                agregadorBaseUrl + "/api/solicitudes/eliminacion/" + id,
                SolicitudDeEliminacionDTO.class
        );
    }

    public void resolverEliminacion(Long id, ResolucionSolicitudDeEliminacionOutputDTO resolucion){
        webApiCallerService.patch(
                agregadorBaseUrl + "/api/solicitudes/eliminacion/" + id + "/estado",
                resolucion,
                Void.class
        );
    }

    public List<Long> pedirIDsExternosDinamica(){
      return webApiCallerService.getList(
              agregadorBaseUrl + "/api/hechos/dinamica/idsExternos",
              Long.class
      );
    }

    public HechoDTO pedirHechoDinamica(Long id_externo) {
      return webApiCallerService.get(
              agregadorBaseUrl + "/api/hechos/dinamica/" + id_externo,
                 HechoDTO.class
      );

    }

}
