package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.FiltroOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.impl.HechosRepository;
import ar.utn.ba.ddsi.services.IColeccionesService;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final HechosRepository hechosRepository;
    private IColeccionesRepository coleccionesRepository;
    private IHechosService hechosService;

    @Autowired
    public ColeccionesService(IColeccionesRepository coleccionesRepository, IHechosService hechosService, HechosRepository hechosRepository, ApplicationEventPublisher applicationEventPublisher, HechosRepository hechosRepository) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.hechosRepository = hechosRepository;
    }

    // -------------------------------- Métodos expuestos al controller -----------------------------------------

    @Override
    public List<ColeccionOutputDTO> buscarTodos() {
        return coleccionesRepository
                .findAll()
                .stream()
                .map(this::coleccionOutputDTO)
                .toList();
    }

    @Override
    public Mono<List<HechoOutputDTO>> buscarHechosPorColeccion(String identificador) {
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);

        // Esto convierte la List a Mono<List> para poder juntarla con el otro Mono, el de MetaMapa
        Mono<List<Hecho>> hechosColeccion = Mono.fromCallable(coleccion::getHechos);

        // Trae los hechos de fuentes MetaMapa y los pasa por el filtro de la colección
        Mono<List<Hecho>> hechosMetaMapa = hechosService.getFromMetaMapa().map(coleccion::aplicarFiltros);

        // Junta los hechos de la colección (ya persistidos localmente) con los MetaMapa obtenidos recién
        Mono<List<Hecho>> todos = Mono.zip(hechosColeccion, hechosMetaMapa)
                .map(tuple ->
                        Stream.concat(tuple.getT1().stream(), tuple.getT2().stream()).toList()
                );

        // Falta agregar los hechos a su colección cuando se actualizan
        return todos.map(list -> list.stream().map(hechosService::hechoOutputDTO).toList());
    }

    // TODO: endpoint y ver esto
    @Override
    public void crearColeccion(String identificador, String titulo, String descripcion, Criterio criterioDePertenencia, List<String> fuentes){
        Coleccion coleccion = new Coleccion(identificador, titulo, descripcion, criterioDePertenencia, fuentes);

        List<Hecho> hechos = hechosRepository.findFromFuentes(fuentes);     // Obtenemos los hechos de las fuentes
        coleccion.agregarTandaDeHechos(hechos);                             // Agregamos los hechos a la colección
        coleccion.filtrarHechosPropios(hechos);                             // Tenemos en cuenta su criterio

        coleccionesRepository.save(coleccion);
        applicationEventPublisher.publishEvent(new CriterioCambiadoEvent(coleccion));
    }

    //  -------------------------------------------- Métodos de conversión -------------------------------------------------


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
            throw new RuntimeException("Tipo de filtro no encontrado");
        }

        return dto;
    }




}
