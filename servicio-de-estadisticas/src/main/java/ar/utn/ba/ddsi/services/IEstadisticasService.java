package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.entities.Categoria;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;


public interface IEstadisticasService {
    void generarEstadisticas();
    String getCSVPath(String filename);
}
