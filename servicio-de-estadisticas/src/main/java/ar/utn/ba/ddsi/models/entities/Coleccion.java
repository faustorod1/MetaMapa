package ar.utn.ba.ddsi.models.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Coleccion {
    private String identificador;
    private String titulo;
    private String descripcion;
    private List<Hecho> hechos;
}