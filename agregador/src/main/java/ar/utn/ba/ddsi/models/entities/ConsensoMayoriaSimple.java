package ar.utn.ba.ddsi.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsensoMayoriaSimple implements IAlgoritmoDeConsenso {

    // Para que un hecho este consensuado, debe estar en, mínimo, la mitad de las fuentes
    @Override
    public List<Hecho> consensuar(List<Hecho> hechosColeccion, List<String> fuentesColeccion) {

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
        }           // ACLARACIÓN: el algoritmo no pide verificar contradicciones

        return hechosConsensuados;
    }
}