package ar.utn.ba.ddsi.models.entities;

import java.util.ArrayList;
import java.util.List;


public class ConsensoMultiplesMenciones implements IAlgoritmoDeConsenso {

    @Override
    public List<Hecho> consensuar(List<Hecho> hechosColeccion, List<String> fuentesColeccion) {

        List<Hecho> hechosConsensuados = new ArrayList<>();

        for (Hecho hecho : hechosColeccion) {

            // 1. Contar en cuántas fuentes aparece exactamente igual
            long cantidadFuentesConIgualHecho = fuentesColeccion.stream()
                    .filter(fuente -> hechosColeccion.stream()
                            .anyMatch(h -> h.hechoIgualA(hecho) && h.perteneceALaFuente(fuente)))
                    .distinct()
                    .count();

            if (cantidadFuentesConIgualHecho < 2) {
                continue; // el hecho se menciona en menos de 2 fuentes --> ya no nos interesa --> analizamos el siguiente hecho
            }

            // 2. Verificar que en ninguna otra fuente haya un hecho de igual titulo y diferentes atributos
            boolean hayContradiccion = fuentesColeccion.stream()
                    .anyMatch(fuente -> hechosColeccion.stream()
                            .anyMatch(h -> h.mismoTituloDiferentesAtributos(hecho)
                                    && h.perteneceALaFuente(fuente)));

            if (!hayContradiccion) {
                hechosConsensuados.add(hecho); // Es consensuado
            }
        }

        List<Hecho> sinDuplicados = new ArrayList<>();

        for (Hecho hecho : hechosConsensuados) {
            boolean yaExiste = sinDuplicados.stream()
                    .anyMatch(h -> h.hechoIgualA(hecho));
            if (!yaExiste) {
                sinDuplicados.add(hecho);
            }
        }

        return sinDuplicados;            // Nos aseguramos de no devolver dos hechos "iguales"
    }

}

