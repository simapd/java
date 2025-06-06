package br.com.simapd.simapd.modules.measurements.mapper;

import org.springframework.stereotype.Component;

import br.com.simapd.simapd.modules.measurements.MeasurementsEntity;
import br.com.simapd.simapd.modules.measurements.dto.MeasurementsDTO;

@Component
public class MeasurementsMapper {

  public MeasurementsDTO toDTO(MeasurementsEntity entity) {
    MeasurementsDTO dto = new MeasurementsDTO();
    dto.setId(entity.getId());
    dto.setMeasurementValue(entity.getMeasurementValue());
    dto.setMeasuredAt(entity.getMeasuredAt());
    dto.setSensorId(entity.getSensorId());

    return dto;
  }

  public MeasurementsEntity toEntity(MeasurementsDTO dto) {
    MeasurementsEntity entity = new MeasurementsEntity();
    entity.setId(dto.getId());
    entity.setMeasurementValue(dto.getMeasurementValue());
    entity.setMeasuredAt(dto.getMeasuredAt());
    entity.setSensorId(dto.getSensorId());

    return entity;
  }
}
