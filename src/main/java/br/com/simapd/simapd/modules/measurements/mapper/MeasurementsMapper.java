package br.com.simapd.simapd.modules.measurements.mapper;

import org.springframework.stereotype.Component;

import br.com.simapd.simapd.modules.measurements.MeasurementsEntity;
import br.com.simapd.simapd.modules.measurements.dto.MeasurementsDTO;

@Component
public class MeasurementsMapper {

  public MeasurementsDTO toDTO(MeasurementsEntity entity) {
    MeasurementsDTO dto = new MeasurementsDTO();
    dto.setId(entity.getId());
    dto.setType(entity.getType());
    dto.setValue(entity.getValue());
    dto.setRiskLevel(entity.getRiskLevel());
    dto.setMeasuredAt(entity.getMeasuredAt());
    dto.setSensorId(entity.getSensorId());
    dto.setAreaId(entity.getAreaId());

    return dto;
  }

  public MeasurementsEntity toEntity(MeasurementsDTO dto) {
    MeasurementsEntity entity = new MeasurementsEntity();
    entity.setId(dto.getId());
    entity.setType(dto.getType());
    entity.setValue(dto.getValue());
    entity.setRiskLevel(dto.getRiskLevel());
    entity.setMeasuredAt(dto.getMeasuredAt());
    entity.setSensorId(dto.getSensorId());
    entity.setAreaId(dto.getAreaId());

    return entity;
  }
}
