package br.com.simapd.simapd.modules.riskAreas.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DeleteRiskAreasUseCase {

  @Autowired
  private RiskAreasCachingUseCase riskAreasCachingUseCase;

  public void execute(String id) {
    if (!riskAreasCachingUseCase.findById(id).isPresent()) {
      throw new EntityNotFoundException("Risk area not found!");
    }

    riskAreasCachingUseCase.deleteById(id);
  }
}
