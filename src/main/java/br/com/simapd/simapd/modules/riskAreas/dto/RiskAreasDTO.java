package br.com.simapd.simapd.modules.riskAreas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RiskAreasDTO {

  private String id;

  @NotNull(message = "Name is required")
  @Size(max = 100, message = "Name must not exceed 100 characters")
  private String name;

  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  @NotNull(message = "Latitude is required")
  @Size(max = 64, message = "Latitude must not exceed 64 characters")
  private String latitude;

  @NotNull(message = "Longitude is required")
  @Size(max = 64, message = "Longitude must not exceed 64 characters")
  private String longitude;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;
}
