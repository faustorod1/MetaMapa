package ar.utn.ba.ddsi.models.exceptions;

public class SolicitudFueraDePlazoException extends RuntimeException {
  public SolicitudFueraDePlazoException(Long hechoId) {
    super("No se puede crear una solicitud de modificación para el hecho " + hechoId + " porque han pasado más de 7 días desde su carga.");
  }
}
