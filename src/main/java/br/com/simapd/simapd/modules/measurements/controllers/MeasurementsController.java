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

@RestController
@RequestMapping("/api/measurements")
public class MeasurementsController {

  @Autowired
  private MeasurementsCachingUseCase measurementsCachingUseCase;

  @GetMapping("/{id}")
  public ResponseEntity<MeasurementsDTO> findById(@PathVariable String id) {
    Optional<MeasurementsDTO> measurement = measurementsCachingUseCase.findById(id);
    return measurement.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<Page<MeasurementsDTO>> findAll(Pageable pageable) {
    Page<MeasurementsDTO> measurements = measurementsCachingUseCase.findAll(pageable);
    return ResponseEntity.ok(measurements);
  }

  @GetMapping("/sensor/{sensorId}")
  public ResponseEntity<MeasurementsDTO> findBySensorId(@PathVariable String sensorId) {
    Optional<MeasurementsDTO> measurement = measurementsCachingUseCase.findBySensorId(sensorId);
    return measurement.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/filter")
  public ResponseEntity<List<MeasurementsDTO>> findByFilters(
      @RequestParam(required = false) String sensorId,
      @RequestParam(required = false) String areaId,
      @RequestParam(required = false) Integer type,
      @RequestParam(required = false) Integer riskLevel) {

    List<MeasurementsDTO> measurements = measurementsCachingUseCase
        .findByFilters(sensorId, areaId, type, riskLevel);

    return ResponseEntity.ok(measurements);
  }

  @GetMapping("/daily-aggregation")
  public ResponseEntity<List<DailyAggregationDTO>> getDailyAggregation(
      @RequestParam(required = false) String sensorId,
      @RequestParam(required = false) String areaId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    List<DailyAggregationDTO> aggregations = measurementsCachingUseCase
        .getDailyAggregation(sensorId, areaId, startDate, endDate);

    return ResponseEntity.ok(aggregations);
  }

  @GetMapping("/daily-average")
  public ResponseEntity<Double> getDailyAverage(
      @RequestParam(required = false) String sensorId,
      @RequestParam(required = false) String areaId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    Double averageOfDailyAverages = measurementsCachingUseCase
        .getAverageOfDailyAverages(sensorId, areaId, startDate, endDate);

    return ResponseEntity.ok(averageOfDailyAverages);
  }
}
