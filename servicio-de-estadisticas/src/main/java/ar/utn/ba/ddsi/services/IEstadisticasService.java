package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.entities.Categoria;

import java.time.LocalTime;

public interface IEstadisticasService {
    String provinciaConMasHechosDeColeccion(String coleccion_id);
    String categoriaConMasHechos();
    String provinciaConMasHechosDeCategoria(String categoria);
    LocalTime horarioConMasHechosDeCiertaCategoria(Categoria categoria);
    Long solicitudesSpam();
}
