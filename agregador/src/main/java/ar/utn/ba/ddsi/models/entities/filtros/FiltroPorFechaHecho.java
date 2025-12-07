package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity @DiscriminatorValue("fecha_hecho")
public class FiltroPorFechaHecho extends Filtro {

    @Column(name = "desde_fechahecho", columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime desde;

    @Column(name = "hasta_fechahecho", columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime hasta;

    public FiltroPorFechaHecho() {}

    public FiltroPorFechaHecho(LocalDateTime desde, LocalDateTime hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    public FiltroPorFechaHecho(String desde, String hasta) {
        if (desde != null) {
            this.desde = LocalDateTime.parse(desde, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        }
        else {
            this.desde = null;
        }
        if (hasta != null) {
            this.hasta = LocalDateTime.parse(hasta, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        }
        else {
            this.hasta = null;
        }
    }

    public static FiltroPorFechaHecho FiltrarDesde(String desde) {
        return FiltrarDesde(LocalDateTime.parse(desde));
    }
    public static FiltroPorFechaHecho FiltrarHasta(String hasta) {
        return FiltrarHasta(LocalDateTime.parse(hasta));
    }

    public static FiltroPorFechaHecho FiltrarDesde(LocalDateTime desde) {
        return new FiltroPorFechaHecho(desde, null);
    }
    public static FiltroPorFechaHecho FiltrarHasta(LocalDateTime hasta) {
        return new FiltroPorFechaHecho(null, hasta);
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        List<Hecho> filtrados = new ArrayList<>(lista);

        if (desde != null) {
            filtrados = filtrados.stream().filter(h -> h.getFechaHecho().isAfter(desde) || h.getFechaHecho().isEqual(desde)).toList();
        }
        if (hasta != null) {
            filtrados = filtrados.stream().filter(h -> h.getFechaHecho().isBefore(hasta) || h.getFechaHecho().isEqual(hasta)).toList();
        }
        return filtrados;
    }
}