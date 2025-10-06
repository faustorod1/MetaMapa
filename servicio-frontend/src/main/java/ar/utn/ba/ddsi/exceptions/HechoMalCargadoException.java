package ar.utn.ba.ddsi.exceptions;

public class HechoMalCargadoException extends RuntimeException {
    public HechoMalCargadoException() {
        super("Alguno de tus campos no es válido");
    }
}
