package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
public class HechoDto implements Serializable {
  Long id;
  String titulo;
  String descripcion;
  CategoriaDto categoria;
  List<ContenidoMultimediaDto> contenidosMultimedia;
  OrigenHecho origen;
  CoordenadaDto lugarAcontecimiento;
  LocalDateTime fechaHecho;
  LocalDateTime fechaDeCarga;
  LocalDateTime fechaUltimaActualizacion;
  ContribuyenteDto contribuyente;
  IdExternoDto idExterno;
  boolean revisado;
  DepartamentoDto departamento;
  List<SolicitudDeEliminacionDto> solicitudesDeEliminacion;
  Set<EtiquetaDto> etiquetas;

  @Value
  public static class ContenidoMultimediaDto implements Serializable {
    Long id;
    String path;
  }
}