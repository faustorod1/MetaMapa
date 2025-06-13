package ar.utn.ba.ddsi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import ar.utn.ba.ddsi.commons.Coordenada;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Builder
@Data

public class Hecho {

    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.
    private Contribuyente contribuyente;
    private Long id;        //USO: Identificacion unica para el Repository (futura BD)
    private String idExterno; // ID que tenía el hecho en la fuente de la que llegó (útil para modificar los hechos)
    private boolean revisado; // USO: cuando un contribuyente sube un hecho se podra aceptar, aceptar con sugerencia de cambios o rechazar la información

    @Builder.Default
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion = new ArrayList<>();
    @Builder.Default //si el builder no le da el valor, hace esto por defecto
    private HashSet<Etiqueta> etiquetas = new HashSet<>();

    public SolicitudDeEliminacion solicitarEliminacion(String justificacion, Contribuyente contribuyente) {
        try {
            SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(this, justificacion, contribuyente);
            solicitudesDeEliminacion.add(solicitud);
            return solicitud;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void etiquetar(Etiqueta etiqueta){
        etiquetas.add(etiqueta);
    }


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

}