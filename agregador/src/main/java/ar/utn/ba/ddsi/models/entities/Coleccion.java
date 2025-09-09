package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.converters.AlgoritmoDeConsensoConverter;
import ar.utn.ba.ddsi.models.entities.consenso.AlgoritmoDeConsenso;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "colecciones")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identificador", columnDefinition = "VARCHAR(255)", nullable = false)
    private String identificador;       // handle: string alfanumerico (único para cada colección)

    @Column(name = "titulo", columnDefinition = "VARCHAR(40)", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "VARCHAR(255)", nullable = true)
    private String descripcion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "criterio_id", referencedColumnName = "id")
    private Criterio criterioDePertenencia;

    @Convert(converter = AlgoritmoDeConsensoConverter.class)
    @Column(name = "algoritmo_de_consenso", columnDefinition = "VARCHAR(40)")
    private AlgoritmoDeConsenso algoritmoDeConsenso;

    @ElementCollection
    @CollectionTable(name = "coleccion_fuentes", joinColumns = @JoinColumn(name = "coleccion_id"))
    @Column(name = "fuente", columnDefinition = "varchar(30)", nullable = false)
    private List<String> fuentes; //TODO convertir a enum? (correccion)

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "hechos_en_coleccion_cargados_manualmente",
            joinColumns = @JoinColumn(name = "coleccion_id",
            referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
    private List<Hecho> hechosCargadosManualmente;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "hechos_en_coleccion_consensuados",
        joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name ="hecho_id", referencedColumnName = "id"))
    private List<Hecho> hechosConsensuados;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "coleccion_hechos",
        joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE) // <- Estas cosas son para que no genere getter/setter de esto
    private List<Hecho> hechos;

    protected Coleccion() {} //Para el ORM

    public Coleccion(String identificador, String titulo, String descripcion, Criterio criterioDePertenencia, List<String> fuentes, AlgoritmoDeConsenso algoritmoDeConsenso) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterioDePertenencia = criterioDePertenencia;
        this.fuentes = fuentes;
        this.algoritmoDeConsenso = algoritmoDeConsenso;     // Si, tambien se específica al crear la coleccion
        hechos = new ArrayList<>();
        hechosCargadosManualmente = new ArrayList<>();
        hechosConsensuados = new ArrayList<>();
    }

    public List<Hecho> getHechos() {
        return new ArrayList<Hecho>(hechos); // Creo uno nuevo para que no rompa el Set original si lo modifican
    }
    public List<Hecho> getHechosConsensuados() {
        return new ArrayList<>(hechosConsensuados);
    }

    public Criterio getCriterio() {
        return criterioDePertenencia;
    }

    public boolean contiene(Hecho hecho){
        return this.hechos.contains(hecho);
    }

    public boolean cumpleFiltros(Hecho hecho){
        return !aplicarFiltros(List.of(hecho)).isEmpty();
    }

    // TODO hacer que los hechos manuales pasen aun si no cumplen el criterio
    public List<Hecho> aplicarFiltros(List<Hecho> hechos) {
        List<Hecho> hechosAFiltrar = hechos
                .stream()
                .filter( h ->
                        fuentes.stream().anyMatch(h::perteneceALaFuente)
                                || hechosCargadosManualmente.stream().anyMatch(hcm -> h.getId().equals(hcm.getId()))
                ).toList();
        return criterioDePertenencia.aplicarA(hechosAFiltrar);
    }

    public void agregarTandaDeHechos(List<Hecho> tanda) {
        List<Hecho> hechosPotables = aplicarFiltros(tanda);
        List<Hecho> hechosARemover = new ArrayList<>(tanda);
        hechosPotables.forEach(hechosARemover::remove);

        hechosPotables.forEach(h -> {
            if (!contiene(h)) hechos.add(h);
        });
        hechosARemover.forEach(h -> {
           if (contiene(h)) hechos.remove(h);
        });
    }


    public void removerHechoEliminado(Hecho hecho) {
        removerHecho(hecho);
        hechosCargadosManualmente.remove(hecho);
    }

    public void removerHecho(Hecho hecho) {
        hechos.remove(hecho);
    }

    // TODO: Cuándo y cómo deberíamos ver qué hechos permanecen en la colección al actualizar?

    public void consensuarHechos(){
        this.hechosConsensuados.clear();        // Para asegurar que todos los hechos esten consensuados al día, vaciamos la lista
        if(this.algoritmoDeConsenso != null){
            this.hechosConsensuados = this.algoritmoDeConsenso.consensuar(this.hechos, this.fuentes);    // Los volvemos a consensuar
        }else{
            this.hechosConsensuados = this.hechos;      // En caso de que no se aclare un algoritmo de consenso, todos sus hechos catalogarán como "consensuados"
        }
    }
}