package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.Categoria;

import java.util.List;

public interface ICategoriaRepository {
  List<Categoria> findAll();
}
