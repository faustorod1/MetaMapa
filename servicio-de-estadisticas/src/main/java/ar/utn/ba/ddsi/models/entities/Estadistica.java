package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "estadisticas")
public class Estadistica {

  public Estadistica(TipoEstadistica tipoEstadistica, String path){
    this.tipoEstadistica = tipoEstadistica;
    this.path = path;
  }

  protected Estadistica() {};

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", columnDefinition = "varchar(20)")
  private TipoEstadistica tipoEstadistica;

  @Column(name = "pathCSV", columnDefinition = "TEXT", nullable = false)
  private String path;
}
