package ar.utn.ba.ddsi.services;
import java.io.IOException;

/**
 * Interfaz de servicio para la gestión de estadísticas.
 * NOTA: Para la nueva implementación JSON, se asume la existencia del método
 * getStatsData(String statName) que devuelve la data lista para ser serializada.
 */
public interface IEstadisticasJsonService {

  /**
   * Devuelve la ruta del archivo CSV para una estadística dada.
   * (Método utilizado por el controlador original).
   * @param statName Nombre de la estadística.
   * @return Ruta completa del archivo CSV.
   */
  String getCSVPath(String statName);

  /**
   * Devuelve los datos de la estadística en un formato de objeto (e.g., List<Map<String, Object>>)
   * listo para ser serializado a JSON por Spring Boot.
   * @param statName Nombre de la estadística.
   * @return Objeto que contiene los datos de la estadística.
   */
  Object getStatsData(String statName);
}