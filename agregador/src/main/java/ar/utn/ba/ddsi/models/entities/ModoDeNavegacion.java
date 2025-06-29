package ar.utn.ba.ddsi.models.entities;

import java.util.List;

public abstract class ModoDeNavegacion {
    abstract List<Hecho> aplicarA(List<Hecho> lista);

    public static ModoDeNavegacion crear(String modo) {
        switch (modo.toLowerCase()) {
            case "curado":
                return new ModoNavegacionCurada();
            case "irrestricto":
                return new ModoNavegacionIrrestricta();
            default:
                throw new IllegalArgumentException("No existe el modo : " + modo);
        }
    }
}
