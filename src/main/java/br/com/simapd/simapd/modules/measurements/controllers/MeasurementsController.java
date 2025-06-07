package br.com.simapd.simapd.modules.measurements.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.simapd.simapd.modules.measurements.dto.DailyAggregationDTO;
import br.com.simapd.simapd.modules.measurements.dto.MeasurementsDTO;
import br.com.simapd.simapd.modules.measurements.useCases.MeasurementsCachingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/measurements")
@Tag(name = "Medições", description = "Endpoints para consulta de dados de medições ambientais dos sensores")
@SecurityRequirement(name = "bearerAuth")
public class MeasurementsController {

  @Autowired
  private MeasurementsCachingUseCase measurementsCachingUseCase;

  @GetMapping("/{id}")
  @Operation(
    summary = "Buscar medição por ID",
    description = "Retorna uma medição específica baseada no seu ID único"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Medição encontrada com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MeasurementsDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Medição não encontrada",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<MeasurementsDTO> findById(
    @Parameter(description = "ID único da medição", required = true) @PathVariable String id) {
    Optional<MeasurementsDTO> measurement = measurementsCachingUseCase.findById(id);
    return measurement.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  @Operation(
    summary = "Listar todas as medições",
    description = "Retorna uma lista paginada de todas as medições registradas no sistema"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Lista de medições obtida com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Page.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Page<MeasurementsDTO>> findAll(
    @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
    Page<MeasurementsDTO> measurements = measurementsCachingUseCase.findAll(pageable);
    return ResponseEntity.ok(measurements);
  }

  @GetMapping("/sensor/{sensorId}")
  @Operation(
    summary = "Buscar medições por sensor",
    description = "Retorna todas as medições de um sensor específico"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Medições do sensor obtidas com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = List.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<List<MeasurementsDTO>> findBySensorId(
    @Parameter(description = "ID único do sensor", required = true) @PathVariable String sensorId) {
    List<MeasurementsDTO> measurements = measurementsCachingUseCase.findBySensorId(sensorId);
    return ResponseEntity.ok(measurements);
  }

  @GetMapping("/area/{areaId}")
  @Operation(
    summary = "Buscar medições por área",
    description = "Retorna todas as medições de uma área específica"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Medições da área obtidas com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = List.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<List<MeasurementsDTO>> findByAreaId(
    @Parameter(description = "ID único da área", required = true) @PathVariable String areaId) {
    List<MeasurementsDTO> measurements = measurementsCachingUseCase.findByAreaId(areaId);
    return ResponseEntity.ok(measurements);
  }

  @GetMapping("/filter")
  @Operation(
    summary = "Filtrar medições",
    description = "Retorna medições filtradas por sensor, área, tipo e/ou nível de risco"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Medições filtradas obtidas com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = List.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<List<MeasurementsDTO>> findByFilters(
      @Parameter(description = "ID do sensor (opcional)") @RequestParam(required = false) String sensorId,
      @Parameter(description = "ID da área (opcional)") @RequestParam(required = false) String areaId,
      @Parameter(description = "Tipo de medição (opcional)") @RequestParam(required = false) Integer type,
      @Parameter(description = "Nível de risco (1-4, opcional)") @RequestParam(required = false) Integer riskLevel) {

    List<MeasurementsDTO> measurements = measurementsCachingUseCase
        .findByFilters(sensorId, areaId, type, riskLevel);

    return ResponseEntity.ok(measurements);
  }

  @GetMapping("/daily-aggregation")
  @Operation(
    summary = "Obter agregação diária",
    description = "Retorna dados agregados por dia (média, mínimo, máximo, contagem) das medições"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Agregação diária obtida com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = List.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<List<DailyAggregationDTO>> getDailyAggregation(
      @Parameter(description = "ID do sensor (opcional)") @RequestParam(required = false) String sensorId,
      @Parameter(description = "ID da área (opcional)") @RequestParam(required = false) String areaId,
      @Parameter(description = "Data de início (formato: yyyy-MM-dd, opcional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @Parameter(description = "Data de fim (formato: yyyy-MM-dd, opcional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    List<DailyAggregationDTO> aggregations = measurementsCachingUseCase
        .getDailyAggregation(sensorId, areaId, startDate, endDate);

    return ResponseEntity.ok(aggregations);
  }

  @GetMapping("/daily-average")
  @Operation(
    summary = "Obter média diária",
    description = "Retorna a média das médias diárias das medições no período especificado"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Média diária obtida com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Double.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Double> getDailyAverage(
      @Parameter(description = "ID do sensor (opcional)") @RequestParam(required = false) String sensorId,
      @Parameter(description = "ID da área (opcional)") @RequestParam(required = false) String areaId,
      @Parameter(description = "Data de início (formato: yyyy-MM-dd, opcional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @Parameter(description = "Data de fim (formato: yyyy-MM-dd, opcional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    Double averageOfDailyAverages = measurementsCachingUseCase
        .getAverageOfDailyAverages(sensorId, areaId, startDate, endDate);

    return ResponseEntity.ok(averageOfDailyAverages);
  }
}
