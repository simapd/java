package br.com.simapd.simapd.modules.riskAreas.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasEntity;
import br.com.simapd.simapd.modules.riskAreas.dto.RiskAreasDTO;
import br.com.simapd.simapd.modules.riskAreas.mapper.RiskAreasMapper;

@Service
public class CreateRiskAreasUseCase {

  @Autowired
  private RiskAreasCachingUseCase riskAreasCachingUseCase;

  public RiskAreasDTO execute(RiskAreasDTO riskAreasDTO) {
    RiskAreasEntity riskAreasEntity = RiskAreasMapper.toEntity(riskAreasDTO);

    RiskAreasEntity savedEntity = riskAreasCachingUseCase.save(riskAreasEntity);

    return RiskAreasMapper.toDTO(savedEntity);
  }
}
