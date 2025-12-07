package ar.utn.ba.ddsi.models.specifications;

import ar.utn.ba.ddsi.models.entities.Hecho;
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

    private static LocalDateTime parsearFecha(String fechaStr) {
        try {
            return LocalDateTime.parse(fechaStr); // Si viene estándar
        } catch (Exception e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Si le ponemos formato manual
            return LocalDateTime.parse(fechaStr, formatter);
        }
    }
}