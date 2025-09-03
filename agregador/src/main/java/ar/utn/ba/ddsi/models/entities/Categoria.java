package ar.utn.ba.ddsi.models.entities;

import lombok.Data;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

@Data
public class Categoria {
    private Long id;
    private String nombre;
    private List<String> sinonimos;

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.sinonimos = new ArrayList<>();
    }

    public void addSinonimo(String palabra) {
        sinonimos.add(palabra.toLowerCase());
    }

    public Boolean esLaMisma(String otraCategoria){
        String otra = otraCategoria.toLowerCase();
        if (this.nombre.toLowerCase().equals(otra)){
            return true;
        }
        if (this.sinonimos.contains(otra)){
            return true;
        }
        return esSimilarSegunLevenshtein(otra);
    }

    public Boolean esSimilarSegunLevenshtein(String b) {
        LevenshteinDistance dist = new LevenshteinDistance();
        int d = dist.apply(this.nombre, b);
        double ratio = 1 - (double) d / Math.max(this.nombre.length(), b.length());
        return ratio > 0.8;
    }
}