package ar.utn.ba.ddsi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
// TODO: repasar DDC (constructores del DDC, flechas, etc...)

public class Hecho {

    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;//TODO revisar si puede modificarse una vez q ya se creo al hecho. Sino usar fechaDeUltimaModificacion
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.
    private Contribuyente contribuyente;

    private Long id;//USO: Identificacion unica para el Repository (futura BD)

    // USO: cuando un contribuyente sube un hecho se podra aceptar, aceptar con sugerencia de cambios o rechazar la información
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

}