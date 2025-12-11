package ar.utn.ba.ddsi.models.specifications;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HechoSpecs {
    public static Specification<Hecho> porFiltros(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isFalse(root.get("eliminado")));

            params.forEach((key, val) -> {
                if (val != null && !val.isEmpty()) {
                    switch (key) {
                        case "categoria":
                            predicates.add(criteriaBuilder.equal(
                                    root.get("categoria").get("id"),
                                    Long.valueOf(val)
                            ));
                            break;
                        case "fecha_reporte_desde":
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("fechaDeCarga"),
                                    parsearFecha(val)
                            ));
                            break;
                        case "fecha_reporte_hasta":
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                                    root.get("fechaDeCarga"),
                                    parsearFecha(val)
                            ));
                            break;
                        case "fecha_acontecimiento_desde":
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("fechaHecho"),
                                    parsearFecha(val)
                            ));
                            break;
                        case "fecha_acontecimiento_hasta":
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                                    root.get("fechaHecho"),
                                    parsearFecha(val)
                            ));
                            break;
                        case "ubicacion":
                            try {
                                String[] partes = val.split(",");
                                Double lat = Double.valueOf(partes[0].trim());
                                Double lon = Double.valueOf(partes[1].trim());
                                predicates.add(criteriaBuilder.equal(
                                        root.get("lugarAcontecimiento").get("latitud"), lat
                                ));
                                predicates.add(criteriaBuilder.equal(
                                        root.get("lugarAcontecimiento").get("longitud"), lon
                                ));
                            } catch (Exception e) {
                                System.err.println("Formato de coordenada inválido: " + val);
                            }
                            break;
                        case "titulo":
                            predicates.add(criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("titulo")), "%" + val.toLowerCase() + "%"
                            ));
                            break;
                        case "descripcion":
                            predicates.add(criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("descripcion")), "%" + val.toLowerCase() + "%"
                            ));
                            break;
                    }
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

/*
    public static Specification<Hecho> pertenecienteAColeccion(String coleccionIdentificador, boolean consensuados) {
        return (root, query, cb) -> {
            var subquery = query.subquery(Hecho.class);
            Root<Coleccion> coleccionRoot = subquery.from(Coleccion.class);

            jakarta.persistence.criteria.Join<Coleccion, Hecho> joinHechos = consensuados
                    ? coleccionRoot.join("hechosConsensuados")
                    : coleccionRoot.join("hechos");

            subquery.select(joinHechos)
                    .where(cb.equal(coleccionRoot.get("identificador"), coleccionIdentificador));

            return root.in(subquery);
        };
    }
    */
public static Specification<Hecho> pertenecienteAColeccion(Long coleccionId,
                                                           boolean traerConsensuados) {

    return (root, query, cb) -> {
        assert query != null;
        query.distinct(true);

        // 1. JOIN DE PERTENENCIA BASE
        Join<Hecho, Coleccion> join = root.join("colecciones", JoinType.INNER);
        Predicate base = cb.equal(join.get("id"), coleccionId);

        if (!traerConsensuados) {
            return base;
        }

        // y colecciones son entidades/tablas distintas en el modelo Hecho
        Join<Hecho, Coleccion> joinCons = root.join("coleccionesConsensuadas", JoinType.INNER);
        Predicate cons = cb.equal(joinCons.get("id"), coleccionId);

        return cb.and(base, cons);
    };
}


    private static LocalDateTime parsearFecha(String fechaStr) {
        try {
            return LocalDateTime.parse(fechaStr); // Si viene estándar
        } catch (Exception e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Si le ponemos formato manual
            return LocalDateTime.parse(fechaStr, formatter);
        }
    }
}