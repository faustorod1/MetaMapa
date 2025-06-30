package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.FiltroInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.FiltroOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IColeccionesService;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private IColeccionesRepository coleccionesRepository;
    private IHechosService hechosService;

    @Autowired
    public ColeccionesService(IColeccionesRepository coleccionesRepository, IHechosService hechosService, IHechosRepository hechosRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // -------------------------------------------- Métodos expuestos al controller ----------------------------------------- //

    @Override
    public List<ColeccionOutputDTO> buscarTodos() {
        return coleccionesRepository
                .findAll()
                .stream()
                .map(this::coleccionOutputDTO)
                .toList();
    }

    @Override
    public Mono<List<HechoOutputDTO>> buscarHechosPorColeccion(String identificador, String modo, Map<String, String> params) {
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        Criterio filtrosDeUsuario = new Criterio(params);

        /*
        if(modo == "curada"){
            Mono<List<Hecho>> hechosColeccion = Mono.fromCallable(coleccion::getHechosConsensuados);
        }else if(modo == "irrestricta") {
            // Esto convierte la List a Mono<List> para poder juntarla con el otro Mono, el de MetaMapa
            Mono<List<Hecho>> hechosColeccion = Mono.fromCallable(coleccion::getHechos);
        }
        */
        // Esto convierte la List a Mono<List> para poder juntarla con el otro Mono, el de MetaMapa
        Mono<List<Hecho>> hechosColeccion = Mono.fromCallable(coleccion::getHechos);

        // Trae los hechos de fuentes MetaMapa y los pasa por el filtro de la colección
        Mono<List<Hecho>> hechosMetaMapa = hechosService.getFromMetaMapa().map(coleccion::aplicarFiltros);

        // Junta los hechos de la colección (ya persistidos localmente) con los MetaMapa obtenidos recién
        Mono<List<Hecho>> todos = Mono.zip(hechosColeccion, hechosMetaMapa)
                .map(tuple ->
                        Stream.concat(tuple.getT1().stream(), tuple.getT2().stream()).toList()
                )
                .map(filtrosDeUsuario::aplicarA);



        /*
        if("consensuado") {
            return todos.intersection(coleccion.getHechosConsenusados())
        }else{ //irrestricto
            return todos.map(list -> list.stream().map(hechosService::hechoOutputDTO).toList());
        }
        */

        return todos.map(list -> list.stream().map(hechosService::hechoOutputDTO).toList());

    }


    @Override
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO input){
        Coleccion coleccion = inputDTOToColeccion(input);
        applicationEventPublisher.publishEvent(new CriterioCambiadoEvent(coleccion));
        coleccionesRepository.save(coleccion);
        return coleccionOutputDTO(coleccion);
    }

    @Override
    public void eliminarColeccion(String identificador){
        coleccionesRepository.delete(identificador);
    }

    @Override
    public ColeccionOutputDTO updateColeccion(ColeccionInputDTO input){
        Coleccion coleccion = inputDTOToColeccion(input);
        coleccionesRepository.save(coleccion);
        return coleccionOutputDTO(coleccion);
    }


    @Override
    public ColeccionOutputDTO updateCriterio(String identificador, CriterioInputDTO criterioInputDTO){
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        Criterio criterio = inputDTOToCriterio(criterioInputDTO);

        coleccion.setCriterioDePertenencia(criterio);

        applicationEventPublisher.publishEvent(new CriterioCambiadoEvent(coleccion));
        return coleccionOutputDTO(coleccion);
   }

    @Override
    public ColeccionOutputDTO updateFuentes(String identificador, List<String> fuentes){
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        List<String> fuentesPrevias = coleccion.getFuentes();

        List<String> fuentesCambiadas = calcularDiferenciaFuentes(fuentes, fuentesPrevias);

        coleccion.setFuentes(fuentes);     // TODO: acá no sería fuentes?
        applicationEventPublisher.publishEvent(new FuentesCambiadasEnColeccionEvent(coleccion, fuentesCambiadas));

        return coleccionOutputDTO(coleccion);
    }


    //  -------------------------------------------- Métodos de internos ------------------------------------------------- //

    public void consensuarColecciones(){
        this.coleccionesRepository.findAll().forEach(Coleccion::consensuarHechos);
    }

    private List<String> calcularDiferenciaFuentes(List<String> actuales, List<String> previas) {
        Set<String> setActual = new HashSet<>(actuales); // [a,b,c]
        Set<String> setPrevio = new HashSet<>(previas); // [a,d,e]

        Set<String> diferencia = new HashSet<>(setActual);
        diferencia.addAll(setPrevio); // [a, a , b, c, d, e]

        Set<String> interseccion = new HashSet<>(setActual);
        interseccion.retainAll(setPrevio); // [a]

        diferencia.removeAll(interseccion); // [b, c, d, e]

        return new ArrayList<>(diferencia);
    }

    //  -------------------------------------------- Métodos de conversión ------------------------------------------------- //


    private ColeccionOutputDTO coleccionOutputDTO(Coleccion coleccion) {
        ColeccionOutputDTO dto = new ColeccionOutputDTO();

        dto.setIdentificador(coleccion.getIdentificador());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setCriterioDePertenencia(criterioOutputDTO(coleccion.getCriterioDePertenencia()));
        return dto;
    }

    private CriterioOutputDTO criterioOutputDTO(Criterio criterio) {
        CriterioOutputDTO dto = new CriterioOutputDTO();
        dto.setFiltros(
                criterio.getFiltros()
                .stream()
                .map(this::filtroOutputDTO)
                .toList()
        );
        return dto;
    }


    private Coleccion inputDTOToColeccion(ColeccionInputDTO input){
        return new Coleccion(
                input.getIdentificador(),
                input.getTitulo(),
                input.getDescripcion(),
                inputDTOToCriterio(input.getCriterioDePertenencia()),
                input.getFuentes()
        );
    }

    private Criterio inputDTOToCriterio(CriterioInputDTO input){
        Criterio criterio = new Criterio();
        input.getFiltros()
                .stream()
                .map(this::inputDTOToFiltro)
                .forEach(criterio::addFiltro);
        return criterio;
    }

    // Bajo la alfombra...  (╯°□°)╯︵ ┻━┻

    private FiltroOutputDTO filtroOutputDTO(Filtro filtro) {
        FiltroOutputDTO dto = new FiltroOutputDTO();
        if (filtro instanceof FiltroPorTitulo f) {
            dto.setTipoDeFiltro("titulo");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("titulo", f.getTitulo());
        } else if (filtro instanceof FiltroPorDescripcion f) {
            dto.setTipoDeFiltro("descripcion");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("descripcion", f.getDescripcion());
        } else if (filtro instanceof FiltroPorCategoria f) {
            dto.setTipoDeFiltro("categoria");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("categoria", f.getCategoria());
        } else if (filtro instanceof FiltroPorUbicacion f) {
            dto.setTipoDeFiltro("ubicacion");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("lugar", f.getLugar().comoArray());
        } else if (filtro instanceof FiltroPorFechaHecho f) {
            dto.setTipoDeFiltro("fechaHecho");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("desde", f.getDesde());
            dto.getParametros().put("hasta", f.getHasta());
        } else if (filtro instanceof FiltroPorFechaDeCarga f) {
            dto.setTipoDeFiltro("fechaDeCarga");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("desde", f.getDesde());
            dto.getParametros().put("hasta", f.getHasta());
        } else if (filtro instanceof FiltroPorEliminados f) {
            dto.setTipoDeFiltro("eliminados");
            dto.setParametros(new HashMap<>());
        } else {
            throw new RuntimeException("Tipo de filtro no encontrado (╯°□°)╯︵ ┻━┻");
        }

        return dto;
    }

    private Filtro inputDTOToFiltro(FiltroInputDTO input){
        if(input.getTipoDeFiltro().equals("titulo")){
            return new FiltroPorTitulo((String) input.getParametros().get("titulo"));
        }else if(input.getTipoDeFiltro().equals("descripcion")){
            return new FiltroPorDescripcion((String) input.getParametros().get("descripcion"));
        }else if(input.getTipoDeFiltro().equals("categoria")){
            return new FiltroPorCategoria(new Categoria((String) input.getParametros().get("categoria")));
        }else if(input.getTipoDeFiltro().equals("ubicacion")){
            Double latitud = (Double) input.getParametros().get("latitud");
            Double longitud = (Double) input.getParametros().get("longitud");
            return new FiltroPorUbicacion(new Coordenada(latitud, longitud));
        }else if(input.getTipoDeFiltro().equals("fechaHecho")){
            LocalDate desde = LocalDate.parse((String) input.getParametros().get("desde"));
            LocalDate hasta = LocalDate.parse((String) input.getParametros().get("hasta"));
            return new FiltroPorFechaHecho(desde,hasta);
        }else if(input.getTipoDeFiltro().equals("fechaDeCarga")){
            return new FiltroPorFechaDeCarga((String) input.getParametros().get("desde"),(String) input.getParametros().get("hasta"));
        }else{
            throw new RuntimeException("Tipo de filtro no encontrado (╯°□°)╯︵ ┻━┻");
        }
    }

}
