package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class Administrador {

  public Coleccion crearColeccion(String titulo, String descripcion, Fuente fuente) {

    return new Coleccion(titulo, descripcion, fuente);
  }

  public void juzgarEliminacionDeHecho(Hecho hecho){
    HashSet<SolicitudDeEliminacion> solicitudes = hecho.getSolicitudesDeEliminacion();
    solicitudes.forEach(solicitud -> {solicitud.preguntar()});
  }
  // TODO: aceptar o rechazar solicitud
  //deberiamos tener una lista de colecciones? no c


}
