package ar.utn.ba.ddsi.models.dtos.externals.APICatedra;

import lombok.Data;

@Data
public class APICatedraLogInDTO {
  private Boolean error;
  private String message;
  private APICatedraLoginDataDTO data;
}