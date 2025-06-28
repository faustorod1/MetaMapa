package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.CriterioCambiadoEvent;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.HechoEliminadoEvent;
import ar.utn.ba.ddsi.models.entities.FuentesCambiadasEnColeccionEvent;
import ar.utn.ba.ddsi.models.entities.HechosModificadosEvent;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IRelacionadorHechoColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

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
  public void alModificarHecho(HechosModificadosEvent evento) {
    List<Hecho> hechos = evento.getHechos();
    List<Coleccion> colecciones = coleccionesRepository.findAll();
    for (Coleccion c : colecciones) {
      c.agregarTandaDeHechos(hechos);
    }
  }

  @EventListener
  public void alEliminarHecho(HechoEliminadoEvent evento) {
    List<Coleccion> colecciones = coleccionesRepository.findAll();
    for (Coleccion c : colecciones) {
      c.removerHechoEliminado(evento.getHechoEliminado());
    }
  }

  // Revisar. Llamado en: creacion de colección,
  @EventListener
  public void alCambiarCriterioDeColeccion(CriterioCambiadoEvent evento) {
    Coleccion coleccion = evento.getColeccion();
    List<Hecho> hechosDeFuentes = hechosRepository.findFromFuentes(coleccion.getFuentes());
    coleccion.agregarTandaDeHechos(hechosDeFuentes);
  }
/*
  [a, b,c ]   // FUENTES ANTES
  [a, d, e]   // FUENTES AHORA

  [b, c, d, e]    // FUENTES QUE ENTRAN O QUE SALEN
  */

  @EventListener
  public void alCambiarFuenteDeColeccion(FuentesCambiadasEnColeccionEvent evento) {
    Coleccion coleccion = evento.getColeccion();
    List<String> fuentesCambiadas = evento.getFuentesCambiadas();
    List<Hecho> hechosDeFuentes = hechosRepository.findFromFuentes(fuentesCambiadas);
    coleccion.agregarTandaDeHechos(hechosDeFuentes);
  }


  /*
  Coleccion
    Nueva  -- Comparar contra todos los de ciertas fuentes y manuales
    Modificada
      Criterio -- Comparar contra todos los de ciertas fuentes y manuales
      Fuente (agregar/quitar) -- Comparar todos los de la fuente / Eliminar todos los de la fuente excepto los manuales
      Hecho manual (agregar/quitar) -- Sacar si no cumple / Agregar
  */

}