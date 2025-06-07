package br.com.simapd.simapd.modules.measurements.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados agregados de medições por dia, incluindo estatísticas resumidas")
public class DailyAggregationDTO {

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "Data da agregação", example = "2024-01-15")
  private LocalDate date;

  @Schema(description = "Valor médio das medições do dia", example = "25.5")
  private Double averageValue;

  @Schema(description = "Quantidade total de medições no dia", example = "48")
  private Long measurementCount;

  @Schema(description = "Menor valor registrado no dia", example = "18.2")
  private Double minValue;

  @Schema(description = "Maior valor registrado no dia", example = "32.1")
  private Double maxValue;

  @Schema(description = "Soma de todos os valores do dia", example = "1224.0")
  private Double sumValue;
}