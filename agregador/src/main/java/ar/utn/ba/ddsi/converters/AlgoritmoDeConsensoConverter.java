package ar.utn.ba.ddsi.converters;

import ar.utn.ba.ddsi.models.entities.consenso.AlgoritmoDeConsenso;
import ar.utn.ba.ddsi.models.entities.consenso.ConsensoAbsoluta;
import ar.utn.ba.ddsi.models.entities.consenso.ConsensoMayoriaSimple;
import ar.utn.ba.ddsi.models.entities.consenso.ConsensoMultiplesMenciones;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AlgoritmoDeConsensoConverter implements AttributeConverter<AlgoritmoDeConsenso, String> {
  @Override
  public String convertToDatabaseColumn(AlgoritmoDeConsenso algoritmo) {
    if (algoritmo instanceof ConsensoAbsoluta) {
      return "absoluta";
    }else if (algoritmo instanceof ConsensoMayoriaSimple) {
      return "mayoriaSimple";
    }else if (algoritmo instanceof ConsensoMultiplesMenciones) {
      return "multiplesMenciones";
    }else{
      return null;
    }
  }

  @Override
  public AlgoritmoDeConsenso convertToEntityAttribute(String nombreAlgoritmo) {
    if("absoluta".equalsIgnoreCase(nombreAlgoritmo)){
      return new ConsensoAbsoluta();
    }else if("mayoriaSimple".equalsIgnoreCase(nombreAlgoritmo)){
      return new ConsensoMayoriaSimple();
    }else if("multiplesMenciones".equalsIgnoreCase(nombreAlgoritmo)){
      return new ConsensoMultiplesMenciones();
    }else{
      return null;
    }
  }
}
