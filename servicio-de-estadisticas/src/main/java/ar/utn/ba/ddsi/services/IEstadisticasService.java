package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.entities.Categoria;

import java.time.LocalTime;

public interface IEstadisticasService {

    public String categoriaConMasHechos();
    public LocalTime horarioConMasHechosPorCategoria(Categoria categoria);
}
