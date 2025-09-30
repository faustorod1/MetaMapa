package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.TipoDeFuente;
import lombok.Data;

import java.io.Serializable;

@Data
public class FuenteDTO implements Serializable {
    Long id;
    TipoDeFuente tipoDeFuente;
    Long subfuenteId;

    public Fuente toEntity(){
        return Fuente.builder()
                .id(this.id)
                .tipoDeFuente(this.tipoDeFuente)
                .subfuenteId(this.subfuenteId)
                .build();
    }

    public static FuenteDTO fromEntity(Fuente entity) {
        FuenteDTO fuenteDTO = new FuenteDTO();
        fuenteDTO.id = entity.getId();
        fuenteDTO.tipoDeFuente = entity.getTipoDeFuente();
        fuenteDTO.subfuenteId = entity.getSubfuenteId();
        return fuenteDTO;
    }
}
