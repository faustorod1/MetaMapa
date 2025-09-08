package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorrefRequestDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorreferenciacionDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.ResultadoGeoDTO;
import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.external.HechoFuenteDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.entities.ubicacion.Municipio;
import ar.utn.ba.ddsi.models.repositories.ICategoriaRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.models.repositories.IMunicipiosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HechosService implements IHechosService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private IHechosRepository hechosRepository;

    private final Map<OrigenHecho, WebClient> webClients = new HashMap<OrigenHecho, WebClient>();
    private LocalDateTime fechaUltimaActualizacion = LocalDate.parse("01/01/1000", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();

    private ICategoriaRepository categoriaRepository;
    private IMunicipiosRepository municipiosRepository;


    @Autowired
    public HechosService(IHechosRepository hechosRepository, @Value("${fuente.estatica.api.base-url}") String fuenteEstaticaApiBaseUrl, @Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaApiBaseUrl, @Value("${fuente.proxy.api.base-url}") String fuenteProxyApiBaseUrl, ApplicationEventPublisher applicationEventPublisher) {
        this.hechosRepository = hechosRepository;
        this.webClients.put(OrigenHecho.DATASET, WebClient.builder().baseUrl(fuenteEstaticaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.CONTRIBUYENTE, WebClient.builder().baseUrl(fuenteDinamicaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.PROXY, WebClient.builder().baseUrl(fuenteProxyApiBaseUrl).build());
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // --- Métodos expuestos al controller -------------------------------------------------------------------------------

    // TODO:
    // Si en el agregador tenemos siempre los hechos de fuentes MetaMapa, al sumarle los que pedimos en tiempo
    // real, quedan repetidos
    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos(Map<String, String> params) {
        Criterio criterio = new Criterio(params);

        Mono<List<Hecho>> hechosLocales = Mono.fromCallable(hechosRepository::findAll);
        Mono<List<Hecho>> hechosMetaMapa = this.getFromMetaMapa();

        Mono<List<Hecho>> todos = Mono.zip(hechosLocales, hechosMetaMapa)
                .map(tuple ->
                        Stream.concat(tuple.getT1().stream(), tuple.getT2().stream()).toList()
                ).map(criterio::aplicarA);

        return todos.map(list -> list.stream().map(this::hechoOutputDTO).toList());
    }


    // ---- Métodos de trabajo interno -------------------------------------------------------------------------------

    @Override
    public Hecho obtenerPorId(Long id){
        return hechosRepository.findById(id).orElse(null);
    }

    @Override
    public Mono<Void> actualizarHechos(){
        Mono<List<Hecho>> monoEstatica = crearMonoPeticionHechos(webClients.get(OrigenHecho.DATASET));
        Mono<List<Hecho>> monoDinamica = crearMonoPeticionHechos(webClients.get(OrigenHecho.CONTRIBUYENTE));
        Mono<List<Hecho>> monoProxy = crearMonoPeticionHechos(webClients.get(OrigenHecho.PROXY));

        Mono<Void> mono = Mono.zip(monoEstatica, monoDinamica, monoProxy)
                .doOnNext(tupla -> {
                    List<Hecho> hechos = new ArrayList<>(tupla.getT1());

                    List<Long> ids = hechos.stream().map(Hecho::getId).toList();
                    List<Hecho> hechosAModificar = hechosRepository.findAllByIdIn(ids);

                    hechos.addAll(tupla.getT2());
                    hechos.addAll(tupla.getT3());

                    normalizarCategoria(hechos);
                    normalizarUbicacion(hechos).subscribe(hechosNormalizados -> {
                                // Para cada hecho normalizado, si ya existía en el agregador, lo actualiza
                                for (int i = 0; i < hechosNormalizados.size(); i++) {
                                    Hecho hechoNormalizado = hechosNormalizados.get(i);
                                    Hecho viejo = hechosAModificar.stream()
                                            .filter(h -> h.getIdExterno().equals(hechoNormalizado.getIdExterno()))
                                            .findFirst().orElse(null);
                                    if (viejo != null) {
                                        modificarHecho(viejo, hechoNormalizado);
                                        hechosNormalizados.set(i, viejo);
                                    }
                                }
                                hechosRepository.saveAll(hechosNormalizados);
                            }
                    );
                })
                .then();

        fechaUltimaActualizacion = LocalDateTime.now();

        
        // Código necesario para activar el evento
        List<Hecho> todosLosHechos = Mono.zip(monoEstatica, monoDinamica, monoProxy)
            .map(tuple -> Stream.of(tuple.getT1(), tuple.getT2(), tuple.getT3())
                .flatMap(List::stream)
                .toList())
                .block();
        applicationEventPublisher.publishEvent(new HechosModificadosEvent(todosLosHechos));

        return mono;
    }

    @Override
    public void normalizarCategoria(List<Hecho> hechos){
        List<Categoria> listaDeCategorias = categoriaRepository.findAll();

        hechos.forEach(hecho -> {
            String categoriaOriginal = hecho.getCategoria().getNombre();

            for (Categoria categoria : listaDeCategorias) {
                if (categoria.esLaMisma(categoriaOriginal)) {
                    hecho.setCategoria(categoria);
                    return;
                }
            }

            hecho.setCategoria(null);
        });
    }

    @Override
    public Mono<List<Hecho>> normalizarUbicacion(List<Hecho> hechos){
        List<Hecho> requierenGeorref = new ArrayList<>();
        hechos.forEach(hecho -> {
            if (hecho.getLugarAcontecimiento() != null) {
                requierenGeorref.add(hecho);
            }
            else {
                // TODO Normalización tipo categoría
            }
        });

        Map<Coordenada, List<Hecho>> hechosPorCoordenada = requierenGeorref.stream()
                .collect(Collectors.groupingBy(Hecho::getLugarAcontecimiento));
        List<Coordenada> coordenadas = hechosPorCoordenada.keySet().stream().toList();

      return georreferenciacionInversa(coordenadas).map(municipiosPorCoordenada -> {
          municipiosPorCoordenada.forEach((coordenada, municipio) ->
              hechosPorCoordenada.get(coordenada).forEach(hechoAModificar ->
                  hechoAModificar.setMunicipio(municipio)
              )
          );
          return hechos;
      });
    }

    public Mono<Map<Coordenada, Municipio>> georreferenciacionInversa(List<Coordenada> coordenadas){

        List<GeorrefRequestDTO> coordFormat = coordenadas.stream().map(this::coordToGeorrefRequestDto).toList();
        List<Municipio> municipios = municipiosRepository.findAll();

        return webClients.get(OrigenHecho.PROXY)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("https://apis.datos.gob.ar/georef/api/ubicacion")
                        .queryParam("Ubicaciones", coordFormat)
                        .build())
                .retrieve()
                .bodyToMono(GeorreferenciacionDTO.class)
                .map(dto -> Optional.ofNullable(dto.getResultados()).orElse(Collections.emptyList()))
                .map(listaRes -> listaRes.stream().map(ResultadoGeoDTO::getUbicacion).toList())
                .map(ubicaciones -> {
                    Map<Coordenada, Municipio> municipiosPorCoordenada = new HashMap<>();
                    ubicaciones.forEach(ubicacion -> {
                        Municipio municipio = municipios.stream().filter(m ->
                                        m.getNombre().equals(ubicacion.getMunicipio_nombre()) &&
                                                m.getProvincia().getNombre().equals(ubicacion.getProvinicia_nombre()))
                                .findFirst().orElse(null);
                        Coordenada coord = new Coordenada(ubicacion.getLat(), ubicacion.getLon());
                        municipiosPorCoordenada.put(coord, municipio);
                    });
                    return municipiosPorCoordenada;
                });
    }


    @Override
    public Mono<List<Hecho>> getFromMetaMapa() {
        return webClients.get(OrigenHecho.PROXY)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/hechos/metamapaInstance")     // TODO: Ver si nos conviene pedir una fuente MetaMapa específica
                        .build()
                )
                .retrieve()
                .bodyToFlux(HechoFuenteDTO.class)
                .map(this::hechoFromHechoFuenteDTO)
                .collectList();
    }


    private Mono<List<Hecho>> crearMonoPeticionHechos(WebClient webClient) {
        String fechaUltimaActualizacionStr = fechaUltimaActualizacion.format(DateTimeFormatter.ISO_DATE_TIME);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/hechos")
                        .queryParam("desde", fechaUltimaActualizacionStr)  //    /api/hechos?desde=fecha
                        .build()
                )
                .retrieve()
                .bodyToFlux(HechoFuenteDTO.class)
                .map(this::hechoFromHechoFuenteDTO)
                .collectList();
    }

    // TODO: Está bien esto?
    public void guardarCambios(Hecho hecho) {
        hechosRepository.save(hecho);
    }

    // No lo guarda, solo actualiza sus atributos
    public void modificarHecho(Hecho viejo, Hecho nuevo) {
        viejo.setTitulo(nuevo.getTitulo());
        viejo.setDescripcion(nuevo.getDescripcion());
        viejo.setCategoria(nuevo.getCategoria());
        viejo.setContenidosMultimedia(nuevo.getContenidosMultimedia());
        viejo.setLugarAcontecimiento(nuevo.getLugarAcontecimiento());
        viejo.setFechaHecho(nuevo.getFechaHecho());
        viejo.setFechaUltimaActualizacion(LocalDateTime.now());
        viejo.setRevisado(nuevo.isRevisado());
        viejo.setMunicipio(nuevo.getMunicipio());
    }

    //TODO: revisar si es funcional esto asincronico
    @Async
    public void eliminarHechoEnLasFuentes(Hecho hecho){ // Llamada no bloqueante a otra API
        // Para escapar los ":" del id
        String idExternoEscapado = UriUtils.encodePathSegment(hecho.getIdExterno(), StandardCharsets.UTF_8);

        webClients.get(hecho.getOrigen())
                .delete()
                .uri("api/hechos/{id}", idExternoEscapado)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> System.out.println("Eliminación remota exitosa"))
                .doOnError(error -> System.err.println("Error en la eliminación remota"))
                .subscribe();
        
        applicationEventPublisher.publishEvent(new HechoEliminadoEvent(hecho));
    }



    // ---- Conversiones DTO -------------------------------------------------------------------------------

    private GeorrefRequestDTO coordToGeorrefRequestDto(Coordenada c){
    return GeorrefRequestDTO.builder()
            .lat(c.getLatitud())
            .lon(c.getLongitud())
            .aplanar(true)
            .campo("municipio.nombre,provincia.nombre")
            .build();
    }

    public HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setContenidosMultimedia(hecho.getContenidosMultimedia());
        dto.setOrigen(hecho.getOrigen());
        dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setIdExterno(hecho.getIdExterno());
        if (hecho.getContribuyente() != null) dto.setContribuyente(hecho.getContribuyente().getId());

        dto.setEtiquetas(
                hecho.getEtiquetas()
                        .stream()
                        .map(Etiqueta::getNombre)
                        .collect(Collectors.toCollection(HashSet::new))
        );
        return dto;
    }

    private Hecho hechoFromHechoFuenteDTO(HechoFuenteDTO dto) {
        Contribuyente contribuyente = null;
        if (dto.getContribuyente() != null) {
            contribuyente = contribuyenteFromContribuyenteDTO(dto.getContribuyente());
        }

      // Cambiar

      return Hecho.builder()
              .idExterno(dto.getId())
              .titulo(dto.getTitulo())
              .descripcion(dto.getDescripcion())
              .categoria(dto.getCategoria())
              .contenidosMultimedia(dto.getContenidosMultimedia().stream().map(ContenidoMultimedia::new).toList())
              .origen(dto.getOrigen())
              .lugarAcontecimiento(dto.getLugarAcontecimiento())
              .fechaHecho(dto.getFechaHecho())
              .fechaDeCarga(dto.getFechaDeCarga())
              .contribuyente(contribuyente)
              .solicitudesDeEliminacion(dto.getSolicitudesDeEliminacion()) // Cambiar
              .build();
    }

    private Contribuyente contribuyenteFromContribuyenteDTO(ContribuyenteDTO dto) {
        return new Contribuyente(dto.getId(), dto.getNombre(), dto.getApellido(), LocalDate.parse(dto.getFechaDeNacimiento(),DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}

