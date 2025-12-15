package ar.utn.ba.ddsi.services.impl;


import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IAPIsRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HechosService implements IHechosService {

  @Autowired
  private IAPIsRepository apisRepository;
  @Autowired
  private IHechosRepository hechosRepository;
  private LocalDateTime lastCachedAPI;
  private LocalDateTime lastCachedMetamapa;

  private final ExecutorService apiExecutor = Executors.newFixedThreadPool(10);


  @Override
  public Page<HechoOutputDTO> getAll(Pageable pageable) {
    Page<Hecho> pagina = hechosRepository.findAll(pageable);
    List<Hecho> hechos = pagina.getContent();
    List<HechoOutputDTO> dtos = hechos.stream().map(HechoOutputDTO::fromEntity).toList();
    return new PageImpl<>(dtos, pageable, pagina.getTotalElements());
  }

  @Override
  public Page<HechoOutputDTO> getAllDesde(LocalDateTime desde, Pageable pageable){
    Page<Hecho> pagina = hechosRepository.findByFechaObtencionAfter(desde, pageable);
    List<Hecho> hechos = pagina.getContent();
    List<HechoOutputDTO> dtos = hechos.stream().map(HechoOutputDTO::fromEntity).toList();
    return new PageImpl<>(dtos, pageable, pagina.getTotalElements());
  }

  //----------------------------------------------------------------CONSUMIR METAMAPA----------------------------------------------------------//

  @Override
  public List<HechoOutputDTO> getAllFromMetamapa (){
    List<API> apisMetamapa = apisRepository.findAllMetamapa();
    List<Long> apisIds = apisMetamapa.stream().map(API::getId).toList();
    return hechosRepository.findByAPIidIn(apisIds).stream().map(HechoOutputDTO::fromEntity).toList();
  }

  public List<HechoOutputDTO> getAllFromMetamapaDesde(LocalDateTime desde){
    List<API> apisMetamapa = apisRepository.findAllMetamapa();
    List<Long> apisIds = apisMetamapa.stream().map(API::getId).toList();
    return hechosRepository.findByAPIidInAndFechaObtencionAfter(apisIds, desde)
            .stream().map(HechoOutputDTO::fromEntity).toList();
  }

  @Override
  @Transactional
  public void actualizarHechos() {
    List<API> apis = apisRepository.findAll();
    List<CompletableFuture<Void>> futuros = new ArrayList<>();

    for (API api : apis) {
      CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        try {
          System.out.println("Leyendo datos de la api: " + api.getId());

          List<Hecho> nuevos = api.getNuevos();
          guardarLoteHechos(nuevos, api);
          api.setFechaUltimaActualizacion(LocalDateTime.now());

        } catch (Exception e) {
          System.err.println("Error en adapter " + api.getId() + ": " + e.getMessage());
        }
      }, apiExecutor);
      futuros.add(future);
    }
    CompletableFuture.allOf(futuros.toArray(new CompletableFuture[0])).join();
    System.out.println("Sincronización con APIs terminada");
  }

  @Transactional
  public void guardarLoteHechos(List<Hecho> nuevos, API api) {
    List<String> idsExternos = nuevos.stream()
            .map(Hecho::getIdExterno)
            .toList();

    List<Hecho> existentes = hechosRepository.findByAPIidAndIdExternoInWithCollections(api.getId(), idsExternos);

    Map<String, Hecho> mapaExistentes = existentes.stream()
            .collect(Collectors.toMap(Hecho::getIdExterno, h -> h));

    List<Hecho> aGuardar = new ArrayList<>();
    for (Hecho nuevo : nuevos) {
      Hecho existente = mapaExistentes.get(nuevo.getIdExterno());
      if (existente == null) { // INSERT
        aGuardar.add(nuevo);
        nuevo.setFechaObtencion(LocalDateTime.now());
      } else { // UPDATE
        actualizarHecho(existente, nuevo);
        aGuardar.add(existente);
        existente.setFechaObtencion(LocalDateTime.now());
      }
    }

    if (!aGuardar.isEmpty()) {
      hechosRepository.saveAll(aGuardar);
    }
  }

  private void actualizarHecho(Hecho hechoViejo, Hecho hechoNuevo) {
    hechoViejo.setTitulo(hechoNuevo.getTitulo());
    hechoViejo.setDescripcion(hechoNuevo.getDescripcion());
    hechoViejo.setCategoria(hechoNuevo.getCategoria());
    hechoViejo.setFechaHecho(hechoNuevo.getFechaHecho());
    hechoViejo.setEliminado(hechoNuevo.isEliminado());
    hechoViejo.setLugarAcontecimiento(hechoNuevo.getLugarAcontecimiento());
    if (hechoNuevo.getFechaUltimaActualizacion() != null) {
      hechoViejo.setFechaUltimaActualizacion(hechoNuevo.getFechaUltimaActualizacion());
    } else {
      hechoViejo.setFechaUltimaActualizacion(LocalDateTime.now());
    }

    if (hechoViejo.getEtiquetas() == null) {
      hechoViejo.setEtiquetas(new HashSet<>());
    }
    hechoViejo.getEtiquetas().clear();
    if (hechoNuevo.getEtiquetas() != null) {
      hechoViejo.getEtiquetas().addAll(hechoNuevo.getEtiquetas());
    }

    if (hechoViejo.getContenidoMultimedia() == null) {
      hechoViejo.setContenidoMultimedia(new ArrayList<>());
    }

    hechoViejo.getContenidoMultimedia().clear();

    if (hechoNuevo.getContenidoMultimedia() != null) {
      hechoViejo.getContenidoMultimedia().addAll(hechoNuevo.getContenidoMultimedia());
    }
  }
}