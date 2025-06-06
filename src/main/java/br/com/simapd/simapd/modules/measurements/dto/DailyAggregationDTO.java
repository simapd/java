package br.com.simapd.simapd.modules.measurements.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyAggregationDTO {

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  private Double averageValue;

  private Long measurementCount;

  private Double minValue;

  private Double maxValue;

  private Double sumValue;
}