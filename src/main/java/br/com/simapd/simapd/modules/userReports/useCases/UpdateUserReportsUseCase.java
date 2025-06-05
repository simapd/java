package br.com.simapd.simapd.modules.userReports.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasRepository;
import br.com.simapd.simapd.modules.userReports.UserReportsEntity;
import br.com.simapd.simapd.modules.userReports.dto.UpdateUserReportsDTO;
import br.com.simapd.simapd.modules.userReports.dto.UserReportsDTO;
import br.com.simapd.simapd.modules.userReports.mapper.UserReportsMapper;
import br.com.simapd.simapd.modules.users.UsersRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UpdateUserReportsUseCase {

  @Autowired
  private UserReportsCachingUseCase userReportsCachingUseCase;

  @Autowired
  private RiskAreasRepository riskAreasRepository;

  @Autowired
  private UsersRepository usersRepository;

  public UserReportsDTO execute(String id, UpdateUserReportsDTO updateUserReportsDTO) {
    UserReportsEntity existingEntity = userReportsCachingUseCase.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User report not found!"));

    if (updateUserReportsDTO.getAreaId() != null && !riskAreasRepository.existsById(updateUserReportsDTO.getAreaId())) {
      throw new EntityNotFoundException("Risk area not found!");
    }

    if (updateUserReportsDTO.getUserId() != null && !usersRepository.existsById(updateUserReportsDTO.getUserId())) {
      throw new EntityNotFoundException("User not found!");
    }

    if (updateUserReportsDTO.getAreaId() != null) {
      existingEntity.setAreaId(updateUserReportsDTO.getAreaId());
    }
    if (updateUserReportsDTO.getUserId() != null) {
      existingEntity.setUserId(updateUserReportsDTO.getUserId());
    }
    if (updateUserReportsDTO.getDescription() != null) {
      existingEntity.setDescription(updateUserReportsDTO.getDescription());
    }
    if (updateUserReportsDTO.getLocationInfo() != null) {
      existingEntity.setLocationInfo(updateUserReportsDTO.getLocationInfo());
    }
    if (updateUserReportsDTO.getPhotoUrl() != null) {
      existingEntity.setPhotoUrl(updateUserReportsDTO.getPhotoUrl());
    }
    if (updateUserReportsDTO.getIsVerified() != null) {
      existingEntity.setIsVerified(updateUserReportsDTO.getIsVerified());
    }

    UserReportsEntity savedEntity = userReportsCachingUseCase.save(existingEntity);

    return UserReportsMapper.toDTO(savedEntity);
  }
}