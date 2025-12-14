package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.converters.AlgoritmoDeConsensoConverter;
import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionConHechosCuradosOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionConHechosOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.entities.consenso.AlgoritmoDeConsenso;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IFuentesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.models.specifications.HechoSpecs;
import ar.utn.ba.ddsi.services.IColeccionesService;
import ar.utn.ba.ddsi.services.IHechosService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    public ColeccionesService(IColeccionesRepository coleccionesRepository, IHechosService hechosService, ApplicationEventPublisher applicationEventPublisher, IFuentesRepository fuentesRepository) {
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
    public ColeccionOutputDTO buscarPorId(String identificador){
        return ColeccionOutputDTO.fromEntity(coleccionesRepository.findByIdentificador(identificador));
    }


    @Override
    public List<ColeccionConHechosCuradosOutputDTO> buscarTodosConHechosCurados(){
        return coleccionesRepository
                .findAll()
                .stream()
                .map(this::coleccionConHechosCuradosOutputDTO)
                .toList();

    }

    @Override
    public Page<HechoOutputDTO> buscarHechosPorColeccion(String identificador, Map<String, String> params, Pageable pageable) {
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        Page<HechoOutputDTO> paginaDTO = hechosService.obtenerPorColeccion(coleccion.getId(), params, pageable)
                .map(HechoOutputDTO::fromEntity);
        return paginaDTO;
    }

    @Override
    public ColeccionConHechosOutputDTO buscarColeccionConHechos(String identificador, Map<String, String> params, Pageable pageable) {
        Coleccion coleccion = coleccionesRepository.findByIdentificador(identificador);
        if (coleccion == null) {
            throw new EntityNotFoundException("Colección no encontrada");
        }
        Page<HechoOutputDTO> paginaDTO = buscarHechosPorColeccion(identificador, params, pageable);
        return ColeccionConHechosOutputDTO.fromEntity(coleccion, paginaDTO);
    }


    @Override
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO input){
        Coleccion coleccion = input.toEntity();
        cargarFuentesDeColeccion(coleccion);
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
        Coleccion coleccionAModificar = coleccionesRepository.findByIdentificador(coleccion.getIdentificador());
        coleccionAModificar.editar(coleccion);
        cargarFuentesDeColeccion(coleccionAModificar);
        applicationEventPublisher.publishEvent(new CriterioCambiadoEvent(coleccionAModificar));
        coleccionesRepository.save(coleccionAModificar);
        return ColeccionOutputDTO.fromEntity(coleccionAModificar);
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

        coleccion.setFuentes(fuentes);
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

    @Override
    public List<FuenteDTO> buscarFuentes(){
        return fuentesRepository.findAll()
                .stream().map(FuenteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColeccionOutputDTO> buscarUltimasColecciones (LocalDateTime fecha, Integer cantidad_colecciones_destacadas){
        // PageRequest.of(numero_pagina, tamaño_pagina)
        Pageable pageable = PageRequest.of(0, cantidad_colecciones_destacadas);

        List<Coleccion> coleccionesEncontradas = coleccionesRepository.findByFechaDeCreacionLessThanEqualOrderByFechaDeCreacionDesc(fecha, pageable);

        return coleccionesEncontradas.stream()
            .map(ColeccionOutputDTO::fromEntity).toList();
    }

    @Override
    public List<String> buscarIdentificadores(){
        return coleccionesRepository.findAll()
                .stream().map(Coleccion::getIdentificador)
                .collect(Collectors.toList());
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


    // Reemplaza las fuentes de la colección por sus versiones de la DB.
    // Esto modifica a la colección recibida por parámetro
    private void cargarFuentesDeColeccion(Coleccion coleccion){
        List<Fuente> fuentesTruchas = coleccion.getFuentes();
        List<Long> fuentesIds = fuentesTruchas.stream().map(Fuente::getId).toList();
        List<Fuente> fuentesPosta = fuentesRepository.findAllByIdIn(fuentesIds);
        coleccion.setFuentes(fuentesPosta);
    }

    //  -------------------------------------------- Métodos de conversión ------------------------------------------------- //

    private ColeccionConHechosCuradosOutputDTO coleccionConHechosCuradosOutputDTO(Coleccion coleccion){
        ColeccionConHechosCuradosOutputDTO dto = new ColeccionConHechosCuradosOutputDTO();

        dto.setIdentificador(coleccion.getIdentificador());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setHechos(coleccion.getHechosConsensuados().stream().map(HechoOutputDTO::fromEntity).toList());
        dto.setFuentes(coleccion.getFuentes().stream().map(FuenteDTO::fromEntity).toList());
        return dto;
    }

    private AlgoritmoDeConsenso obtenerAlgoritmoDeConsenso(String algoritmoDeConsenso){         // no devuelve una interfaz OJO
        return new AlgoritmoDeConsensoConverter().convertToEntityAttribute(algoritmoDeConsenso);
    }
}
