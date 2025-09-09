package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.CSVReader;
import ar.utn.ba.ddsi.models.dtos.inputs.HechoInputDTO;
import ar.utn.ba.ddsi.models.dtos.inputs.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EstadisticasService implements IEstadisticasService {
    private WebClient servicioAgregador;
    private IEstadisticasRepository estadisticasRepository;

    private String[] csvs = {"provincia_con_mas_hechos_de_coleccion.csv","provincia_con_mas_hechos_de_categoria", "categoria_con_mas_hechos.csv", "horario_con_mas_hechos_de_cierta_categoria.csv", "solicitudes_spam.csv"};

    @Autowired
    public EstadisticasService(@Value("${servicio.agregador.api.base-url}") String apiAgregadorURL) {
        this.servicioAgregador = WebClient.builder().baseUrl(apiAgregadorURL).build();
    }

    public void updateEstadisticas(){

        List<Estadistica> estadisticas = new ArrayList<>();

       estadisticas.add(new Estadistica(TipoEstadistica.PROVINCIA_CON_MAS_HECHOS_POR_COLECCION, provinciaConMasHechosDeColeccionCSV());
       estadisticas.add(new Estadistica(TipoEstadistica.CATEGORIA_CON_MAS_HECHOS, categoriaConMasHechosCSV()));
       estadisticas.add(new Estadistica(TipoEstadistica.PROVINCIA_CON_MAS_HECHOS_POR_CATEGORIA, provinciaConMasHechosDeCategoriaCSV()));
       estadisticas.add(new Estadistica(TipoEstadistica.SOLICITUDES_DE_ELIMINACION_SPAM, solicitudesSpamCSV()));
       estadisticas.add(new Estadistica(TipoEstadistica.HORA_CON_MAS_HECHOS_POR_CATEGORIA, horarioConMasHechosPorCategoriaCSV()));

        estadisticasRepository.saveAll(estadisticas);


    }

    public void eliminarEstadisticasViejas(){
        Path basePath = Path.of("../"); 
    }


    public String provinciaConMasHechosDeColeccion(String coleccion_id) {
        List<Hecho> hechos = getHechosDeColeccionFromAgregador(coleccion_id);
        return provinciaConMasHechos(hechos);
    }

    public String provinciaConMasHechosDeCategoria(String categoria) {
        List<Hecho> hechos = getHechosFromAgregador();
        List<Hecho> hechosDeCategoria = hechos.stream()
            .filter(h -> h.getCategoria().getNombre().equals(categoria))
            .collect(Collectors.toList());
        return provinciaConMasHechos(hechosDeCategoria);
    }

    public String categoriaConMasHechos() {
        List<Hecho> hechos = getHechosFromAgregador();
        return masHechosSegunParametro(hechos, hecho -> hecho.getCategoria().getNombre());
    }

    public LocalTime horarioConMasHechosDeCiertaCategoria(String categoria) {
        List<Hecho> hechos = getHechosFromAgregador();
        List<Hecho> hechosDeCategoriaParticular = hechos.stream().filter(hecho -> hecho.getCategoria().getNombre().equals(categoria)).toList();

        int hora = masHechosSegunParametro(hechosDeCategoriaParticular, hecho -> hecho.getFechaHecho().getHour());
        return LocalTime.of(hora, 0);
    }

    public Long solicitudesSpam() {
        List<SolicitudDeEliminacion> solicitudes = getSolicitudesFromAgregador();
        return solicitudes
                .stream()
                .filter(solicitud -> solicitud.getEstado().equals(EstadoSolicitud.RECHAZADA_POR_SPAM))
                .count();
    }

    //--------------------------------------------------------- CSV --------------------------------------------------------------//

    public String provinciaConMasHechosDeColeccionCSV() {
        // List<Hecho> hechos = getHechosDeColeccionFromAgregador(coleccion_id);
        List<Hecho> hechos = getHechosFromAgregador();
        List<Map.Entry<String, Long>> leaderboard = cantidadHechosSegunParametro(hechos, Hecho::getProvincia);
        return escribirCSV(leaderboard, "Provincia", csvs[0] );
    }

    public String categoriaConMasHechosCSV(){
        List<Hecho> hechos = getHechosFromAgregador();
        List<Map.Entry<String, Long>> leaderboard = cantidadHechosSegunParametro(hechos, hecho -> hecho.getCategoria().getNombre());
        return escribirCSV(leaderboard, "Categoria", csvs[2]);
    }

    public String provinciaConMasHechosDeCategoriaCSV(String categoria){
        List<Hecho> hechos = getHechosFromAgregador().stream().filter(hecho -> hecho.getCategoria().getNombre().equals(categoria)).toList();
        List<Map.Entry<String, Long>> leaderboard = cantidadHechosSegunParametro(hechos, Hecho::getProvincia);
        return escribirCSV(leaderboard, "Provincia",csvs[1]);

    }

    public String horarioConMasHechosPorCategoriaCSV(String categoria){
        List<Hecho> hechos = getHechosFromAgregador().stream().filter(hecho -> hecho.getCategoria().getNombre().equals(categoria)).toList();
        List<Map.Entry<String, Long>> leaderboard = cantidadHechosSegunParametro(hechos, hecho -> String.valueOf(hecho.getFechaHecho().getHour()));
        return escribirCSV(leaderboard, "Horario", csvs[3]);
    }

    public String solicitudesSpamCSV(){
        List<SolicitudDeEliminacion> solicitudes = getSolicitudesFromAgregador();
        Integer cantTotal = solicitudes.size();
        Long cantSpam = solicitudes
            .stream()
            .filter(solicitud -> solicitud.getEstado().equals(EstadoSolicitud.RECHAZADA_POR_SPAM))
            .count();

        String[] headers = {"Cantidad total", "Spam"};
        String[] data = { cantTotal.toString(), cantSpam.toString() };

        ArrayList<String[]> arr = new ArrayList<>();
        arr.add(headers);
        arr.add(data);

        String path ="../" + csvs[4];
        CSVReader.crear(path, arr);
        return path;
    }

    //--------------------------------------------------------- privados ---------------------------------------------------------//

    private <TipoKey> String escribirCSV(List<Map.Entry<TipoKey, Long>> data, String headerName, String path) {
        ArrayList<String[]> leaderboardStr = entryListToStringArray(data);
        String[] headers = { headerName, "Cantidad de hechos" };
        leaderboardStr.add(0, headers);
        CSVReader.crear( "../" + path, leaderboardStr);
        return path;
    }
    
    private <TipoKey> ArrayList<String[]> entryListToStringArray(List<Map.Entry<TipoKey, Long>> entryList) {
        ArrayList<String[]> arrayList = new ArrayList<>();
        entryList.forEach(entry -> {
            String[] str = new String[2];
            str[0] = entry.getKey().toString();
            str[1] = entry.getValue().toString();
            arrayList.add(str);
        });
        return arrayList;
    }

    private <TipoRetorno> List<Map.Entry<TipoRetorno, Long>> cantidadHechosSegunParametro(List <Hecho> hechos, Function<Hecho, TipoRetorno> criterio) {
        return hechos.stream()
                .collect(Collectors.groupingBy(criterio, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();
    }


    private <TipoRetorno> TipoRetorno masHechosSegunParametro(List<Hecho> hechos, Function<Hecho, TipoRetorno> criterio) {
        return cantidadHechosSegunParametro(hechos, criterio)
                .stream()
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String provinciaConMasHechos(List<Hecho> hechos) {
        return masHechosSegunParametro(hechos, Hecho::getProvincia);
    }

    private List<Hecho> getHechosFromAgregador() {
        return servicioAgregador.get()
            .uri("/api/hechos")
            .retrieve()
            .bodyToFlux(HechoInputDTO.class)
            .map(this::HechoDtoToHecho)
            .collectList()
            .block();
    }

    private List<Hecho> getHechosDeColeccionFromAgregador(String coleccion_id) {
        return servicioAgregador.get()
            .uri("/api/colecciones/%s/hechos".formatted(coleccion_id))
            .retrieve()
            .bodyToFlux(HechoInputDTO.class)
            .map(this::HechoDtoToHecho)
            .collectList()
            .block();
    }

    private List<SolicitudDeEliminacion> getSolicitudesFromAgregador() {
        return servicioAgregador.get()
            .uri("/api/solicitudes")
            .retrieve()
            .bodyToFlux(SolicitudDeEliminacionInputDTO.class)
            .map(this::solicitudDTOtoSolicitud)
            .collectList()
            .block();
    }


    private Hecho HechoDtoToHecho(HechoInputDTO h) {
        Set<Etiqueta> hashDeEtiquteas = h.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toSet());
        List<SolicitudDeEliminacion> solicitudesDeEliminacion = h.getSolicitudesDeEliminacion().stream()
            .map(this::solicitudDTOtoSolicitud)
            .toList();

        return Hecho.builder()
            .id(h.getId())
            .titulo(h.getTitulo())
            .categoria(h.getCategoria())
            .provincia(h.getMunicipio().getProvincia().getNombre())
            .municipio(h.getMunicipio().getNombre())
            .descripcion(h.getDescripcion())
            .origen(h.getOrigen())
            .lugarAcontecimiento(h.getLugarAcontecimiento())
            .fechaHecho(h.getFechaHecho())
            .fechaDeCarga(h.getFechaDeCarga())
            .etiquetas(hashDeEtiquteas)
            .contenidosMultimedia(h.getContenidosMultimedia())
            .contribuyente(h.getContribuyente())
            .build();
    }

    private SolicitudDeEliminacion solicitudDTOtoSolicitud(SolicitudDeEliminacionInputDTO solicitud) {
        return SolicitudDeEliminacion.builder()
            .id(solicitud.getId())
            .solicitante(solicitud.getSolicitante())        // En este servicio, el solicitante se maneja con un Long
            .hechoId(solicitud.getHechoId())
            .descripcion(solicitud.getDescripcion())
            .estado(solicitud.getEstado())
            .fechaDeResolucion(solicitud.getFechaDeResolucion())
            .fechaDeCarga(solicitud.getFechaDeCarga())
            .administradorQueResolvio(solicitud.getAdministradorQueResolvio())
            .build();
    }

}