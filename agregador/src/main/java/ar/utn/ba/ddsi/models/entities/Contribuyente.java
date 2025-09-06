package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;

@Getter

@Entity
@Table(name = "contribuyentes")
public class Contribuyente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", columnDefinition = "VARCHAR(30)", nullable = false)
    private String nombre;
    @Column(name = "apellido", columnDefinition = "VARCHAR(50)", nullable = false)
    private String apellido;
    @Column(name = "fecha_de_nacimiento", columnDefinition = "DATE", nullable = false)
    private LocalDate fechaNacimiento;

    public static final Contribuyente ANONIMO = new Contribuyente(0L);

    protected Contribuyente() {}

    private Contribuyente(Long id) {
        this.id = id;
        this.nombre = null;
        this.apellido = null;
        this.fechaNacimiento = null;
    }

    public Contribuyente(Long id, String nombre, String apellido, LocalDate fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }


}