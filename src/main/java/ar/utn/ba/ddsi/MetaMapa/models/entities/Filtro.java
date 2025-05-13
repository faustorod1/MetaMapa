package ar.utn.ba.ddsi.MetaMapa.models.entities;

import java.util.List;

public abstract class Filtro {

    public abstract List<Hecho> aplicar(List<Hecho> lista);
}