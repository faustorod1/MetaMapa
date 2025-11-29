package ar.utn.ba.ddsi.exceptions;

public class UsuarioExistenteException extends RuntimeException {
  public UsuarioExistenteException(String email) {
    super("Ya existe un usuario con el email: " + email);
  }
}
