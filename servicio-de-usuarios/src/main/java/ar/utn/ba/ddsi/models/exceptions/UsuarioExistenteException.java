package ar.utn.ba.ddsi.models.exceptions;

public class UsuarioExistenteException extends RuntimeException {
  public UsuarioExistenteException(String email) {
    super("Ya existe un usuario con el email: " + email);
  }
}
