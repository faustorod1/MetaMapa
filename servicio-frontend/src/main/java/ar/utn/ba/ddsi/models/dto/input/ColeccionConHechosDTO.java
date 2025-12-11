package ar.utn.ba.ddsi.models.dto.input;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionConHechosDTO implements Serializable {
    private String identificador;
    private String titulo;
    private String descripcion;
    @JsonProperty("hechos")
    private PageHechosContainer hechosPage;
    private List<FuenteDTO> fuentes;

    @JsonIgnore
    public List<HechoDTO> getHechos() {
        if (hechosPage != null && hechosPage.getContent() != null) {
            return hechosPage.getContent();
        }
        return new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageHechosContainer implements Serializable {
        @JsonProperty("content")
        private List<HechoDTO> content;
        private int totalPages;
        private long totalElements;
        private int number;
        private int size;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}
