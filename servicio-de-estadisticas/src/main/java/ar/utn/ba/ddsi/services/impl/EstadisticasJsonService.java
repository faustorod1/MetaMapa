package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.services.IEstadisticasJsonService;
import ar.utn.ba.ddsi.commons.CSVReader;
import ar.utn.ba.ddsi.models.dtos.inputs.CategoriaInputDTO;
import ar.utn.ba.ddsi.models.dtos.inputs.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.dtos.inputs.HechoInputDTO;
import ar.utn.ba.ddsi.models.dtos.inputs.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EstadisticasJsonService implements IEstadisticasJsonService {
  private WebClient servicioAgregador;
  private String csvPathTemplate = "../%s.csv";

  @Autowired
  public EstadisticasJsonService(@Value("${servicio.agregador.api.base-url}") String apiAgregadorURL) {
    this.servicioAgregador = WebClient.builder().baseUrl(apiAgregadorURL).build();
  }

  // --- Métodos de Interfaz IEstadisticasService ---

  /**
   * Mantiene la funcionalidad original de generación de archivos CSV.
   */
  public void generarEstadisticas() {
    List<Hecho> hechos = fetchHechosFromAgregador();
    List<Categoria> categorias = fetchCategoriasFromAgregador();
    List<Coleccion> colecciones = fetchColeccionesFromAgregador();
    List<SolicitudDeEliminacion> solicitudes = fetchSolicitudesDeEliminacionFromAgregador();

    // Se mantiene la lógica de generar archivos CSV
    escribeCSV("provincia-con-mas-hechos-de-categoria", generarStringProvinciaConMasHechosDeCategoria(hechos, categorias));
    escribeCSV("categoria-con-mas-hechos", generarStringCategoriaConMasHechos(hechos));
    escribeCSV("horario-con-mas-hechos-por-categoria", generarStringHorariConMasHechosPorCategoria(hechos, categorias));
    escribeCSV("solicitudes-que-son-spam", generarStringSolicitudesQueSonSpam(solicitudes));
    escribeCSV("provincia-con-mas-hechos-de-coleccion", generarStringProvinciaConMasHechosDeColeccion(colecciones));
  }

  /**
   * Devuelve la ruta del archivo CSV (mantenido para compatibilidad).
   */
  @Override
  public String getCSVPath(String filename) {
    return csvPathTemplate.formatted(filename);
  }

  /**
   * NUEVO MÉTODO: Devuelve los datos de la estadística como un objeto serializable a JSON.
   * La data se carga al momento de la solicitud.
   */
  @Override
  public Object getStatsData(String statName) {
    switch (statName) {
      case "provincia-con-mas-hechos-de-coleccion":
        List<Coleccion> colecciones = fetchColeccionesFromAgregador();
        return generarDatosProvinciaConMasHechosDeColeccion(colecciones);

      case "categoria-con-mas-hechos":
        List<Hecho> hechos_cat_top = fetchHechosFromAgregador();
        return generarDatosCategoriaConMasHechos(hechos_cat_top);

      case "provincia-con-mas-hechos-de-categoria":
        List<Hecho> hechos_prov_cat = fetchHechosFromAgregador();
        List<Categoria> categorias_prov_cat = fetchCategoriasFromAgregador();
        return generarDatosProvinciaConMasHechosDeCategoria(hechos_prov_cat, categorias_prov_cat);

      case "horario-con-mas-hechos-por-categoria":
        List<Hecho> hechos_horario = fetchHechosFromAgregador();
        List<Categoria> categorias_horario = fetchCategoriasFromAgregador();
        return generarDatosHorariConMasHechosPorCategoria(hechos_horario, categorias_horario);

      case "solicitudes-que-son-spam":
        List<SolicitudDeEliminacion> solicitudes = fetchSolicitudesDeEliminacionFromAgregador();
        return generarDatosSolicitudesQueSonSpam(solicitudes);

      default:
        // El controlador JSON manejará este 'null' devolviendo 404
        return null;
    }
  }

  // --- Lógica de Generación de Datos (JSON) ---
  // Métodos refactorizados para devolver List<Map<String, Object>> o Map<String, Object>

  /**
   * Genera la lista de estadísticas para Provincia con más hechos de Colección (JSON).
   * Retorna List<Map<String, Object>>
   */
  private List<Map<String, Object>> generarDatosProvinciaConMasHechosDeColeccion(List<Coleccion> colecciones) {
    List<Map<String, Object>> result = new ArrayList<>();

    colecciones.forEach(coleccion -> {
      List<Hecho> hechos = fetchHechosDeColeccionFromAgregador(coleccion.getIdentificador());
      List<Hecho> hechosCurados = hechos.stream().filter(h -> h.getProvincia() != null).toList();

      Map.Entry<String, Long> max = masHechosSegunParametro(hechosCurados, Hecho::getProvincia);
      if (max == null) {
        return;
      }
      result.add(Map.of(
          "idColeccion", coleccion.getIdentificador(),
          "tituloColeccion", coleccion.getTitulo(),
          "provincia", max.getKey(),
          "cantidadHechos", max.getValue()
      ));
    });
    return result;
  }

  /**
   * Genera la estadística de Categoría con más hechos (JSON).
   * Retorna Map<String, Object> (una sola fila de resultado).
   */
  private Map<String, Object> generarDatosCategoriaConMasHechos(List<Hecho> hechos) {
    Map.Entry<Categoria, Long> max = masHechosSegunParametro(hechos, Hecho::getCategoria);
    if (max == null) {
      return Map.of();
    }
    return Map.of(
        "categoria", max.getKey().getNombre(),
        "cantidadHechos", max.getValue()
    );
  }

  /**
   * Genera la lista de estadísticas para Provincia con más hechos de Categoría (JSON).
   * Retorna List<Map<String, Object>>
   */
  private List<Map<String, Object>> generarDatosProvinciaConMasHechosDeCategoria(List<Hecho> hechos, List<Categoria> categorias) {
    List<Map<String, Object>> result = new ArrayList<>();
    final List<Hecho> hechosCurados = hechos.stream().filter(h -> h.getProvincia() != null).toList();

    categorias.forEach(categoria -> {
      List<Hecho> hechosDeCategoria = hechosCurados.stream().filter(h->h.getCategoria().equals(categoria)).toList();
      Map.Entry<String, Long> max = masHechosSegunParametro(hechosDeCategoria, Hecho::getProvincia);
      if (max == null) {
        return;
      }
      result.add(Map.of(
          "categoria", categoria.getNombre(),
          "provincia", max.getKey(),
          "cantidadHechos", max.getValue()
      ));
    });
    return result;
  }

  /**
   * Genera la lista de estadísticas para Horario con más hechos por Categoría (JSON).
   * Retorna List<Map<String, Object>>
   */
  private List<Map<String, Object>> generarDatosHorariConMasHechosPorCategoria(List<Hecho> hechos, List<Categoria> categorias) {
    List<Map<String, Object>> result = new ArrayList<>();
    categorias.forEach(categoria -> {
      List<Hecho> hechosDeCategoria = hechos.stream().filter(h->h.getCategoria().equals(categoria)).toList();
      Map.Entry<Integer, Long> max = masHechosSegunParametro(hechosDeCategoria, h -> h.getFechaHecho().getHour());
      if (max == null) {
        return;
      }
      result.add(Map.of(
          "categoria", categoria.getNombre(),
          "horario", String.format("%02d:00", max.getKey()),
          "cantidadHechos", max.getValue()
      ));
    });
    return result;
  }

  /**
   * Genera la estadística de Solicitudes que son Spam (JSON).
   * Retorna Map<String, Integer>.
   */
  private Map<String, Integer> generarDatosSolicitudesQueSonSpam(List<SolicitudDeEliminacion> solicitudes) {
    Integer total = solicitudes.size();
    // Usamos count() en el stream y lo casteamos a Integer
    Integer spam = (int) solicitudes.stream().filter(s -> s.getEstado().equals(EstadoSolicitud.RECHAZADA_POR_SPAM)).count();

    return Map.of(
        "cantidadTotal", total,
        "spam", spam
    );
  }

  // --- Lógica de Generación de Datos (CSV) ---
  // Mantenido para la funcionalidad 'generarEstadisticas()' original

  private void escribeCSV(String filename, ArrayList<String[]> data) {
    CSVReader.crear(getCSVPath(filename), data);
  }

  private ArrayList<String[]> generarStringProvinciaConMasHechosDeCategoria(List<Hecho> hechos, List<Categoria> categorias) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = {"Categoría", "Provincia", "Cantidad de hechos"};
    str.add(headers);

    final List<Hecho> hechosCurados = hechos.stream().filter(h -> h.getProvincia() != null).toList();

    categorias.forEach(categoria -> {
      List<Hecho> hechosDeCategoria = hechosCurados.stream().filter(h -> h.getCategoria().equals(categoria)).toList();
      Map.Entry<String, Long> max = masHechosSegunParametro(hechosDeCategoria, Hecho::getProvincia);
      if (max == null) {
        return;
      }
      String provincia = max.getKey();
      Long cantidadHechos = max.getValue();
      String[] fila = {categoria.getNombre(), provincia, cantidadHechos.toString()};
      str.add(fila);
    });

    return str;
  }

  private ArrayList<String[]> generarStringCategoriaConMasHechos(List<Hecho> hechos) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = {"Categoría", "Cantidad de hechos"};
    str.add(headers);

    Map.Entry<Categoria, Long> max = masHechosSegunParametro(hechos, Hecho::getCategoria);
    if (max == null) {
      return str;
    }
    Categoria categoria = max.getKey();
    Long cantidadHechos = max.getValue();
    String[] fila = {categoria.getNombre(), cantidadHechos.toString()};
    str.add(fila);
    return str;
  }

  private ArrayList<String[]> generarStringHorariConMasHechosPorCategoria(List<Hecho> hechos, List<Categoria> categorias) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = {"Categoría", "Horario", "Cantidad de hechos"};
    str.add(headers);

    categorias.forEach(categoria -> {
      List<Hecho> hechosDeCategoria = hechos.stream().filter(h -> h.getCategoria().equals(categoria)).toList();
      Map.Entry<Integer, Long> max = masHechosSegunParametro(hechosDeCategoria, h -> h.getFechaHecho().getHour());
      if (max == null) {
        return;
      }
      String horario = String.format("%02d:00", max.getKey());
      Long cantidadHechos = max.getValue();
      String[] fila = {categoria.getNombre(), horario, cantidadHechos.toString()};
      str.add(fila);
    });

    return str;
  }

  private ArrayList<String[]> generarStringSolicitudesQueSonSpam(List<SolicitudDeEliminacion> solicitudes) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = {"Cantidad total", "Spam"};
    str.add(headers);

    Integer total = solicitudes.size();
    Integer spam = (int) solicitudes.stream().filter(s -> s.getEstado().equals(EstadoSolicitud.RECHAZADA_POR_SPAM)).count();

    String[] fila = {total.toString(), spam.toString()};
    str.add(fila);

    return str;
  }


  private ArrayList<String[]> generarStringProvinciaConMasHechosDeColeccion(List<Coleccion> colecciones) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = {"ID Colección", "Titulo Colección", "Provincia", "Cantidad de hechos"};
    str.add(headers);

    colecciones.forEach(coleccion -> {
      List<Hecho> hechos = fetchHechosDeColeccionFromAgregador(coleccion.getIdentificador());
      List<Hecho> hechosCurados = hechos.stream().filter(h -> h.getProvincia() != null).toList();

      Map.Entry<String, Long> max = masHechosSegunParametro(hechosCurados, Hecho::getProvincia);
      if (max == null) {
        return;
      }
      String provincia = max.getKey();
      Long cantidadHechos = max.getValue();
      String[] fila = {coleccion.getIdentificador(), coleccion.getTitulo(), provincia, cantidadHechos.toString()};
      str.add(fila);
    });

    return str;
  }

  // --- Privados (Mantenidos) ---

  private List<Hecho> fetchHechosFromAgregador() {
    return servicioAgregador.get()
        .uri("/api/hechos")
        .retrieve()
        .bodyToFlux(HechoInputDTO.class)
        .map(HechoInputDTO::toEntity)
        .collectList()
        .block();
  }

  private List<Categoria> fetchCategoriasFromAgregador() {
    return servicioAgregador.get()
        .uri("/api/categorias")
        .retrieve()
        .bodyToFlux(CategoriaInputDTO.class)
        .map(c -> new Categoria(c.getNombre()))
        .collectList()
        .block();
  }

  private List<Coleccion> fetchColeccionesFromAgregador() {
    return servicioAgregador.get()
        .uri("/api/colecciones")
        .retrieve()
        .bodyToFlux(ColeccionInputDTO.class)
        .map(ColeccionInputDTO::toEntity)
        .collectList()
        .block();
  }

  private List<Hecho> fetchHechosDeColeccionFromAgregador(String identificadorColeccion) {
    return servicioAgregador.get()
        .uri("/api/colecciones/%s/hechos".formatted(identificadorColeccion))
        .retrieve()
        .bodyToFlux(HechoInputDTO.class)
        .map(HechoInputDTO::toEntity)
        .collectList()
        .block();
  }

  private List<SolicitudDeEliminacion> fetchSolicitudesDeEliminacionFromAgregador() {
    return servicioAgregador.get()
        .uri("/api/solicitudes")
        .retrieve()
        .bodyToFlux(SolicitudDeEliminacionInputDTO.class)
        .map(SolicitudDeEliminacionInputDTO::toEntity)
        .collectList()
        .block();
  }


  // [(key,value),(TipoDeDato,Long)]
  private <TipoRetorno> List<Map.Entry<TipoRetorno, Long>> cantidadHechosSegunParametro(List<Hecho> hechos, Function<Hecho, TipoRetorno> criterio) {
    return hechos.stream()
        .collect(Collectors.groupingBy(criterio, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .toList();
  }

  // (TipoDeDato, Long)
  private <TipoRetorno> Map.Entry<TipoRetorno, Long> masHechosSegunParametro(List<Hecho> hechos, Function<Hecho, TipoRetorno> criterio) {
    return cantidadHechosSegunParametro(hechos, criterio)
        .stream()
        .findFirst()
        .orElse(null);
  }

}
