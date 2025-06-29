package ar.utn.ba.ddsi.models.entities;

import java.util.List;
import java.util.Map;

public class FiltroPorModo extends Filtro {
    public String modo;

    public FiltroPorModo (String modo){
        this.modo = modo;
    }

    public List<Hecho> aplicar(List<Hecho> lista) {
        ModoDeNavegacion modo = ModoDeNavegacion.crear(this.modo);
        return modo.aplicarA(lista);
    }
}
