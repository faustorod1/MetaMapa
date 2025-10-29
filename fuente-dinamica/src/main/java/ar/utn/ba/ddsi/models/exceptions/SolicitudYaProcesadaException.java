package ar.utn.ba.ddsi.models.exceptions;

public class SolicitudYaProcesadaException extends RuntimeException {
    public SolicitudYaProcesadaException(Long solicitudId) {
        super("La solicitud " + solicitudId + " ya fue procesada previamente");
    }
}
