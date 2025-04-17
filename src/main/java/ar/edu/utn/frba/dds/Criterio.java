package ar.edu.utn.frba.dds;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Criterio {

  private ArrayList<Predicate<Hecho>> filtros;

    //todo convertir un input en predicate
    //ejemplo:
    // Input: titulo = "string" ,
    // Predicate: x -> x.titulo == titulo



  public List<Hecho> aplicarA(List<Hecho> listaOriginal){
    Predicate<Hecho> filtrosUnificados = filtros.stream().reduce(x -> true, Predicate::and);
    return listaOriginal.stream().filter(filtrosUnificados).collect(Collectors.toList());
  }



  /*//Pense en Patron State. Hacer una Clase q herede para cada criterio deseado y definirle la implemetacion a "pertenece"
  public boolean pertenece(Hecho hecho, Coleccion coleccion);*/

}

//No usar esto, usar Predicate y q sea atributo

//Predicate<Hecho> criterio = hecho -> hecho.getCategoria().equals("Incendio forestal");