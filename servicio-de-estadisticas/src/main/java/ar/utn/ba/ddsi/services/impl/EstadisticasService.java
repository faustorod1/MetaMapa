package ar.utn.ba.ddsi.services.impl;

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
import ar.utn.ba.ddsi.services.IEstadisticasService;
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
public class EstadisticasService implements IEstadisticasService {
  private WebClient servicioAgregador;
  private String csvPathTemplate =  "../%s.csv";

  @Autowired
  public EstadisticasService(@Value("${servicio.agregador.api.base-url}") String apiAgregadorURL) {
    this.servicioAgregador = WebClient.builder().baseUrl(apiAgregadorURL).build();
  }

  public void generarEstadisticas() {
    List<Hecho> hechos = fetchHechosFromAgregador();
    List<Categoria> categorias = fetchCategoriasFromAgregador();
    List<Coleccion> colecciones = fetchColeccionesFromAgregador();
    List<SolicitudDeEliminacion> solicitudes = fetchSolicitudesDeEliminacionFromAgregador();

    escribeCSV("provincia-con-mas-hechos-de-categoria", generarStringProvinciaConMasHechosDeCategoria(hechos, categorias));
    escribeCSV("categoria-con-mas-hechos",generarStringCategoriaConMasHechos(hechos));
    escribeCSV("horario-con-mas-hechos-por-categoria",generarStringHorariConMasHechosPorCategoria(hechos,categorias));
    escribeCSV("solicitudes-que-son-spam",generarStringSolicitudesQueSonSpam(solicitudes));
    escribeCSV("provincia-con-mas-hechos-de-coleccion",generarStringProvinciaConMasHechosDeColeccion(colecciones));
  }

  public String getCSVPath(String filename) {
    return csvPathTemplate.formatted(filename);
  }

  private void escribeCSV(String filename, ArrayList<String[]> data){
    CSVReader.crear(getCSVPath(filename), data);
  }

  //--------------------------------------------------------------- Comportamiento estadistico ---------------------------------------------------------------//

  private ArrayList<String[]> generarStringProvinciaConMasHechosDeCategoria(List<Hecho> hechos, List<Categoria> categorias) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = { "Categoría", "Provincia", "Cantidad de hechos" };
    str.add(headers);

    categorias.forEach(categoria -> {
      List<Hecho> hechosDeCategoria = hechos.stream().filter(h->h.getCategoria().equals(categoria)).toList();
      Map.Entry<String, Long> max = masHechosSegunParametro(hechosDeCategoria, Hecho::getProvincia);
      String provincia = max.getKey();
      Long cantidadHechos = max.getValue();
      String[] fila = { categoria.getNombre(), provincia, cantidadHechos.toString() };
      str.add(fila);
    });

    return str;
  }

  private ArrayList<String[]> generarStringCategoriaConMasHechos(List<Hecho> hechos) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = { "Categoría", "Cantidad de hechos" };
    str.add(headers);

    Map.Entry<Categoria, Long> max = masHechosSegunParametro(hechos, Hecho::getCategoria);
    Categoria categoria = max.getKey();
    Long cantidadHechos = max.getValue();
    String[] fila = { categoria.getNombre(), cantidadHechos.toString() };
    str.add(fila);
    return str;
  }

  private ArrayList<String[]> generarStringHorariConMasHechosPorCategoria(List<Hecho> hechos, List<Categoria> categorias) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = { "Categoría", "Horario", "Cantidad de hechos" };
    str.add(headers);

    categorias.forEach(categoria -> {
      List<Hecho> hechosDeCategoria = hechos.stream().filter(h->h.getCategoria().equals(categoria)).toList();
      Map.Entry<Integer, Long> max = masHechosSegunParametro(hechosDeCategoria, h -> h.getFechaHecho().getHour());
      String horario = String.format("%02d:00", max.getKey());
      Long cantidadHechos = max.getValue();
      String[] fila = { categoria.getNombre(), horario, cantidadHechos.toString() };
      str.add(fila);
    });

    return str;
  }

  private ArrayList<String[]> generarStringSolicitudesQueSonSpam(List<SolicitudDeEliminacion> solicitudes) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = { "Cantidad total", "Spam" };
    str.add(headers);

    Integer total = solicitudes.size();
    Integer spam = solicitudes.stream().filter(s -> s.getEstado().equals(EstadoSolicitud.RECHAZADA_POR_SPAM)).toList().size();

    String[] fila = { total.toString(), spam.toString() };

    return str;
  }


  private ArrayList<String[]> generarStringProvinciaConMasHechosDeColeccion(List<Coleccion> colecciones) {
    ArrayList<String[]> str = new ArrayList<>();
    String[] headers = { "ID Colección", "Titulo Colección", "Provincia", "Cantidad de hechos" };
    str.add(headers);

    colecciones.forEach(coleccion -> {
      List<Hecho> hechos = fetchHechosDeColeccionFromAgregador(coleccion.getIdentificador());
      Map.Entry<String, Long> max = masHechosSegunParametro(hechos, Hecho::getProvincia);
      String provincia = max.getKey();
      Long cantidadHechos = max.getValue();
      String[] fila = { coleccion.getIdentificador(), coleccion.getTitulo(), provincia, cantidadHechos.toString() };
      str.add(fila);
    });

    return str;
  }


  //--------------------------------------------------------------- Privados ---------------------------------------------------------------//

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


  //[(key,value),(TipoDeDato,Long)]
  private <TipoRetorno> List<Map.Entry<TipoRetorno, Long>> cantidadHechosSegunParametro(List <Hecho> hechos, Function<Hecho, TipoRetorno> criterio) {
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
