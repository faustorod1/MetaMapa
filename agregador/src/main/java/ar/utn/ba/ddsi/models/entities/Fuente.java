package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "fuentes")
public class Fuente {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_de_fuente")
  private TipoDeFuente tipoDeFuente;

  @Column(name = "subfuente_id")
  private Long subfuenteId;
}
