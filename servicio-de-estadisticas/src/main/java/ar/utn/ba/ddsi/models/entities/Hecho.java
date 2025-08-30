package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Builder
@Data

public class Hecho {

//    public class HechoOutputDTO {
//        private Long id;
//        private String titulo;
//        private String descripcion;
//        private Categoria categoria;
//        private ContenidoMultimedia contenidoMultimedia;
//        private OrigenHecho origen;
//        private Coordenada lugarAcontecimiento;
//        private LocalDate fechaHecho;
//        private LocalDateTime fechaDeCarga;
//        private String idExterno;
//        private Long contribuyente;
//        private List<SolicitudDeEliminacionOutputDTO> solicitudesDeEliminacion; //
//        private HashSet<String> etiquetas;
//    }

    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private String provincia;
    private String municipio;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private String idExterno;
    private Long contribuyente;

    @Builder.Default
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion = new ArrayList<>();
    @Builder.Default
    private Set<Etiqueta> etiquetas = new HashSet<>();



    public boolean perteneceALaFuente(OrigenHecho fuente) {
        return this.origen.equals(fuente);
    }

    public boolean perteneceALaFuente(String fuente) {
        String[] splitF = fuente.split(":");
        String[] splitI = idExterno.split(":");

        for (int i = 0; i < splitF.length; i++) {
            if (splitI.length <= i || !splitF[i].equals(splitI[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean hechoIgualA(Hecho otroHecho) {
        //NO CONSIDERAMOS ELIMINADO, REVISADO, ID, IDEXTERNO, CONTRIBUYENTE, ORIGEN, FECHAULTIMAACTUALIZACION, FECHADECARGA SOLICITUDESDEELIMINACION NI ETIQUETAS
        if (otroHecho == null) return false;

        if (!this.titulo.equals(otroHecho.getTitulo())) {
            return false;
        }

        return  Objects.equals(this.descripcion, otroHecho.getDescripcion()) &&
                Objects.equals(this.categoria, otroHecho.getCategoria()) &&
                Objects.equals(this.contenidoMultimedia, otroHecho.getContenidoMultimedia()) &&
                Objects.equals(this.lugarAcontecimiento, otroHecho.getLugarAcontecimiento()) &&
                Objects.equals(this.fechaHecho, otroHecho.getFechaHecho());
    }



    public boolean mismoTituloDiferentesAtributos(Hecho otroHecho) {
        //NO CONSIDERAMOS ELIMINADO, REVISADO, ID, IDEXTERNO, CONTRIBUYENTE, ORIGEN, FECHAULTIMAACTUALIZACION, FECHADECARGA SOLICITUDESDEELIMINACION NI ETIQUETAS
        if (otroHecho == null) return false;

        if (!this.titulo.equals(otroHecho.titulo)) {
            return false;
            }

        return !Objects.equals(this.descripcion, otroHecho.getDescripcion()) ||
                !Objects.equals(this.categoria, otroHecho.getCategoria()) ||
                !Objects.equals(this.contenidoMultimedia, otroHecho.getContenidoMultimedia()) ||
                !Objects.equals(this.lugarAcontecimiento, otroHecho.getLugarAcontecimiento()) ||
                !Objects.equals(this.fechaHecho, otroHecho.getFechaHecho());
    }

}