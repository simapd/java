package br.com.simapd.simapd.modules.riskAreas.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasEntity;
import br.com.simapd.simapd.modules.riskAreas.RiskAreasRepository;

@Service
public class RiskAreasCachingUseCase {

  @Autowired
  private RiskAreasRepository riskAreasRepository;

  @Cacheable(value = "riskAreas", key = "#id")
  public Optional<RiskAreasEntity> findById(String id) {
    return riskAreasRepository.findById(id);
  }

  @Cacheable(value = "riskAreas", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()")
  public Page<RiskAreasEntity> findAll(Pageable pageable) {
    return riskAreasRepository.findAll(pageable);
  }

  @CacheEvict(value = "riskAreas", allEntries = true)
  public RiskAreasEntity save(RiskAreasEntity riskArea) {
    return riskAreasRepository.save(riskArea);
  }

  @CacheEvict(value = "riskAreas", allEntries = true)
  public void deleteById(String id) {
    riskAreasRepository.deleteById(id);
  }

  @CacheEvict(value = "riskAreas", allEntries = true)
  public void clearCache() {
  }
}
