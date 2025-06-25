package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.CriterioCambiadoEvent;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.HechoEliminadoEvent;
import ar.utn.ba.ddsi.models.entities.HechosModificadosEvent;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IRelacionadorHechoColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
      c.removerHecho(evento.getHechoEliminado());
    }
  }

  // Revisar. Parece andar para Nueva, modificados criterio y fuente
  @EventListener
  public void alCambiarCriterioDeColeccion(CriterioCambiadoEvent evento) {
    Coleccion coleccion = evento.getColeccion();
    List<Hecho> hechosDeFuentes = hechosRepository.findFromFuentes(coleccion.getFuentes());
    coleccion.agregarTandaDeHechos(hechosDeFuentes);
  }

  /*
  Coleccion
    Nueva  -- Comparar contra todos los de ciertas fuentes y manuales
    Modificada
      Criterio -- Comparar contra todos los de ciertas fuentes y manuales
      Fuente (agregar/quitar) -- Comparar todos los de la fuente / Eliminar todos los de la fuente excepto los manuales
      Hecho manual (agregar/quitar) -- Sacar si no cumple / Agregar
  Hecho
    Nuevo         --- listo
    Modificación  --- listo
    Eliminarlo    --- listo
  */

}