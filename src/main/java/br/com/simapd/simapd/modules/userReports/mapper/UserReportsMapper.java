package br.com.simapd.simapd.modules.userReports.mapper;

import java.time.LocalDateTime;

import br.com.simapd.simapd.modules.userReports.UserReportsEntity;
import br.com.simapd.simapd.modules.userReports.dto.UserReportsDTO;
import io.github.thibaultmeyer.cuid.CUID;

public class UserReportsMapper {

  public static UserReportsDTO toDTO(UserReportsEntity entity) {
    UserReportsDTO dto = new UserReportsDTO();
    dto.setId(entity.getId());
    dto.setAreaId(entity.getAreaId());
    dto.setUserId(entity.getUserId());
    dto.setDescription(entity.getDescription());
    dto.setLocationInfo(entity.getLocationInfo());
    dto.setPhotoUrl(entity.getPhotoUrl());
    dto.setIsVerified(entity.getIsVerified());
    dto.setReportedAt(entity.getReportedAt());
    return dto;
  }

  public static UserReportsEntity toEntity(UserReportsDTO dto) {
    UserReportsEntity entity = new UserReportsEntity();
    entity.setId(CUID.randomCUID2(24).toString());
    entity.setAreaId(dto.getAreaId());
    entity.setUserId(dto.getUserId());
    entity.setDescription(dto.getDescription());
    entity.setLocationInfo(dto.getLocationInfo());
    entity.setPhotoUrl(dto.getPhotoUrl());
    entity.setIsVerified(dto.getIsVerified());
    entity.setReportedAt(LocalDateTime.now());
    return entity;
  }
}