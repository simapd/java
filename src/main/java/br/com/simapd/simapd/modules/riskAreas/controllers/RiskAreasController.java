package br.com.simapd.simapd.modules.riskAreas.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasEntity;
import br.com.simapd.simapd.modules.riskAreas.dto.RiskAreasDTO;
import br.com.simapd.simapd.modules.riskAreas.dto.UpdateRiskAreasDTO;
import br.com.simapd.simapd.modules.riskAreas.mapper.RiskAreasMapper;
import br.com.simapd.simapd.modules.riskAreas.useCases.CreateRiskAreasUseCase;
import br.com.simapd.simapd.modules.riskAreas.useCases.DeleteRiskAreasUseCase;
import br.com.simapd.simapd.modules.riskAreas.useCases.RiskAreasCachingUseCase;
import br.com.simapd.simapd.modules.riskAreas.useCases.UpdateRiskAreasUseCase;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/risk-areas")
public class RiskAreasController {

  @Autowired
  private CreateRiskAreasUseCase createRiskAreasUseCase;

  @Autowired
  private UpdateRiskAreasUseCase updateRiskAreasUseCase;

  @Autowired
  private DeleteRiskAreasUseCase deleteRiskAreasUseCase;

  @Autowired
  private RiskAreasCachingUseCase riskAreasCachingUseCase;

  @PostMapping
  public ResponseEntity<RiskAreasDTO> create(@Valid @RequestBody RiskAreasDTO riskAreasDTO) {
    try {
      RiskAreasDTO createdRiskArea = createRiskAreasUseCase.execute(riskAreasDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdRiskArea);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping
  public ResponseEntity<Page<RiskAreasDTO>> findAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<RiskAreasEntity> riskAreas = riskAreasCachingUseCase.findAll(pageable);
    Page<RiskAreasDTO> riskAreasDTO = riskAreas.map(RiskAreasMapper::toDTO);

    return ResponseEntity.ok(riskAreasDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable String id) {
    try {
      Optional<RiskAreasEntity> entity = riskAreasCachingUseCase.findById(id);

      if (entity.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Risk area not found");
      }

      RiskAreasDTO dto = RiskAreasMapper.toDTO(entity.get());
      return ResponseEntity.ok(dto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable String id,
      @Valid @RequestBody UpdateRiskAreasDTO updateRiskAreasDTO) {
    try {
      updateRiskAreasUseCase.execute(id, updateRiskAreasDTO);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating risk area: " + e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable String id) {
    try {
      deleteRiskAreasUseCase.execute(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting risk area: " + e.getMessage());
    }
  }
}
