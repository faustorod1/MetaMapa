package ar.utn.ba.ddsi.models.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cache {
  private final Hecho hecho;
  private final LocalDateTime instanteDeCarga;

  public Cache(Hecho hecho) {
    this.hecho = hecho;
    this.instanteDeCarga = LocalDateTime.now();
  }
}
