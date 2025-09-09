package ar.utn.ba.ddsi.models.dto.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContribuyenteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;
}
