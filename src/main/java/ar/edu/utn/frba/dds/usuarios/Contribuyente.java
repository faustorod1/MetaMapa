package ar.edu.utn.frba.dds.usuarios;

public class Contribuyente  {
    private String nombre;
    private String apellido;
    private int edad;

    public static final Contribuyente ANONIMO = new Contribuyente();

    private Contribuyente() {
        this.nombre = null;
        this.apellido = null;
        this.edad = 0;
    }

    public Contribuyente(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }
}

