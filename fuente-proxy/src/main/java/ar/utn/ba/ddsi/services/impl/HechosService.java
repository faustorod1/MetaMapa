package ar.utn.ba.ddsi.services.impl;


import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IAPIsRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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


  @Override
  public List<HechoOutputDTO> getAll() {
    List<HechoOutputDTO> hechosAPI = this.getAllAPI();
    List<HechoOutputDTO> hechosMeta = this.getAllFromMetamapa();
    return Stream.concat(hechosAPI.stream(), hechosMeta.stream()).collect(Collectors.toList());
  }

  @Override
  public List<HechoOutputDTO> getAllDesde(LocalDateTime desde){
    List<HechoOutputDTO> hechosAPI = this.getAllAPIDesde(desde);
    List<HechoOutputDTO> hechosMeta = this.getAllFromMetamapaDesde(desde);
    return Stream.concat(hechosAPI.stream(), hechosMeta.stream()).collect(Collectors.toList());
  }

  //----------------------------------------------------------------CONSUMIR APIEXT-------------------------------------------------------------//
  @Override
  public List<HechoOutputDTO> getAllAPI(){
    if (lastCachedAPI == null || ChronoUnit.HOURS.between(lastCachedAPI, LocalDateTime.now()) >= 12) {
      List<Hecho> hechos = apisRepository.findAllAPI().stream().flatMap(api -> api.getAll().stream()).toList();

      hechosRepository.APIsaveAll(hechos); // Actualizamos Caché
      lastCachedAPI = LocalDateTime.now();

      return hechos.stream().map(this::hechoToOutputDTO).toList();
    }
    else {
      return hechosRepository.findAllAPI().stream().map(this::hechoToOutputDTO).toList();
    }
  }

  @Override
  public List<HechoOutputDTO> getAllAPIDesde(LocalDateTime desde) {
    if (lastCachedAPI == null || ChronoUnit.HOURS.between(lastCachedAPI, LocalDateTime.now()) >= 12) {
    List<Hecho> hechos = apisRepository.findAllAPI().stream().flatMap(api -> api.getAllDesde(desde).stream()).toList();

    hechosRepository.APIsaveAll(hechos);  // Actualizamos Caché
    lastCachedAPI = LocalDateTime.now();

    return hechos.stream().map(this::hechoToOutputDTO).toList();
    }
    else {
      return hechosRepository.findAllAfterAPI(desde).stream().map(this::hechoToOutputDTO).toList();
    }
  }

  //----------------------------------------------------------------CONSUMIR METAMAPA----------------------------------------------------------//

  @Override
  public List<HechoOutputDTO> getAllFromMetamapa (){
    if (lastCachedMetamapa == null || ChronoUnit.HOURS.between(lastCachedMetamapa, LocalDateTime.now()) >= 12) {
      List<Hecho> hechos = apisRepository.findAllMetamapa().stream().flatMap(api -> api.getAll().stream()).toList();

      hechosRepository.metaSaveAll(hechos);
      lastCachedMetamapa = LocalDateTime.now();

      return hechos.stream().map(this::hechoToOutputDTO).toList();
    }else{
      return hechosRepository.findAllMetaMapa().stream().map(this::hechoToOutputDTO).toList();
    }
  }

  public List<HechoOutputDTO> getAllFromMetamapaDesde(LocalDateTime desde){
    if (lastCachedMetamapa == null || ChronoUnit.HOURS.between(lastCachedMetamapa, LocalDateTime.now()) >= 12) {
      List<Hecho> hechos = apisRepository.findAllMetamapa().stream().flatMap(api -> api.getAllDesde(desde).stream()).toList();

      hechosRepository.metaSaveAll(hechos);  // Actualizamos Caché
      lastCachedAPI = LocalDateTime.now();

      return hechos.stream().map(this::hechoToOutputDTO).toList();
    }
    else {
      return hechosRepository.findAllAfterMetamapa(desde).stream().map(this::hechoToOutputDTO).toList();
    }
  }

  @Override
  public void marcarComoEliminado(Long id,Long APIid){
    hechosRepository.marcarComoEliminado(id, APIid);
  }

  private HechoOutputDTO hechoToOutputDTO(Hecho hecho) {
    return HechoOutputDTO.builder()
            .id(String.format("proxy:%s:%s", hecho.getAPIid(), hecho.getId())) // Usamos proxy:<id-api>:<id-hecho>
            .titulo(hecho.getTitulo())
            .descripcion(hecho.getDescripcion())
            .categoria((hecho.getCategoria()))
            .origen(OrigenHecho.PROXY)
            .lugarAcontecimiento(hecho.getLugarAcontecimiento())
            .fechaHecho(hecho.getFechaHecho())
            .fechaDeCarga(hecho.getFechaDeCarga())
            .fechaUltimaActualizacion(hecho.getFechaUltimaActualizacion())
            .eliminado(hecho.isEliminado())
            .build();
  }
}