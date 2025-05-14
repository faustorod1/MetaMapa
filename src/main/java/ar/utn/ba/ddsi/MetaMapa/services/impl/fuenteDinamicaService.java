package ar.utn.ba.ddsi.MetaMapa.services.impl;

import ar.utn.ba.ddsi.MetaMapa.models.entities.Hecho;
import ar.utn.ba.ddsi.MetaMapa.models.repositories.iHechosRepository;
import ar.utn.ba.ddsi.MetaMapa.services.iFuenteDinamicaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class fuenteDinamicaService implements iFuenteDinamicaService {
  private final iHechosRepository hechosRepository;

  public fuenteDinamicaService(iHechosRepository hechosRepository) {
    this.hechosRepository = hechosRepository;
  }

  @Override
  public void crearHecho(Hecho hecho) {
    hechosRepository.save(hecho);
  }

  @Override
  public void modificarHecho(Hecho hechoAModificar,Hecho hechoNuevo){ //Todo ver como llega la request de modificar, si con el hecho nuevo y el id viejo o con dos hechos distintos
    if(ChronoUnit.DAYS.between(hechoNuevo.getFechaDeCarga(), LocalDateTime.now()) > 7){
      this.hechosRepository.save(hechoNuevo);
    }
    //aqui se podria arrojar una excepcion si el usuario intenta modificar luego de los 7 dias.
  }

  @Override
  public List<Hecho> obtenerHechos(Boolean pendiente) { // true si quiero q me de los pendientes
    return hechosRepository.findByPendiente(pendiente);
  }

  @Override
  public void procesarPendientes() {
    //Todo implementar logica de acceptar y rechazar pendientes
  }
}
