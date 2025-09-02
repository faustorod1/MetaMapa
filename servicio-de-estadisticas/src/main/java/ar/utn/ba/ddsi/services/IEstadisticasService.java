package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.entities.Categoria;

import java.time.LocalTime;


public interface IEstadisticasService {
    String provinciaConMasHechosDeColeccion(String coleccion_id);
    String categoriaConMasHechos();
    String provinciaConMasHechosDeCategoria(String categoria);
    LocalTime horarioConMasHechosDeCiertaCategoria(String categoria);
    Long solicitudesSpam();
    String provinciaConMasHechosDeColeccionCSV(String coleccion_id);
    String categoriaConMasHechosCSV();
    String provinciaConMasHechosDeCategoriaCSV(String categoria);
    String horarioConMasHechosPorCategoriaCSV(String categoria);
    String solicitudesSpamCSV();
}
