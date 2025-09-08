package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.models.entities.ubicacion.Municipio;
import ar.utn.ba.ddsi.models.entities.ubicacion.Provincia;
import jakarta.persistence.*;
import lombok.*;

import ar.utn.ba.ddsi.commons.Coordenada;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "hechos")
public class Hecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // USO: Identificacion unica para el Repository (futura BD)

    @Column(name = "titulo", columnDefinition = "TEXT", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = true)
    private Categoria categoria;

    @OneToMany
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    private List<ContenidoMultimedia> contenidosMultimedia;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen", columnDefinition = "varchar(20)")
    private OrigenHecho origen;

    @Embedded
    private Coordenada lugarAcontecimiento;

    @Column(name = "fecha_del_hecho", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaHecho;

    @Column(name = "fecha_de_carga", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaDeCarga;

    @Column(name = "fecha_de_ultima_actualizacion", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "eliminado", nullable = false)
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.}

    @ManyToOne
    @JoinColumn(name = "contribuyente_id", referencedColumnName = "id", nullable = true)
    private Contribuyente contribuyente;

    @Column(name = "id_externo", columnDefinition = "VARCHAR(20)", nullable = false)
    private String idExterno; // //proxy/2/5

    @Column(name = "revisado", nullable = false)
    private boolean revisado; // USO: cuando un contribuyente sube un hecho se podra aceptar, aceptar con sugerencia de cambios o rechazar la información

    @ManyToOne
    @JoinColumn(name = "municipio_id", referencedColumnName = "id", nullable = true)
    private Municipio municipio;

    @OneToMany(mappedBy = "hecho")
    @Builder.Default
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion = new ArrayList<>();

    @Access(AccessType.FIELD)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "etiquetas_por_hecho",
            joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name ="etiqueta_id", referencedColumnName = "id"))
    @Builder.Default // si el builder no le da el valor, hace esto por defecto
    private Set<Etiqueta> etiquetas = new HashSet<>();

    protected Hecho() {}

    public SolicitudDeEliminacion solicitarEliminacion(String justificacion, Contribuyente contribuyente) {
        try {

            SolicitudDeEliminacion solicitud = SolicitudDeEliminacion.builder()
                    .hecho(this)
                    .solicitante(contribuyente)
                    .descripcion(justificacion)
                    .build();

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

    public boolean hechoIgualA(Hecho otroHecho) {
        //NO CONSIDERAMOS ELIMINADO, REVISADO, ID, IDEXTERNO, CONTRIBUYENTE, ORIGEN, FECHAULTIMAACTUALIZACION, FECHADECARGA SOLICITUDESDEELIMINACION NI ETIQUETAS
        if (otroHecho == null) return false;

        if (!this.titulo.equals(otroHecho.getTitulo())) {
            return false;
        }

        return  Objects.equals(this.descripcion, otroHecho.getDescripcion()) &&
                Objects.equals(this.categoria, otroHecho.getCategoria()) &&
                Objects.equals(this.contenidosMultimedia, otroHecho.getContenidosMultimedia()) &&
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
                !Objects.equals(this.contenidosMultimedia, otroHecho.getContenidosMultimedia()) ||
                !Objects.equals(this.lugarAcontecimiento, otroHecho.getLugarAcontecimiento()) ||
                !Objects.equals(this.fechaHecho, otroHecho.getFechaHecho());
    }

    public Provincia getProvincia(){
        return this.municipio.getProvincia();
    }

}