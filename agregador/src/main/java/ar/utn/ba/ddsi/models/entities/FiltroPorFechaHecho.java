package ar.utn.ba.ddsi.models.entities;

import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
public class FiltroPorFechaHecho extends Filtro {

    private LocalDate desde;
    private LocalDate hasta;

    public FiltroPorFechaHecho(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    public FiltroPorFechaHecho(String desde, String hasta) {
        if (desde != null) {
            this.desde = LocalDate.parse(desde, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        else {
            this.desde = null;
        }
        if (hasta != null) {
            this.hasta = LocalDate.parse(hasta, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        else {
            this.hasta = null;
        }
    }

    public static FiltroPorFechaHecho FiltrarDesde(LocalDate desde) {
        return new FiltroPorFechaHecho(desde, null);
    }
    public static FiltroPorFechaHecho FiltrarHasta(LocalDate hasta) {
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