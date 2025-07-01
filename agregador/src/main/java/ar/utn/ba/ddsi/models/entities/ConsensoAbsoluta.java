package ar.utn.ba.ddsi.models.entities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsensoAbsoluta implements IAlgoritmoDeConsenso {

    // Para que el hecho sea consensuado, debe estar en TODAS las colecciones
    @Override
    public List<Hecho> consensuar(List<Hecho> hechosColeccion, List<String> fuentesColeccion) {

      List<Hecho> hechosConsensuados = new ArrayList<>();
      int minimoFuentesParaConsenso = fuentesColeccion.size();
      System.out.println("--------Cantidad de fuentes: " + minimoFuentesParaConsenso);

      for (Hecho hecho : hechosColeccion) {

          boolean estaEnTodas = fuentesColeccion.stream().allMatch(fuente ->
              hechosColeccion.stream().filter(h -> h.perteneceALaFuente(fuente))
                  .anyMatch(hechoDeFuente -> hechoDeFuente.hechoIgualA(hecho)));


        if (!estaEnTodas) {
          continue;   // el hecho no se menciona en todas las fuentes --> ya no nos interesa --> analizamos el siguiente hecho
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

      return sinDuplicados;           // Nos aseguramos de no devolver dos hechos "iguales"
    }
}
