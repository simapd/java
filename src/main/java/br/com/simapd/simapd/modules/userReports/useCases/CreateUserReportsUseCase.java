package br.com.simapd.simapd.modules.userReports.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasRepository;
import br.com.simapd.simapd.modules.userReports.UserReportsEntity;
import br.com.simapd.simapd.modules.userReports.dto.UserReportsDTO;
import br.com.simapd.simapd.modules.userReports.mapper.UserReportsMapper;
import br.com.simapd.simapd.modules.users.UsersRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CreateUserReportsUseCase {

  @Autowired
  private UserReportsCachingUseCase userReportsCachingUseCase;

  @Autowired
  private RiskAreasRepository riskAreasRepository;

  @Autowired
  private UsersRepository usersRepository;

  public UserReportsDTO execute(UserReportsDTO userReportsDTO) {

    if (userReportsDTO.getAreaId() != null && !riskAreasRepository.existsById(userReportsDTO.getAreaId())) {
      throw new EntityNotFoundException("Risk area not found!");
    }

    if (!usersRepository.existsById(userReportsDTO.getUserId())) {
      throw new EntityNotFoundException("User not found!");
    }

    UserReportsEntity userReportsEntity = UserReportsMapper.toEntity(userReportsDTO);

    UserReportsEntity savedEntity = userReportsCachingUseCase.save(userReportsEntity);

    return UserReportsMapper.toDTO(savedEntity);
  }
}
