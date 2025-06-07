package br.com.simapd.simapd.modules.measurements.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import br.com.simapd.simapd.modules.measurements.MeasurementsEntity;
import br.com.simapd.simapd.modules.measurements.dto.DailyAggregationDTO;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MeasurementAggregator {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Double extractNumericValue(String jsonValue, Integer sensorType) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonValue);
            
            switch (sensorType) {
                case 1:
                    return getDoubleValue(rootNode, "rainLevel");
                
                case 2:
                    return getDoubleValue(rootNode, "moistureLevel");
                
                case 3:
                    JsonNode acceleration = rootNode.get("acceleration");
                    if (acceleration != null) {
                        return getDoubleValue(acceleration, "magnitude");
                    }
                    return null;
                
                case 4:
                    return getDoubleValue(rootNode, "temperature");
                
                default:
                    return findFirstNumericValue(rootNode);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public List<DailyAggregationDTO> processAggregation(List<MeasurementsEntity> measurements) {
        Map<LocalDate, List<Double>> dailyValues = new HashMap<>();

        for (MeasurementsEntity measurement : measurements) {
            LocalDate date = measurement.getMeasuredAt().toLocalDate();
            Double value = extractNumericValue(measurement.getValue(), measurement.getType());
            
            if (value != null) {
                dailyValues.computeIfAbsent(date, k -> new ArrayList<>()).add(value);
            }
        }

        return dailyValues.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Double> values = entry.getValue();
                    
                    return new DailyAggregationDTO(
                            date,
                            calculateAverage(values),
                            (long) values.size(),
                            calculateMin(values),
                            calculateMax(values),
                            calculateSum(values)
                    );
                })
                .sorted(Comparator.comparing(DailyAggregationDTO::getDate))
                .collect(Collectors.toList());
    }

    private Double getDoubleValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && fieldNode.isNumber()) {
            return fieldNode.asDouble();
        }
        return null;
    }

    private Double findFirstNumericValue(JsonNode node) {
        if (node.isNumber()) {
            return node.asDouble();
        }
        
        if (node.isObject()) {
            for (JsonNode childNode : node) {
                Double value = findFirstNumericValue(childNode);
                if (value != null) {
                    return value;
                }
            }
        }
        
        return null;
    }

    private Double calculateAverage(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private Double calculateMin(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    private Double calculateMax(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    private Double calculateSum(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).sum();
    }
} 