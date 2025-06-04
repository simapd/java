package br.com.simapd.simapd.modules.userReports.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.userReports.UserReportsEntity;
import br.com.simapd.simapd.modules.userReports.UserReportsRepository;

@Service
public class UserReportsCachingUseCase {

  @Autowired
  private UserReportsRepository userReportsRepository;

  @Cacheable(value = "userReports", key = "#id")
  public Optional<UserReportsEntity> findById(String id) {
    return userReportsRepository.findById(id);
  }

  @Cacheable(value = "userReports", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()")
  public Page<UserReportsEntity> findAll(Pageable pageable) {
    return userReportsRepository.findAll(pageable);
  }

  @Cacheable(value = "userReports", key = "'area_' + #areaId + '_page_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()")
  public Page<UserReportsEntity> findByAreaId(String areaId, Pageable pageable) {
    return userReportsRepository.findByAreaId(areaId, pageable);
  }

  @CacheEvict(value = "userReports", allEntries = true)
  public UserReportsEntity save(UserReportsEntity userReport) {
    return userReportsRepository.save(userReport);
  }

  @CacheEvict(value = "userReports", allEntries = true)
  public void deleteById(String id) {
    userReportsRepository.deleteById(id);
  }

  @CacheEvict(value = "userReports", allEntries = true)
  public void clearCache() {
  }
}