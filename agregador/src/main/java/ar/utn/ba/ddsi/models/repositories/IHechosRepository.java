package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface IHechosRepository {
    List<Hecho> findAll();
}
