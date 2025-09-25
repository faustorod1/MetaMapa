package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable
public class IdExterno {
  @ManyToOne
  @JoinColumn(name = "fuente_id", referencedColumnName = "id")
  private Fuente fuente;
  @Column(name = "id_externo")
  private Long idExterno;

}