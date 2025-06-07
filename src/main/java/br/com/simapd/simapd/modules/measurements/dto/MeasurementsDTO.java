package br.com.simapd.simapd.modules.measurements.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MeasurementsDTO {

  private String id;

  private Integer type;

  private String value;

  private Integer riskLevel;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime measuredAt;

  private String sensorId;

  private String areaId;
}
