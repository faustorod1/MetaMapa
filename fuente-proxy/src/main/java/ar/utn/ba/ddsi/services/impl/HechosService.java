package ar.utn.ba.ddsi.services.impl;


import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IAPIsRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

@PropertySource("classpath:keys.properties")
@Service
public class HechosService implements IHechosService {
  private IAPIsRepository apisRepository;
  private IHechosRepository hechosRepository;
  private LocalDateTime lastCached;

  @Override
  public List<HechoOutputDTO> getAll(){
    if (ChronoUnit.HOURS.between(lastCached, LocalDateTime.now()) >= 12 || lastCached == null) {
      List<API> apis = apisRepository.findAll();
      List<Hecho> hechos = apis.stream().flatMap(api -> api.getAll().stream()).toList();

      hechosRepository.saveAll(hechos); // Actualizamos Caché

      return hechos.stream().map(this::hechoToOutputDTO).toList();
    }
    else {
      return hechosRepository.findAll().stream().map(this::hechoToOutputDTO).toList();
    }
  }

  @Override
  public List<HechoOutputDTO> getAllDesde(LocalDateTime desde){
    List<Hecho> hechos = apisRepository.findAll().stream().flatMap(api -> api.getAllDesde(desde).stream()).toList();

    hechosRepository.saveAll(hechos);  // Actualizamos Caché

    return hechos.stream().map(this::hechoToOutputDTO).toList();
  }

  @Override
  public List<HechoOutputDTO> getAllFromAPI(Long APIid){
    return apisRepository.findByAPIid(APIid).getAll().stream().map(this::hechoToOutputDTO).toList();
  }

  @Override
  public List<HechoOutputDTO> getAllDesdeFromAPI(Long APIid,LocalDateTime desde){
    return apisRepository.findByAPIid(APIid).getAllDesde(desde).stream().map(this::hechoToOutputDTO).toList();
  }

  //----------------------------------------------------------------CONSUMIR METAMAPA----------------------------------------------------------//

  @Override
  public List<HechoOutputDTO> getAllFromMetamapa (){
    Stream<Hecho> hechos = apisRepository.findAllMetamapa().stream().flatMap(api -> api.getAll().stream());
    return hechos.map(this::hechoToOutputDTO).toList();
  }

  // tal vez getAllFromMetamapaDesde

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