package ar.utn.ba.ddsi.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEstaticaService {
    void importarCSVs(List<MultipartFile> archivos);
}
