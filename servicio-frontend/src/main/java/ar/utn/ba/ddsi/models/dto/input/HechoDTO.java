package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Value
public class HechoDTO implements Serializable {
  Long id;
  String titulo;
  String descripcion;
  CategoriaDTO categoria;
  List<ContenidoMultimediaDto> contenidosMultimedia;
  OrigenHecho origen;
  CoordenadaDTO lugarAcontecimiento;
  LocalDateTime fechaHecho;
  LocalDateTime fechaDeCarga;
  LocalDateTime fechaUltimaActualizacion;
  DepartamentoDTO departamento;
  List<SolicitudDeEliminacionDto> solicitudesDeEliminacion;
  Set<EtiquetaDTO> etiquetas;

  @Value
  public static class ContenidoMultimediaDto implements Serializable {
    Long id;
    String path;
  }
}