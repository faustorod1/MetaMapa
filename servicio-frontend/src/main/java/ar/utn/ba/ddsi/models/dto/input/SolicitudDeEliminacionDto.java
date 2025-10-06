package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class SolicitudDeEliminacionDto implements Serializable {
  String descripcion;
  HechoDTO hecho;
  LocalDateTime fechaDeCarga;
  LocalDateTime fechaDeResolucion;
  EstadoSolicitud estado;
  ContribuyenteDTO solicitante;
  AdministradorDTO administradorQueResolvio;
  int CANT_MINIMA_DE_CARACTERES;
}