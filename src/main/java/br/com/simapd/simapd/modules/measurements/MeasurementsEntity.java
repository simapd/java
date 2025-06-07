package br.com.simapd.simapd.modules.measurements;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "measurement")
public class MeasurementsEntity {

  @Id
  @NotBlank(message = "ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for ID")
  private String id;

  @NotNull(message = "Type cannot be null")
  @Column(name = "type", nullable = false)
  private Integer type;

  @NotBlank(message = "Value cannot be blank")
  @Column(name = "value", nullable = false, columnDefinition = "CLOB")
  private String value;

  @NotNull(message = "Risk level cannot be null")
  @Min(value = 1, message = "Risk level must be between 1 and 4")
  @Max(value = 4, message = "Risk level must be between 1 and 4")
  @Column(name = "risk_level", nullable = false)
  private Integer riskLevel;

  @NotNull(message = "Measured at cannot be null")
  @Column(name = "measured_at", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime measuredAt;

  @NotBlank(message = "Sensor ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for sensor")
  @Column(name = "sensor_id", nullable = false)
  private String sensorId;

  @NotBlank(message = "Area ID cannot be blank")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for area")
  @Column(name = "area_id", nullable = false)
  private String areaId;
}
