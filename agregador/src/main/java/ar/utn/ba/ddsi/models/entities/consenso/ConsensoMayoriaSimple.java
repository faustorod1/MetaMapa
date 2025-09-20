package ar.utn.ba.ddsi.models.entities.consenso;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.Fuente;

import java.util.ArrayList;
import java.util.List;

public class ConsensoMayoriaSimple extends AlgoritmoDeConsenso {

    @Override
    public List<Hecho> aplicarA(List<Hecho> hechosColeccion, List<Fuente> fuentesColeccion) {

        List<Hecho> hechosConsensuados = new ArrayList<>();
        int minimoFuentesParaConsenso = (int) Math.ceil(fuentesColeccion.size() / 2.0);

        for (Hecho hecho : hechosColeccion) {
            // Contar en cuántas fuentes aparece exactamente igual este hecho
            long cantidadFuentesConEsteHecho = fuentesColeccion.stream()
                    .filter(fuente -> hechosColeccion.stream()
                            .anyMatch(h -> h.hechoIgualA(hecho) && h.perteneceALaFuente(fuente)))
                    .distinct()
                    .count();

            if (cantidadFuentesConEsteHecho >= minimoFuentesParaConsenso) {
                hechosConsensuados.add(hecho);
            }
        }
        return hechosConsensuados;
    }
}

