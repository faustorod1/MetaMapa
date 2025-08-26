package ar.utn.ba.ddsi.models.entities;

import java.util.ArrayList;
import java.util.List;


public class ConsensoMultiplesMenciones extends AlgoritmoDeConsenso {

    @Override
    public List<Hecho> aplicarA(List<Hecho> hechosColeccion, List<String> fuentesColeccion) {

        List<Hecho> hechosConsensuados = new ArrayList<>();
        for (Hecho hecho : hechosColeccion) {

            // 1. Contar en cuántas fuentes aparece exactamente igual
            long cantidadFuentesConIgualHecho = fuentesColeccion.stream()
                    .filter(fuente -> hechosColeccion.stream()
                            .anyMatch(h -> h.hechoIgualA(hecho) && h.perteneceALaFuente(fuente)))
                    .distinct()
                    .count();

            if (cantidadFuentesConIgualHecho >= 2) {
                hechosConsensuados.add(hecho); // el hecho se menciona en menos de 2 fuentes --> ya no nos interesa --> analizamos el siguiente hecho
            }
        }
        return hechosConsensuados;
    }
}





