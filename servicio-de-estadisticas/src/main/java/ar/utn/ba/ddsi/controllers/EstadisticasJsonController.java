package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.services.IEstadisticasJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para exponer estadísticas en formato JSON.
 * Se asume que IEstadisticasService.getStatsData(String) devuelve un objeto
 * que Jackson puede serializar (e.g., List<Map<String, Object>>).
 */
@RestController
@RequestMapping("/api/jsons")
public class EstadisticasJsonController {

  @Autowired
  private IEstadisticasJsonService estadisticasService;

  /**
   * Obtiene la provincia con más hechos de colección en formato JSON.
   * GET /api/colecciones/provincias/top
   */
  @GetMapping("/colecciones/provincias/top")
  public ResponseEntity<Object> provinciaConMasHechosDeColeccionJSON() {
    Object stats = estadisticasService.getStatsData("provincia-con-mas-hechos-de-coleccion");

    // Comprueba si los datos existen para replicar la lógica 404 del controlador CSV
    if (stats == null || (stats instanceof List && ((List<?>) stats).isEmpty())) {
      return ResponseEntity.status(404).body(null);
    }
    return ResponseEntity.ok(stats);
  }

  /**
   * Obtiene la categoría con más hechos en formato JSON.
   * GET /api/categorias/top
   */
  @GetMapping("/categorias/top")
  public ResponseEntity<Object> categoriaConMasHechosJSON() {
    Object stats = estadisticasService.getStatsData("categoria-con-mas-hechos");

    if (stats == null || (stats instanceof List && ((List<?>) stats).isEmpty())) {
      return ResponseEntity.status(404).body(null);
    }
    return ResponseEntity.ok(stats);
  }

  /**
   * Obtiene la provincia con más hechos de una categoría en formato JSON.
   * GET /api/categorias/provincias/top
   */
  @GetMapping("/categorias/provincias/top")
  public ResponseEntity<Object> provinciaConMasHechosDeCategoriaJSON() {
    Object stats = estadisticasService.getStatsData("provincia-con-mas-hechos-de-categoria");

    if (stats == null || (stats instanceof List && ((List<?>) stats).isEmpty())) {
      return ResponseEntity.status(404).body(null);
    }
    return ResponseEntity.ok(stats);
  }

  /**
   * Obtiene el horario con más hechos por categoría en formato JSON.
   * GET /api/categorias/horarios/top
   */
  @GetMapping("categorias/horarios/top")
  public ResponseEntity<Object> horarioConMasHechosPorCategoriaJSON() {
    Object stats = estadisticasService.getStatsData("horario-con-mas-hechos-por-categoria");

    if (stats == null || (stats instanceof List && ((List<?>) stats).isEmpty())) {
      return ResponseEntity.status(404).body(null);
    }
    return ResponseEntity.ok(stats);
  }

  /**
   * Obtiene la cantidad de solicitudes que son spam en formato JSON.
   * GET /api/solicitudes/cantidad-spam
   */
  @GetMapping("/solicitudes/cantidad-spam")
  public ResponseEntity<Object> cuantasSonSpamJSON() {
    Object stats = estadisticasService.getStatsData("solicitudes-que-son-spam");

    if (stats == null || (stats instanceof List && ((List<?>) stats).isEmpty())) {
      return ResponseEntity.status(404).body(null);
    }
    return ResponseEntity.ok(stats);
  }
}

