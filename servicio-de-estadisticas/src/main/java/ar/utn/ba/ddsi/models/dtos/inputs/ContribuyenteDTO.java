package ar.utn.ba.ddsi.models.dtos.inputs;

import lombok.Data;

@Data
public class ContribuyenteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String fechaDeNacimiento;
}
