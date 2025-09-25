package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.converters.AlgoritmoDeConsensoConverter;
import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.FiltroInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionConHechosOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.FiltroOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.entities.consenso.AlgoritmoDeConsenso;
import ar.utn.ba.ddsi.models.entities.filtros.*;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IFuentesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IColeccionesService;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private IColeccionesRepository coleccionesRepository;
    private IHechosService hechosService;
    private IFuentesRepository fuentesRepository;

    @Autowired
    public ColeccionesService(IColeccionesRepository coleccionesRepository, IHechosService hechosService, IHechosRepository hechosRepository, ApplicationEventPublisher applicationEventPublisher, IFuentesRepository fuentesRepository) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.fuentesRepository = fuentesRepository;
    }

    // -------------------------------------------- Métodos expuestos al controller ----------------------------------------- //

    @Override
    public List<ColeccionOutputDTO> buscarTodos() {
        return coleccionesRepository
                .findAll()
                .stream()
                .map(ColeccionOutputDTO::fromEntity)
                .toList();
    }

    @Override
    public List<ColeccionConHechosOutputDTO> buscarTodosConHechos() {
        return coleccionesRepository
            .findAll()
            .stream()
            .map(this::coleccionConHechosOutputDTO)
            .toList();
    }

    @Override
    public Mono<List<HechoOutputDTO>> buscarHechosPorColeccion(String identificador, Map<String, String> params) {
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        Criterio filtrosDeUsuario = new Criterio(params);

        Mono<List<Hecho>> hechosColeccion;
        String modo = params.getOrDefault("modo", "curada");

        // PASO 1: segun el modo, convierte la List a Mono<List> para poder juntarla con el otro Mono, el de MetaMapa
        if("irrestricta".equalsIgnoreCase(modo)){
            hechosColeccion = Mono.fromCallable(coleccion::getHechos);
        }else {
            // Si no es irrestricta, se toma como curada
            hechosColeccion = Mono.fromCallable(coleccion::getHechosConsensuados);
        }

        // PASO 2: trae los hechos de fuentes MetaMapa
        Mono<List<Hecho>> hechosMetaMapa = hechosService.getFromMetaMapa();

        // Reemplaza de la colección (ya persistida localmente) con los MetaMapa obtenidos recién
        // Si hay algún hecho de fuente MetaMapa que NO teníamos en el agregador (porque se agregó a esa
        // fuente MetaMapa después de la última actualización), ese hecho NO se incluye entre los hechos
        // que devolvemos acá, ya que no sabemos si debería pertenecer a la colección.
        Mono<List<Hecho>> todos = Mono.zip(hechosColeccion, hechosMetaMapa)
                .map(tuple -> {
                        List<Hecho> listaMetaMapa = tuple.getT2();
                        Map<IdExterno, Hecho> hechosPorId = tuple.getT1().stream()
                            .collect(Collectors.toMap(Hecho::getIdExterno, h -> h));

                        for (Hecho hechoMetaMapa : listaMetaMapa) {
                            if (hechosPorId.containsKey(hechoMetaMapa.getIdExterno())) {
                                hechoMetaMapa.setId(hechosPorId.get(hechoMetaMapa.getIdExterno()).getId());
                                hechosPorId.put(hechoMetaMapa.getIdExterno(), hechoMetaMapa);
                            }
                        }
                        return new ArrayList<>(hechosPorId.values());
                    }
                )
                .map(filtrosDeUsuario::aplicarA);

        // PASO 3: convertir
        return todos.map(list -> list.stream().map(HechoOutputDTO::fromEntity).toList());
    }


    @Override
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO input){
        Coleccion coleccion = input.toEntity();
        applicationEventPublisher.publishEvent(new CriterioCambiadoEvent(coleccion));
        coleccionesRepository.save(coleccion);
        return ColeccionOutputDTO.fromEntity(coleccion);
    }

    @Override
    public void eliminarColeccion(String identificador){
        coleccionesRepository.deleteByIdentificador(identificador);
    }

    @Override
    public ColeccionOutputDTO updateColeccion(ColeccionInputDTO input){
        Coleccion coleccion = input.toEntity();
        coleccionesRepository.save(coleccion);
        return ColeccionOutputDTO.fromEntity(coleccion);
    }


    @Override
    public ColeccionOutputDTO updateCriterio(String identificador, CriterioInputDTO criterioInputDTO){
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        Criterio criterio = criterioInputDTO.toEntity();

        coleccion.setCriterioDePertenencia(criterio);

        applicationEventPublisher.publishEvent(new CriterioCambiadoEvent(coleccion));
        return ColeccionOutputDTO.fromEntity(coleccion);
   }

    @Override
    public ColeccionOutputDTO updateFuentes(String identificador, List<Long> fuentesIds){
        List<Fuente> fuentes = fuentesRepository.findAllByIdIn(fuentesIds);
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        List<Fuente> fuentesPrevias = coleccion.getFuentes();

        List<Fuente> fuentesCambiadas = calcularDiferenciaFuentes(fuentes, fuentesPrevias);

        coleccion.setFuentes(fuentes);     // TODO: acá no sería fuentes?
        applicationEventPublisher.publishEvent(new FuentesCambiadasEnColeccionEvent(coleccion, fuentesCambiadas));

        return ColeccionOutputDTO.fromEntity(coleccion);
    }

    @Override
    public ColeccionOutputDTO updateConsenso (String identificador, String tipoDeConsenso){
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);

        AlgoritmoDeConsenso algoritmoDeConsenso = obtenerAlgoritmoDeConsenso(tipoDeConsenso);
        coleccion.setAlgoritmoDeConsenso(algoritmoDeConsenso);

        return ColeccionOutputDTO.fromEntity(coleccion);
    }


    //  -------------------------------------------- Métodos de internos ------------------------------------------------- //

    public void consensuarColecciones(){
        this.coleccionesRepository.findAll().forEach(Coleccion::consensuarHechos);
    }

    private List<Fuente> calcularDiferenciaFuentes(List<Fuente> actuales, List<Fuente> previas) {
        Set<Fuente> setActual = new HashSet<>(actuales); // [a,b,c]
        Set<Fuente> setPrevio = new HashSet<>(previas); // [a,d,e]

        Set<Fuente> diferencia = new HashSet<>(setActual);
        diferencia.addAll(setPrevio); // [a, a , b, c, d, e]

        Set<Fuente> interseccion = new HashSet<>(setActual);
        interseccion.retainAll(setPrevio); // [a]

        diferencia.removeAll(interseccion); // [b, c, d, e]

        return new ArrayList<>(diferencia);
    }

    //  -------------------------------------------- Métodos de conversión ------------------------------------------------- //


    private ColeccionConHechosOutputDTO coleccionConHechosOutputDTO(Coleccion coleccion){
        ColeccionConHechosOutputDTO dto = new ColeccionConHechosOutputDTO();

        dto.setIdentificador(coleccion.getIdentificador());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setHechos(coleccion.getHechos());
        dto.setFuentes(coleccion.getFuentes());
        return dto;
    }

    private AlgoritmoDeConsenso obtenerAlgoritmoDeConsenso(String algoritmoDeConsenso){         // no devuelve una interfaz OJO
        return new AlgoritmoDeConsensoConverter().convertToEntityAttribute(algoritmoDeConsenso);
    }
}
