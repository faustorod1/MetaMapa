package ar.utn.ba.ddsi.models.entities;


import java.util.ArrayList;
import java.util.List;

public class ConsensoAbsoluta extends AlgoritmoDeConsenso {

    @Override
    public List<Hecho> aplicarA(List<Hecho> hechosColeccion, List<String> fuentesColeccion) {

        List<Hecho> hechosConsensuados = new ArrayList<>();

        for (Hecho hecho : hechosColeccion) {
            boolean estaEnTodas = fuentesColeccion.stream().allMatch(fuente ->
                    hechosColeccion.stream().filter(h -> h.perteneceALaFuente(fuente))
                            .anyMatch(hechoDeFuente -> hechoDeFuente.hechoIgualA(hecho)));

            if (estaEnTodas) {
                hechosConsensuados.add(hecho);   // el hecho no se menciona en todas las fuentes --> ya no nos interesa --> analizamos el siguiente hecho
            }

        }
        return hechosConsensuados;
    }
}
