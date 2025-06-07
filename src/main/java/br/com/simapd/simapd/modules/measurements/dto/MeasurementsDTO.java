package br.com.simapd.simapd.modules.measurements.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class MeasurementsDTO {

  @NotBlank(message = "ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for ID")
  private String id;

  @NotNull(message = "Type cannot be null")
  private Integer type;

  @NotBlank(message = "Value cannot be blank")
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
  private Integer riskLevel;

  @NotNull(message = "Measured at cannot be null")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime measuredAt;

  @NotBlank(message = "Sensor ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for sensor")
  private String sensorId;

  @NotBlank(message = "Area ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for area")
  private String areaId;
}
