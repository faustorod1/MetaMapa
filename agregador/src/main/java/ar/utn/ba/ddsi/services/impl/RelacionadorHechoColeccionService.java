package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.CriterioCambiadoEvent;
import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.HechoEliminadoEvent;
import ar.utn.ba.ddsi.models.entities.FuentesCambiadasEnColeccionEvent;
import ar.utn.ba.ddsi.models.entities.HechosModificadosEvent;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IRelacionadorHechoColeccionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//Patrón Observer
@Service
public class RelacionadorHechoColeccionService implements IRelacionadorHechoColeccionService {
  private final IHechosRepository hechosRepository;
  private final IColeccionesRepository coleccionesRepository;

  @Autowired
  public RelacionadorHechoColeccionService(IHechosRepository hechosRepository, IColeccionesRepository coleccionesRepository) {
    this.hechosRepository = hechosRepository;
    this.coleccionesRepository = coleccionesRepository;
  }

  // Hecho nuevo o modificado
  @EventListener
  @Transactional
  public void alModificarHecho(HechosModificadosEvent evento) {
    List<Hecho> hechos = evento.getHechos();
    List<Coleccion> colecciones = coleccionesRepository.findAll();
    for (Coleccion c : colecciones) {
      c.agregarTandaDeHechos(hechos);
    }

    coleccionesRepository.saveAll(colecciones);
  }

  @EventListener
  public void alEliminarHecho(HechoEliminadoEvent evento) {
    List<Coleccion> colecciones = coleccionesRepository.findAll();
    for (Coleccion c : colecciones) {
      c.removerHechoEliminado(evento.getHechoEliminado());
    }

    coleccionesRepository.saveAll(colecciones);
  }

  // Revisar. Llamado en: creacion de colección,
  @EventListener
  public void alCambiarCriterioDeColeccion(CriterioCambiadoEvent evento) {
    Coleccion coleccion = evento.getColeccion();
    List<Hecho> hechosDeFuentes = hechosRepository.findAllByIdExterno_FuenteIn(coleccion.getFuentes());
    coleccion.agregarTandaDeHechos(hechosDeFuentes);
    coleccionesRepository.save(coleccion);

  }

  @EventListener
  public void alCambiarFuenteDeColeccion(FuentesCambiadasEnColeccionEvent evento) {
    Coleccion coleccion = evento.getColeccion();
    List<Fuente> fuentesCambiadas = evento.getFuentesCambiadas();
    List<Hecho> hechosDeFuentes = hechosRepository.findAllByIdExterno_FuenteIn(fuentesCambiadas);
    coleccion.agregarTandaDeHechos(hechosDeFuentes);

    coleccionesRepository.save(coleccion);
  }


}