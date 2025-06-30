package ar.utn.ba.ddsi.models.entities;

import java.util.List;

public class ModoNavegacionIrrestricta extends ModoDeNavegacion{

    public List<Hecho> aplicarA(List<Hecho> hechos) {
        return hechos;// sin filtros
    }
}
