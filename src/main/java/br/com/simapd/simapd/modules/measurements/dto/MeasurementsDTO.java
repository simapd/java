package br.com.simapd.simapd.modules.measurements.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonGetter;

@Data
@Schema(description = "Dados de uma medição ambiental coletada por sensor")
public class MeasurementsDTO {

  @NotBlank(message = "ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for ID")
  @Schema(description = "ID único da medição", example = "cm456789012345678901234567")
  private String id;

  @NotNull(message = "Type cannot be null")
  @Schema(description = "Tipo de medição (1=Temperatura, 2=Umidade, 3=Pressão, 4=Qualidade do Ar)", example = "1")
  private Integer type;

  @NotBlank(message = "Value cannot be blank")
  @Schema(description = "Valor da medição em formato JSON", example = "{\"temperature\": 25.5, \"unit\": \"celsius\"}")
  private String value;

  @JsonGetter("value")
  public JsonNode getValueAsJson() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readTree(value);
    } catch (Exception e) {
      return null;
    }
  }

  @NotNull(message = "Risk level cannot be null")
  @Min(value = 1, message = "Risk level must be between 1 and 4")
  @Max(value = 4, message = "Risk level must be between 1 and 4")
  @Schema(description = "Nível de risco da medição (1=Baixo, 2=Médio, 3=Alto, 4=Crítico)", example = "2")
  private Integer riskLevel;

  @NotNull(message = "Measured at cannot be null")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "Data e hora da medição", example = "2024-01-15 14:30")
  private LocalDateTime measuredAt;

  @NotBlank(message = "Sensor ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for sensor")
  @Schema(description = "ID único do sensor que coletou a medição", example = "cm789012345678901234567890")
  private String sensorId;

  @NotBlank(message = "Area ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for area")
  @Schema(description = "ID da área onde a medição foi coletada", example = "cm123456789012345678901234")
  private String areaId;
}
