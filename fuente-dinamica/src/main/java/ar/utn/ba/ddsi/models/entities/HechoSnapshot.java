package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HechoSnapshot {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaSnapshot;

    public HechoSnapshot(Hecho hecho) {
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        this.categoria = hecho.getCategoria();
        this.contenidoMultimedia = hecho.getContenidoMultimedia();
        this.lugarAcontecimiento = hecho.getLugarAcontecimiento();
        this.fechaHecho = hecho.getFechaHecho();
        this.fechaSnapshot = LocalDateTime.now();
    }
}