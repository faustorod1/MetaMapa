package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.models.repositories.IHechosRepository;

import java.util.ArrayList;
import java.util.List;

public class ConsensoAbsoluta implements IAlgoritmoDeConsenso {

    // Para que el hecho sea consensuado, debe estar en TODAS las colecciones
    @Override
    public List<Hecho> consensuar(List<Hecho> hechosColeccion, List<String> fuentesColeccion) {

        List<Hecho> hechosConsensuados = new ArrayList<>();
        int minimoFuentesParaConsenso = fuentesColeccion.size();

        for (Hecho hecho : hechosColeccion) {
            // Contar en cuántas fuentes aparece exactamente igual este hecho
            long cantidadFuentesConEsteHecho = fuentesColeccion.stream()
                    .filter(fuente -> hechosColeccion.stream()
                            .anyMatch(h -> h.hechoIgualA(hecho) && h.perteneceALaFuente(fuente)))
                    .distinct()
                    .count();

            if (cantidadFuentesConEsteHecho == minimoFuentesParaConsenso) {
                hechosConsensuados.add(hecho);
            }
        }   // ACLARACIÓN: el algoritmo no pide verificar contradicciones

        return hechosConsensuados;
    }

}
