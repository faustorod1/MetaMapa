package ar.utn.ba.ddsi.models.dtos.external;

import ar.utn.ba.ddsi.models.entities.Contribuyente;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ContribuyenteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String fechaDeNacimiento;

    public Contribuyente toEntity() {
        return new Contribuyente(
                this.getId(),
                this.getNombre(), this.getApellido(),
                LocalDate.parse(this.getFechaDeNacimiento(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public static ContribuyenteDTO fromEntity(Contribuyente contribuyente) {
        ContribuyenteDTO dto = new ContribuyenteDTO();
        dto.setId(contribuyente.getId());
        dto.setNombre(contribuyente.getNombre());
        dto.setApellido(contribuyente.getApellido());
        dto.setFechaDeNacimiento(contribuyente.getFechaNacimiento().toString());
        return dto;
    }
}
