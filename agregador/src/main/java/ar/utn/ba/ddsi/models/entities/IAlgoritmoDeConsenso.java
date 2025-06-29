package ar.utn.ba.ddsi.models.entities;

import java.util.List;

public interface IAlgoritmoDeConsenso {
    List<Hecho> consensuar(List<Hecho> hechos, List<String> fuentes);
}
