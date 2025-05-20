package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.MetaMapa.models.entities.Filtro;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
public class FiltroPorFechaDeCarga extends Filtro {
    private LocalDateTime desde;
    private LocalDateTime hasta;

    public FiltroPorFechaDeCarga(LocalDateTime desde, LocalDateTime hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    public FiltroPorFechaDeCarga(String desde, String hasta) {
        this.desde = LocalDateTime.parse(desde, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.hasta = LocalDateTime.parse(hasta, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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