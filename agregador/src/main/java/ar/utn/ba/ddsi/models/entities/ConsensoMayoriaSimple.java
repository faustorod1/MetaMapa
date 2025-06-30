package ar.utn.ba.ddsi.models.entities;

import java.util.ArrayList;
import java.util.List;

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

            if (cantidadFuentesConEsteHecho < minimoFuentesParaConsenso) {
                continue;   // el hecho no se menciona en la mayoria de las fuentes --> ya no nos interesa --> analizamos el siguiente hecho
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

        return sinDuplicados;                // Nos aseguramos de no devolver dos hechos "iguales"
    }
}
