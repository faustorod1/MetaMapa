package ar.utn.ba.ddsi.converters;

import ar.utn.ba.ddsi.models.dto.input.CategoriaDTO;
import ar.utn.ba.ddsi.services.impl.AgregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategoriaConverter implements Converter<String, CategoriaDTO> {

    @Autowired
    private AgregadorService agregadorService; // O como se llame tu servicio

    @Override
    public CategoriaDTO convert(String sourceId) {
        if (sourceId == null || sourceId.isEmpty()) {
            return null;
        }

        try {
            Long id = Long.parseLong(sourceId);
            return agregadorService.pedirCategoriaPorID(id);

        } catch (NumberFormatException e) {
            return null;
        }
    }
}