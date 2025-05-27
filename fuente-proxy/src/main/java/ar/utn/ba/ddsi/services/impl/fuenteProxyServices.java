package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.externals.HechoExternalMetamapa;
import ar.utn.ba.ddsi.models.dtos.externals.HechoFuenteApiCatedraResponseDto;
import ar.utn.ba.ddsi.models.dtos.externals.HechoDTO;
import ar.utn.ba.ddsi.models.dtos.externals.HechosMetamapaDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.services.ifuenteProxyServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class fuenteProxyServices implements ifuenteProxyServices {
  private WebClient webClient;
  private String token;

  private int idApiDesastres = 2;
  private int idApiMetaMapa = 1;

  @Autowired
  public fuenteProxyServices(WebClient.Builder webClientBuilder, @Value("${api.desastres-naturales.token}") String apiDesastresNaturalesToken, @Value("${api.desastres-naturales.url}") String apiDesastresNaturalesURL) {
    this.token = apiDesastresNaturalesToken;
    this.webClient = webClientBuilder.baseUrl(apiDesastresNaturalesURL).build();
  }

  @Override
  public List<HechoOutputDTO> getAll(){
    return buscarTodos()
            .block()
            .stream()
            .map(this::externalToOutput)
            .toList();
  }

  @Override
  public List<HechoOutputDTO> getAllDesde(LocalDateTime desde){
     return buscarTodos()
            .block()
            .stream()
            .filter(h-> LocalDateTime.parse(h.getUpdated_at(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"))
                    .isAfter(desde))
            .map(this::externalToOutput)
            .toList(); // tal vez puede devolver un puntero a null
  }

  @Override
  public HechoOutputDTO getById(Long id){
    return externalToOutput(buscarPorId(id).block());
  } //TODO contemplar que no recibamos un null al buscar un hecho en particular o al haacer un getAll



  @Override
  public List<HechoOutputDTO> consumirMetamapa(String baseUrl) {
    WebClient metamapa = WebClient.builder().baseUrl(baseUrl).build();
    return new ArrayList<>();
    /*
    return metamapa
            .get()
            .uri("/hechos")
            .retrieve()
            .bodyToMono(HechosMetamapaDTO.class)
            .map(HechosMetamapaDTO::getHechos)
            .block()
            .stream()
            .map(this::externalMetamapaToHechoOutPut)
            .toList();
      */
  }

  @Override
  public Mono<List<HechoDTO>> buscarTodos(){
    return Flux.range(1, 100)
            .flatMap(page -> webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/desastres")
                            .queryParam("page", page)
                            .queryParam("per_page", 100)
                            .build())
                    .header("Authorization", "Bearer " + this.token)
                    .retrieve()
                    .bodyToMono(HechoFuenteApiCatedraResponseDto.class)
                    .map(HechoFuenteApiCatedraResponseDto::getData))
            .flatMap(Flux::fromIterable)
            .collectList();
  }

  @Override
  public Mono<HechoDTO> buscarPorId(Long id) {//pedimos un hecho
    return webClient
            .get()
            .uri("/desastres/{id}", id)
            .header("Authorization", "Bearer " + this.token)
            .retrieve()
            .bodyToMono(HechoDTO.class);
  }

  @Override
  public HechoOutputDTO externalToOutput (HechoDTO hechoDTO) {
    return HechoOutputDTO.builder()
            .id(String.format("proxy:%s:%s", idApiDesastres, hechoDTO.getId())) // Usamos proxy:<id-api>:<id-hecho>, como solo tenemos una API, usamos siempre el mismo
            .titulo(hechoDTO.getTitulo())
            .descripcion(hechoDTO.getDescripcion())
            .categoria(new Categoria(hechoDTO.getCategoria()))
            .lugarAcontecimiento(new Coordenada(hechoDTO.getLatitud(), hechoDTO.getLongitud()))
            .fechaHecho(LocalDate.parse(hechoDTO.getFecha_hecho(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
            .fechaDeCarga(LocalDateTime.parse(hechoDTO.getCreated_at(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
            .fechaUltimaActualizacion(LocalDateTime.parse(hechoDTO.getUpdated_at(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
            .contenidoMultimedia(null)
            .origen(OrigenHecho.PROXY)
            .eliminado(false)
            .solicitudesDeEliminacion(new ArrayList<>())
            .etiquetas(new HashSet<>())
            .build();
  }

  private HechoOutputDTO externalMetamapaToHechoOutPut(HechoExternalMetamapa metamapaDTO) {
    HechoOutputDTO hecho = HechoOutputDTO.builder()
            .id(String.format("proxy:%s:%s", idApiMetaMapa, metamapaDTO.getId())) // Usamos proxy:<id-api>:<id-hecho>
            .titulo(metamapaDTO.getTitulo())
            .descripcion(metamapaDTO.getDescripcion())
            .categoria(new Categoria(metamapaDTO.getCategoria()))
            .contenidoMultimedia(new ContenidoMultimedia()) //todo ver como esta implementado el constructor
            .origen(metamapaDTO.getOrigen())
            .lugarAcontecimiento(new Coordenada(metamapaDTO.getLugarAcontecimiento()[0],metamapaDTO.getLugarAcontecimiento()[1]))
            .fechaHecho(metamapaDTO.getFechaHecho())
            .fechaDeCarga(metamapaDTO.getFechaDeCarga())
            .fechaUltimaActualizacion(metamapaDTO.getFechaUltimaActualizacion())
            .contribuyenteId(metamapaDTO.getContribuyenteId())
            .solicitudesDeEliminacion(metamapaDTO.getSolicitudesDeEliminacion())
            .etiquetas(metamapaDTO.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toCollection(HashSet::new)))
            .build();
    return hecho;
  }

}