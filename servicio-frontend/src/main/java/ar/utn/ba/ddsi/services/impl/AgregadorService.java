package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.RestPage;
import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public Page<HechoDTO> buscarHechos(int page, int size) {
    return agregadorWebClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/hechos")
        .queryParam("page", page)
        .queryParam("size", size)
        .build())
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<RestPage<HechoDTO>>() {})
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

    @Override
    public Page<HechoDTO> pedirHechosDeContribuyentePaginado(int page, int size) {
      String url = UriComponentsBuilder
              .fromHttpUrl(agregadorBaseUrl + "/api/hechos/contribuyente")
              .queryParam("page", page)
              .queryParam("size", size)
              .toUriString();

      return webApiCallerService.getPage(url, new ParameterizedTypeReference<RestPage<HechoDTO>>() {});
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


  public void eliminarColeccion(String identificador){
      webApiCallerService.delete(
              agregadorBaseUrl + "/api/colecciones/" + identificador);
  }


    public List<ColeccionDTO> pedirColecciones(){
        return agregadorWebClient.get()
                .uri(agregadorBaseUrl + "/api/colecciones")
                .retrieve()
                .bodyToFlux(ColeccionDTO.class)
                .collectList()
                .block();
    }

    public ColeccionDTO pedirColeccionPorId(String identificador){
        return agregadorWebClient.get()
                .uri("/api/colecciones/{identificador}", identificador)
                .retrieve()
                .bodyToMono(ColeccionDTO.class)
                .block();
    }

  public void actualizarColeccion(ColeccionOutputDTO coleccion){
      System.out.println("LLEGUE ACÁ");
      webApiCallerService.put(
              agregadorBaseUrl + "/api/colecciones/actualizar",
              coleccion,
              void.class
      );
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

    public ColeccionConHechosDTO pedirColeccionConHechos(String id, int pagina, int tamanio, String modo){
        String urlBase = agregadorBaseUrl + "/api/colecciones/" + id + "/con-hechos"
                + "?page=" + pagina + "&size=" + tamanio;
        String urlFinal = UriComponentsBuilder.fromHttpUrl(urlBase)
                .queryParam("modo", modo)
                .toUriString();

        return agregadorWebClient.get()
                .uri(urlFinal)
                .retrieve()
                .bodyToMono(ColeccionConHechosDTO.class)
                .block();
    }


    public List<CategoriaDTO> pedirCategorias() {
        return agregadorWebClient.get()
                .uri(agregadorBaseUrl + "/api/categorias")
                .retrieve()
                .bodyToFlux(CategoriaDTO.class)
                .collectList()
                .block();
    }



   public CategoriaDTO pedirCategoriaPorID(Long id){
      return agregadorWebClient.get()
              .uri("/api/categorias/{id}", id)
              .retrieve()
              .bodyToMono(CategoriaDTO.class)
              .block();
   }

    public SolicitudDeEliminacionDTO solicitarEliminacion(SolicitudDeEliminacionOutputDTO solicitud) {
      return webApiCallerService.post(
                    agregadorBaseUrl + "/api/solicitudes",
                    solicitud,
                    SolicitudDeEliminacionDTO.class);

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

    // Panel de control

    public Long pedirSolicitudesAceptadas(){
        return webApiCallerService.get(
                agregadorBaseUrl + "/api/solicitudes/cantidadAceptadas",
                Long.class
        );
    }

    public Long pedirSolicitudesRechazadas(){
      return webApiCallerService.get(
              agregadorBaseUrl + "/api/solicitudes/cantidadRechazadas",
              Long.class
      );
    }

    public HechoDTO pedirUltimoHechoCargado(){
      return agregadorWebClient.get()
              .uri("/api/hechos/ultimo")
              .retrieve()
              .bodyToMono(HechoDTO.class)
              .block();
    }

    public Integer pedirCantidadDeHechosEnElSistema(){
      return webApiCallerService.get(
              agregadorBaseUrl + "/api/hechos/disponible/cantidad",
                Integer.class
      );
    }

    public void actualizarHechos(){
       webApiCallerService.post(
              agregadorBaseUrl + "/api/hechos/actualizarTodos",
               new HashMap<String, String>(),
              Void.class
      );
    }

    public void consensuarColecciones(){
      webApiCallerService.post(
              agregadorBaseUrl + "/api/colecciones/consensuar",
              new HashMap<String, String>(),
              Void.class
      );
    }
}
