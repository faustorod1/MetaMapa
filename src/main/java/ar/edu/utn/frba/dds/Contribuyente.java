package ar.edu.utn.frba.dds;

public class Contribuyente{
  //Como persona contribuyente, deseo poder solicitar la eliminación de un hecho.

  void solicitarEliminacionDeHecho(Hecho hecho, String descripcion) {
    if(descripcion.length() >= 500) {
      hecho.aniadirSolicitud(new SolicitudDeEliminacion(hecho,descripcion));
    }else{
      System.out.println("La descripcion no puede tener menos de 500 carácteres");
    }
  }

}
