package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;

@Getter
public class Contribuyente  {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    public static final Contribuyente ANONIMO = new Contribuyente();

    private Contribuyente() {
        this.nombre = null;
        this.apellido = null;
        this.fechaNacimiento = null;
    }

    public Contribuyente(String nombre, String apellido, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }


}