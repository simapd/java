package br.com.simapd.simapd.modules.measurements.useCases;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.measurements.MeasurementsEntity;
import br.com.simapd.simapd.modules.measurements.MeasurementsRepository;
import br.com.simapd.simapd.modules.measurements.dto.DailyAggregationDTO;
import br.com.simapd.simapd.modules.measurements.dto.MeasurementsDTO;
import br.com.simapd.simapd.modules.measurements.mapper.MeasurementsMapper;

@Service
public class MeasurementsCachingUseCase {

    @Autowired
    private MeasurementsRepository measurementsRepository;

    @Autowired
    private MeasurementsMapper measurementsMapper;

    @Cacheable(value = "measurements-by-id", key = "#id")
    public Optional<MeasurementsDTO> findById(String id) {
        Optional<MeasurementsEntity> entity = measurementsRepository.findById(id);
        return entity.map(measurementsMapper::toDTO);
    }

    @Cacheable(value = "measurements-pages", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<MeasurementsDTO> findAll(Pageable pageable) {
        Page<MeasurementsEntity> entities = measurementsRepository.findAll(pageable);
        return entities.map(measurementsMapper::toDTO);
    }

    @Cacheable(value = "measurements-by-sensor", key = "#sensorId")
    public Optional<MeasurementsDTO> findBySensorId(String sensorId) {
        MeasurementsEntity entity = measurementsRepository.findBySensorId(sensorId);
        return Optional.ofNullable(entity)
                .map(measurementsMapper::toDTO);
    }

    @Cacheable(value = "measurements-by-filters", key = "#sensorId + '_' + #areaId + '_' + #type + '_' + #riskLevel")
    public List<MeasurementsDTO> findByFilters(String sensorId, String areaId, Integer type, Integer riskLevel) {
        List<MeasurementsEntity> entities = measurementsRepository.findByFilters(sensorId, areaId, type, riskLevel);
        return entities.stream()
                .map(measurementsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "daily-aggregations", key = "#sensorId + '_' + #areaId + '_' + #startDate + '_' + #endDate")
    public List<DailyAggregationDTO> getDailyAggregation(String sensorId, String areaId, LocalDate startDate,
            LocalDate endDate) {
        List<Object[]> results = measurementsRepository.findDailyAggregation(sensorId, areaId, startDate, endDate);

        return results.stream()
                .map(row -> new DailyAggregationDTO(
                        ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate(),
                        ((Number) row[1]).doubleValue(),
                        ((Number) row[2]).longValue(),
                        ((Number) row[3]).doubleValue(),
                        ((Number) row[4]).doubleValue(),
                        ((Number) row[5]).doubleValue()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "daily-aggregations", key = "'avg_' + #sensorId + '_' + #areaId + '_' + #startDate + '_' + #endDate")
    public Double getAverageOfDailyAverages(String sensorId, String areaId, LocalDate startDate, LocalDate endDate) {
        List<DailyAggregationDTO> dailyAggregations = getDailyAggregation(sensorId, areaId, startDate, endDate);

        return dailyAggregations.stream()
                .mapToDouble(DailyAggregationDTO::getAverageValue)
                .average()
                .orElse(0.0);
    }

    @CacheEvict(value = { "measurements-by-id", "measurements-pages", "measurements-by-sensor",
            "measurements-by-filters", "daily-aggregations" }, allEntries = true)
    public void clearCache() {
    }
}
