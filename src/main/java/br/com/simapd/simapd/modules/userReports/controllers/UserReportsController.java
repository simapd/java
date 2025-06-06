package br.com.simapd.simapd.modules.userReports.controllers;

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

import br.com.simapd.simapd.modules.userReports.UserReportsEntity;
import br.com.simapd.simapd.modules.userReports.dto.UpdateUserReportsDTO;
import br.com.simapd.simapd.modules.userReports.dto.UserReportsDTO;
import br.com.simapd.simapd.modules.userReports.mapper.UserReportsMapper;
import br.com.simapd.simapd.modules.userReports.useCases.CreateUserReportsUseCase;
import br.com.simapd.simapd.modules.userReports.useCases.DeleteUserReportsUseCase;
import br.com.simapd.simapd.modules.userReports.useCases.UpdateUserReportsUseCase;
import br.com.simapd.simapd.modules.userReports.useCases.UserReportsCachingUseCase;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user-reports")
public class UserReportsController {

  @Autowired
  private CreateUserReportsUseCase createUserReportsUseCase;

  @Autowired
  private UpdateUserReportsUseCase updateUserReportsUseCase;

  @Autowired
  private DeleteUserReportsUseCase deleteUserReportsUseCase;

  @Autowired
  private UserReportsCachingUseCase userReportsCachingUseCase;

  @PostMapping
  public ResponseEntity<Object> create(@Valid @RequestBody UserReportsDTO userReportsDTO) {
    try {
      UserReportsDTO createdReport = createUserReportsUseCase.execute(userReportsDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating user report: " + e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<Object> findAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "reportedAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir) {

    try {
      Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

      Pageable pageable = PageRequest.of(page, size, sort);
      Page<UserReportsEntity> entities = userReportsCachingUseCase.findAll(pageable);

      if (entities.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user reports found");
      }

      Page<UserReportsDTO> dtos = entities.map(UserReportsMapper::toDTO);
      return ResponseEntity.ok(dtos);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error finding user reports: " + e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable String id) {
    try {
      Optional<UserReportsEntity> entity = userReportsCachingUseCase.findById(id);

      if (entity.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User report not found");
      }

      UserReportsDTO dto = UserReportsMapper.toDTO(entity.get());
      return ResponseEntity.ok(dto);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error finding user report: " + e.getMessage());
    }
  }

  @GetMapping("/area/{areaId}")
  public ResponseEntity<Object> findByAreaId(
      @PathVariable String areaId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "reportedAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir) {

    try {
      Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

      Pageable pageable = PageRequest.of(page, size, sort);
      Page<UserReportsEntity> entities = userReportsCachingUseCase.findByAreaId(areaId, pageable);

      if (entities.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user reports found for this area");
      }

      Page<UserReportsDTO> dtos = entities.map(UserReportsMapper::toDTO);
      return ResponseEntity.ok(dtos);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error finding user reports: " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable String id, @Valid @RequestBody UpdateUserReportsDTO updateDTO) {
    try {
      UserReportsDTO updatedReport = updateUserReportsUseCase.execute(id, updateDTO);
      return ResponseEntity.ok(updatedReport);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating user report: " + e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable String id) {
    try {
      deleteUserReportsUseCase.execute(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting user report: " + e.getMessage());
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<Object> findByUserId(
      @PathVariable String userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "reportedAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir) {

    try {
      Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

      Pageable pageable = PageRequest.of(page, size, sort);
      Page<UserReportsEntity> entities = userReportsCachingUseCase.findByUserId(userId, pageable);

      if (entities.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user reports found for this user");
      }

      Page<UserReportsDTO> dtos = entities.map(UserReportsMapper::toDTO);
      return ResponseEntity.ok(dtos);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error finding user reports: " + e.getMessage());
    }
  }
}