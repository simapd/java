package br.com.simapd.simapd.modules.riskAreas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados de uma área de risco monitorada pelo sistema")
public class RiskAreasDTO {

  @Schema(description = "ID único da área de risco", example = "cm123456789012345678901234")
  private String id;

  @NotNull(message = "Name is required")
  @Size(max = 100, message = "Name must not exceed 100 characters")
  @Schema(description = "Nome identificador da área de risco", example = "Zona Industrial Norte")
  private String name;

  @Size(max = 500, message = "Description must not exceed 500 characters")
  @Schema(description = "Descrição detalhada da área e seus riscos", example = "Área industrial com risco de vazamentos químicos e contaminação do solo")
  private String description;

  @NotNull(message = "Latitude is required")
  @Size(max = 64, message = "Latitude must not exceed 64 characters")
  @Schema(description = "Coordenada de latitude da área", example = "-23.550520")
  private String latitude;

  @NotNull(message = "Longitude is required")
  @Size(max = 64, message = "Longitude must not exceed 64 characters")
  @Schema(description = "Coordenada de longitude da área", example = "-46.633308")
  private String longitude;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "Data e hora de criação da área", example = "2024-01-15 14:30")
  private LocalDateTime createdAt;
}
