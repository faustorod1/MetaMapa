package ar.utn.ba.ddsi.models.entities.consenso;

import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.ArrayList;
import java.util.List;

public abstract class AlgoritmoDeConsenso {

    public List<Hecho> consensuar(List<Hecho> hechos, List<String> fuentes){

        List<Hecho> hechosAManejar = aplicarA(hechos, fuentes);
        hechosAManejar = sinContradiccion(hechosAManejar,fuentes);
        return sinDuplicados(hechosAManejar);

    }

    public abstract List<Hecho> aplicarA(List<Hecho> hechos, List<String> fuentes);

    // 2. Verificar que en ninguna otra fuente haya un hecho de igual titulo y diferentes atributos
    public List<Hecho> sinContradiccion(List<Hecho> hechos, List<String> fuentes){
        List<Hecho> hechosAManejar = new ArrayList<>();

        for(Hecho hecho : hechos) {
            boolean hayContradiccion = fuentes.stream()
                    .anyMatch(fuente -> hechos.stream()
                            .anyMatch(h -> h.mismoTituloDiferentesAtributos(hecho)
                                    && h.perteneceALaFuente(fuente)));

            if (!hayContradiccion) {
                hechosAManejar.add(hecho); // Es consensuado
            }
        }
        return hechosAManejar;
    }

    public List<Hecho> sinDuplicados(List<Hecho> hechos){
        List<Hecho> sinDuplicados = new ArrayList<>();

        for (Hecho hecho : hechos) {
            boolean yaExiste = sinDuplicados.stream()
                    .anyMatch(h -> h.hechoIgualA(hecho));
            if (!yaExiste) {
                sinDuplicados.add(hecho);
            }
        }
        return sinDuplicados;                // Nos aseguramos de no devolver dos hechos "iguales"
    }
}