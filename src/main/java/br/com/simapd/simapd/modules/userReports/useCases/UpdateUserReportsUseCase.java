package br.com.simapd.simapd.modules.userReports.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasRepository;
import br.com.simapd.simapd.modules.userReports.UserReportsEntity;
import br.com.simapd.simapd.modules.userReports.dto.UserReportsDTO;
import br.com.simapd.simapd.modules.userReports.mapper.UserReportsMapper;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UpdateUserReportsUseCase {

  @Autowired
  private UserReportsCachingUseCase userReportsCachingUseCase;

  @Autowired
  private RiskAreasRepository riskAreasRepository;

  public UserReportsDTO execute(String id, UserReportsDTO userReportsDTO) {
    UserReportsEntity existingEntity = userReportsCachingUseCase.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User report not found!"));

    if (userReportsDTO.getAreaId() != null && !riskAreasRepository.existsById(userReportsDTO.getAreaId())) {
      throw new EntityNotFoundException("Risk area not found!");
    }

    if (userReportsDTO.getAreaId() != null) {
      existingEntity.setAreaId(userReportsDTO.getAreaId());
    }
    if (userReportsDTO.getDescription() != null) {
      existingEntity.setDescription(userReportsDTO.getDescription());
    }
    if (userReportsDTO.getLocationInfo() != null) {
      existingEntity.setLocationInfo(userReportsDTO.getLocationInfo());
    }
    if (userReportsDTO.getPhotoUrl() != null) {
      existingEntity.setPhotoUrl(userReportsDTO.getPhotoUrl());
    }
    if (userReportsDTO.getIsVerified() != null) {
      existingEntity.setIsVerified(userReportsDTO.getIsVerified());
    }

    UserReportsEntity savedEntity = userReportsCachingUseCase.save(existingEntity);

    return UserReportsMapper.toDTO(savedEntity);
  }
}