package ar.utn.ba.ddsi.models.exceptions;

public class NoHaySolicitudPendienteException extends RuntimeException {
    public NoHaySolicitudPendienteException(Long hechoId) {
        super("No hay una solicitud de modificación pendiente para el hecho de id: " + hechoId);
    }
}
