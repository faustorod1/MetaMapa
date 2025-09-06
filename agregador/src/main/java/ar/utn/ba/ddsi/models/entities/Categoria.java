package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(30)", nullable = false)
    private String nombre;

    @ElementCollection
    @CollectionTable(name = "categoria_sinonimos", joinColumns = @JoinColumn(name = "categoria_id"))
    @Column(name = "sinonimos")
    private List<String> sinonimos;


    protected Categoria() {} // Para el ORM

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