package br.com.simapd.simapd.modules.userReports.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DeleteUserReportsUseCase {

  @Autowired
  private UserReportsCachingUseCase userReportsCachingUseCase;

  public void execute(String id) {
    if (!userReportsCachingUseCase.findById(id).isPresent()) {
      throw new EntityNotFoundException("User report not found!");
    }

    userReportsCachingUseCase.deleteById(id);
  }
}
