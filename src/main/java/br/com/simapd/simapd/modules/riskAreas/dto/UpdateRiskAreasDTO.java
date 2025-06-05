package br.com.simapd.simapd.modules.riskAreas.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRiskAreasDTO {

  @Size(max = 100, message = "Name must not exceed 100 characters")
  private String name;

  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  @Size(max = 64, message = "Latitude must not exceed 64 characters")
  private String latitude;

  @Size(max = 64, message = "Longitude must not exceed 64 characters")
  private String longitude;
}
