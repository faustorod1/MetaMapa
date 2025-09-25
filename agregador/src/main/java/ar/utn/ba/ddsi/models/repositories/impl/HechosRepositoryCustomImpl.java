package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.HechosRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HechosRepositoryCustomImpl implements HechosRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    public List<Hecho> findFromFuentes(List<Fuente> fuentes) {

        //todo

        if (fuentes == null || fuentes.isEmpty()) {
            return List.of();
        }



        StringBuilder jpql = new StringBuilder("SELECT h FROM Hecho h WHERE ");
        for (int i = 0; i < fuentes.size(); i++) {
            if (i > 0) jpql.append(" OR ");
            jpql.append("h.idExterno LIKE :p").append(i);
        }

        TypedQuery<Hecho> q = em.createQuery(jpql.toString(), Hecho.class);

        for (int i = 0; i < fuentes.size(); i++) {
            q.setParameter("p" + i, fuentes.get(i) + ":%");
        }

        return q.getResultList();
    }
}
