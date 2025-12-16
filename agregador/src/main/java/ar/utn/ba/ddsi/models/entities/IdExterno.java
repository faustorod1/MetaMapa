package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Embeddable
public class IdExterno {
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fuente_id", referencedColumnName = "id")
  private Fuente fuente;
  @Column(name = "id_externo")
  private Long idExterno;

}