package ar.utn.ba.ddsi.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DetectorDeSpam {
  private static final Set<String> PALABRAS_SPAM = Set.of(
      "gratis", "urgente", "dinero", "oferta", "promoción", "click", "regalo", "ganá", "millón", "suscribite"
  );

  public static boolean esSpam(String descripcion){
    String[] palabras = descripcion.toLowerCase().split("\\W+");

    Map<String, Integer> frecuencia = new HashMap<>();
    int total = 0;
    int palabrasSpamDetectadas = 0;

    for (String palabra : palabras) {
      if (palabra.isBlank()) continue;
      total++;
      frecuencia.put(palabra, frecuencia.getOrDefault(palabra, 0) + 1);
      if (PALABRAS_SPAM.contains(palabra)) palabrasSpamDetectadas++;
    }

    double repeticionMaxima = frecuencia.values().stream().mapToInt(i -> i).max().orElse(0) / (double) total;
    double promedioPalabrasSpam = palabrasSpamDetectadas / (double) total;

    return repeticionMaxima > 0.4 || promedioPalabrasSpam > 0.25 || frecuencia.size() <= 5;
  }
}
