package br.com.simapd.simapd.modules.measurements.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MeasurementsDTO {

  private String id;

  private BigDecimal measurementValue;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime measuredAt;

  private String sensorId;
}
