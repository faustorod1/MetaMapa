package ar.utn.ba.ddsi.models.entities;

import java.util.List;

public class ModoNavegacionCurada extends ModoDeNavegacion{

    public List<Hecho> aplicarA(List<Hecho> lista){

        return lista;
    }

}
