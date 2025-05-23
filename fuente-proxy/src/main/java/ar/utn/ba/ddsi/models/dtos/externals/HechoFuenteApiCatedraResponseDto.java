package ar.utn.ba.ddsi.models.dtos.externals;

import lombok.Data;

import java.util.List;

@Data
public class HechoFuenteApiCatedraResponseDto {
  private Integer current_page;
  private List<HechoDTO> data;
  private String first_page_url;
  private Integer from;
  private String last_page;
  private String last_page_url;
  private String next_page_url;
  private String path;
  private Integer per_page;
  private String prev_page_url;
  private Integer to;
  private Integer total;
}
