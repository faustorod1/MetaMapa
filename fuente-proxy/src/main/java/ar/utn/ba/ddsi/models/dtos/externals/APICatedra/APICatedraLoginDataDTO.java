package ar.utn.ba.ddsi.models.dtos.externals.APICatedra;

import lombok.Data;

@Data
public class APICatedraLoginDataDTO {
  String access_token;
  String token_type;
  APICatedraLoginDataUserDTO user;
}