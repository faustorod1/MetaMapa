package ar.utn.ba.ddsi.models.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(Long idUsuario) {
        super("El usuario " + idUsuario + " no está autorizado para esta acción.");
    }
}
