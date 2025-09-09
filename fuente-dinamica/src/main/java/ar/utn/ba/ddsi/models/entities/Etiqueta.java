package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "etiquetas")
@Data
public class Etiqueta{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(50)", nullable = false)
    private String nombre;

    protected Etiqueta() {}

   public Etiqueta(String nombre) {
       this.nombre = nombre;
   }

}