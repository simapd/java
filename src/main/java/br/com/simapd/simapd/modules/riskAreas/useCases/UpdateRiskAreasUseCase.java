package br.com.simapd.simapd.modules.riskAreas.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasEntity;
import br.com.simapd.simapd.modules.riskAreas.dto.UpdateRiskAreasDTO;
import br.com.simapd.simapd.modules.riskAreas.dto.RiskAreasDTO;
import br.com.simapd.simapd.modules.riskAreas.mapper.RiskAreasMapper;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UpdateRiskAreasUseCase {

  @Autowired
  private RiskAreasCachingUseCase riskAreasCachingUseCase;

  public RiskAreasDTO execute(String id, UpdateRiskAreasDTO updateRiskAreasDTO) {
    RiskAreasEntity existingEntity = riskAreasCachingUseCase.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Risk area not found!"));

    if (updateRiskAreasDTO.getName() != null) {
      existingEntity.setName(updateRiskAreasDTO.getName());
    }
    if (updateRiskAreasDTO.getDescription() != null) {
      existingEntity.setDescription(updateRiskAreasDTO.getDescription());
    }

    RiskAreasEntity savedEntity = riskAreasCachingUseCase.save(existingEntity);

    return RiskAreasMapper.toDTO(savedEntity);
  }
}
