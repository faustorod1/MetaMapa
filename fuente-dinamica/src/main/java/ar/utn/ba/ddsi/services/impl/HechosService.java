package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dto.input.EtiquetaDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.services.internal.ImageUploaderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ar.utn.ba.ddsi.models.entities.OrigenHecho.CONTRIBUYENTE;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ImageUploaderService imageUploaderService;

    public HechosService(IHechosRepository hechosRepository, ImageUploaderService imageUploaderService) {
        this.hechosRepository = hechosRepository;
        this.imageUploaderService = imageUploaderService;
    }


    // --- Métodos expuestos al controller -------------------------------------------------------------------------------


    @Override
    public List<HechoOutputDTO> getAll_DTO() {
        return hechosRepository
                .findAll()
                .stream()
                .map(this::hechoToDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO buscarHechoNoEliminado(Long id) {
        Hecho hecho = hechosRepository.findById(id).orElse(null);
        if (hecho == null || hecho.isEliminado()) {
            throw new EntityNotFoundException("El Hecho con ID: " + id + " está marcado como eliminado.");
        } else {
            return hechoToDTO(hecho);
        }
    }

    @Override
    public List<Long> buscarIdsHechos(){
        return hechosRepository.findAll().stream().filter(hecho -> !hecho.isEliminado()).map(Hecho::getId).collect(Collectors.toList());
    }

    @Override
    public List<HechoOutputDTO> getAllDesde_DTO(LocalDateTime desde) {
        return hechosRepository
                .findAll()
                .stream()
                .filter(hecho -> hecho.getFechaUltimaActualizacion().isAfter(desde))
                .map(this::hechoToDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO crearHecho(HechoInputDTO hechoInputDTO, List<MultipartFile> imagenes) {

        Hecho hecho = this.DTOToHecho(hechoInputDTO);
        hecho.setFechaDeCarga(LocalDateTime.now());
        hecho.setFechaUltimaActualizacion(LocalDateTime.now());

        if (imagenes != null && !imagenes.isEmpty()) {
            List<ContenidoMultimedia> listaMultimedia = new ArrayList<>();

            for (MultipartFile archivo : imagenes) {
                if (archivo.isEmpty()) continue;

                String urlImagen = imageUploaderService.uploadFile(archivo);

                ContenidoMultimedia cm = new ContenidoMultimedia(urlImagen);
                listaMultimedia.add(cm);
            }
            hecho.setContenidosMultimedia(listaMultimedia);
        }

        hechosRepository.save(hecho);
        return this.hechoToDTO(hecho);
    }



@Override
public void marcarComoELiminado(Long id) {
    hechosRepository.marcarComoEliminado(id);
}


// --- Métodos para uso interno --------------------------------------------------------------------------------------


@Override
public Hecho getById(Long id){return hechosRepository.findById(id).orElse(null);}


@Override
public void guardarCambios(Hecho h){
    hechosRepository.save(h);
}


// --- Conversiones DTO ----------------------------------------------------------------------------------------------


public Hecho DTOToHecho (HechoInputDTO hechoInputDTO){      // Al guardarse el hecho por 1era vez: fechaDeCarga == lastUpdate
    Hecho hecho = Hecho.builder()
            .titulo(hechoInputDTO.getTitulo())
            .descripcion(hechoInputDTO.getDescripcion())
            .categoria(hechoInputDTO.getCategoria())
            .lugarAcontecimiento(new Coordenada(hechoInputDTO.getLatitud(), hechoInputDTO.getLongitud()))
            .fechaHecho(hechoInputDTO.getFechaHecho())
            .eliminado(false)
            .contribuyenteId(hechoInputDTO.getContribuyenteId())
            .build();
    if (hechoInputDTO.getEtiquetas() != null){
        hecho.setEtiquetas(hechoInputDTO.getEtiquetas().stream().map(EtiquetaDTO::getNombre).collect(Collectors.toSet()));
    }
    return hecho;
}

public HechoOutputDTO hechoToDTO (Hecho hecho){
    List<String> urlsParaFrontend = new ArrayList<>();
    if (hecho.getContenidosMultimedia() != null) {
        urlsParaFrontend = hecho.getContenidosMultimedia().stream()
            .map(ContenidoMultimedia::getUrl) // Extraemos el string
            .toList();
    }

    return HechoOutputDTO.builder()
            .titulo(hecho.getTitulo())
            .descripcion(hecho.getDescripcion())
            .categoria(hecho.getCategoria())
            .contenidosMultimedia(urlsParaFrontend)
            .lugarAcontecimiento(hecho.getLugarAcontecimiento())
            .fechaHecho(hecho.getFechaHecho())
            .fechaDeCarga(hecho.getFechaDeCarga())
            .fechaUltimaActualizacion(hecho.getFechaUltimaActualizacion())
            .origen(CONTRIBUYENTE)
            .eliminado(hecho.isEliminado())
            .contribuyenteId(hecho.getContribuyenteId())
            .etiquetas(hecho.getEtiquetas())
            .id(hecho.getId())
            .tipoDeFuente("DINAMICA")
            .build();
}

}