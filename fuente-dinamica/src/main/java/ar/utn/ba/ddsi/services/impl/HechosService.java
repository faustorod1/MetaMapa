package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dto.input.EtiquetaDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ar.utn.ba.ddsi.models.entities.OrigenHecho.CONTRIBUYENTE;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private String imagenesFolder;

    public HechosService(IHechosRepository hechosRepository,  @Value("${imagenes.folder}") String imagenesFolder) {
        this.hechosRepository = hechosRepository;
        this.imagenesFolder = imagenesFolder;
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
            List<String> paths = new ArrayList<>();
            for (MultipartFile archivo : imagenes) {
                if (archivo.isEmpty()) continue;

                String nombreArchivo = archivo.getOriginalFilename();
                String pathArchivo = this.imagenesFolder + File.separator + nombreArchivo;
                File archivoDestino = new File(pathArchivo);

                try {
                    archivo.transferTo(archivoDestino); // guardado de archivo
                    paths.add(pathArchivo);             // guardamos path en la entidad
                } catch (IOException ex) {
                    throw new RuntimeException("Error guardando archivo: " + nombreArchivo, ex);
                }
            }
            hecho.setContenidosMultimedia(paths);
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
public void update(Hecho h, Hecho hViejo){
    hViejo.setFechaUltimaActualizacion(h.getFechaUltimaActualizacion());
    hViejo.setFechaDeCarga(h.getFechaDeCarga());
    hViejo.setFechaHecho(h.getFechaHecho());
    hViejo.setLugarAcontecimiento(h.getLugarAcontecimiento());
    hViejo.setDescripcion(h.getDescripcion());
    hViejo.setCategoria(h.getCategoria());
    hViejo.setEtiquetas(h.getEtiquetas());
    hViejo.setTitulo(h.getTitulo());
    hViejo.setContenidosMultimedia(h.getContenidosMultimedia());
    hechosRepository.save(hViejo);
}


// --- Conversiones DTO ----------------------------------------------------------------------------------------------


public Hecho DTOToHecho (HechoInputDTO hechoInputDTO){      // Al guardarse el hecho por 1era vez: fechaDeCarga == lastUpdate
    Hecho hecho = Hecho.builder()
            .titulo(hechoInputDTO.getTitulo())
            .descripcion(hechoInputDTO.getDescripcion())
            .categoria(hechoInputDTO.getCategoria())
            .lugarAcontecimiento(new Coordenada(hechoInputDTO.getLatitud(), hechoInputDTO.getLongitud()))
            .fechaHecho(hechoInputDTO.getFechaHecho().atStartOfDay())
            .eliminado(false)
            .contribuyenteId(hechoInputDTO.getContribuyenteId())
            .build();
    if (hechoInputDTO.getEtiquetas() != null){
        hecho.setEtiquetas(hechoInputDTO.getEtiquetas().stream().map(EtiquetaDTO::getNombre).collect(Collectors.toSet()));
    }
    return hecho;
}

public HechoOutputDTO hechoToDTO (Hecho hecho){
    return HechoOutputDTO.builder()
            .titulo(hecho.getTitulo())
            .descripcion(hecho.getDescripcion())
            .categoria(hecho.getCategoria())
            .contenidosMultimedia(hecho.getContenidosMultimedia())
            .lugarAcontecimiento(hecho.getLugarAcontecimiento())
            .fechaHecho(hecho.getFechaHecho())
            .fechaDeCarga(hecho.getFechaDeCarga())
            .fechaUltimaActualizacion(hecho.getFechaUltimaActualizacion())
            .origen(CONTRIBUYENTE)
            .eliminado(hecho.isEliminado())
            //.contribuyente(this.contribuyenteToDTO(hecho.getContribuyente()))
            .etiquetas(hecho.getEtiquetas())
            .id(hecho.getId())
            .tipoDeFuente("dinamica")
            .build();
}

}