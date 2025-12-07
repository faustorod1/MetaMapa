package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity @DiscriminatorValue("fecha_de_carga")
public class FiltroPorFechaDeCarga extends Filtro {

    @Column(name = "desde_fechacarga", columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime desde;
    @Column(name = "hasta_fechacarga", columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime hasta;

    protected FiltroPorFechaDeCarga() {}

    public FiltroPorFechaDeCarga(LocalDateTime desde, LocalDateTime hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    public FiltroPorFechaDeCarga(String desde, String hasta) {
        this.desde = LocalDateTime.parse(desde, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        this.hasta = LocalDateTime.parse(hasta, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public static FiltroPorFechaDeCarga FiltrarDesde(String desde) {
        return FiltrarDesde(LocalDateTime.parse(desde));
    }
    public static FiltroPorFechaDeCarga FiltrarHasta(String hasta) {
        return FiltrarHasta(LocalDateTime.parse(hasta));
    }


    public static FiltroPorFechaDeCarga FiltrarDesde(LocalDateTime desde) {
        return new FiltroPorFechaDeCarga(desde, null);
    }
    public static FiltroPorFechaDeCarga FiltrarHasta(LocalDateTime hasta) {
        return new FiltroPorFechaDeCarga(null, hasta);
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        List<Hecho> filtrados = new ArrayList<>(lista);

        if (desde != null) {
            filtrados = filtrados.stream().filter(h -> h.getFechaDeCarga().isAfter(desde) || h.getFechaDeCarga().isEqual(desde)).toList();
        }
        if (hasta != null) {
            filtrados = filtrados.stream().filter(h -> h.getFechaDeCarga().isBefore(hasta) || h.getFechaDeCarga().isEqual(hasta)).toList();
        }
        return filtrados;
    }
}