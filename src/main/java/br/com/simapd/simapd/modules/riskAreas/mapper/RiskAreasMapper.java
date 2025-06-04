package br.com.simapd.simapd.modules.riskAreas.mapper;

import java.time.LocalDateTime;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasEntity;
import br.com.simapd.simapd.modules.riskAreas.dto.RiskAreasDTO;
import io.github.thibaultmeyer.cuid.CUID;

public class RiskAreasMapper {

  public static RiskAreasDTO toDTO(RiskAreasEntity entity) {
    RiskAreasDTO dto = new RiskAreasDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }

  public static RiskAreasEntity toEntity(RiskAreasDTO dto) {
    RiskAreasEntity entity = new RiskAreasEntity();
    entity.setId(CUID.randomCUID2().toString());
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setCreatedAt(LocalDateTime.now());
    return entity;
  }
}
