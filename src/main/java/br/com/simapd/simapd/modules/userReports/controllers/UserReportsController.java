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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user-reports")
@Tag(name = "Relatórios de Usuários", description = "Endpoints para gerenciamento de relatórios enviados pelos usuários")
@SecurityRequirement(name = "bearerAuth")
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
  @Operation(
    summary = "Criar relatório de usuário",
    description = "Cria um novo relatório enviado por um usuário sobre uma situação ou problema em uma área"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Relatório criado com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = UserReportsDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Dados inválidos",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Usuário ou área não encontrados",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
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
  @Operation(
    summary = "Listar todos os relatórios de usuários",
    description = "Retorna uma lista paginada e ordenada de todos os relatórios enviados pelos usuários"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Lista de relatórios obtida com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Page.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Nenhum relatório encontrado",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Erro na consulta",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Object> findAll(
      @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "reportedAt") String sortBy,
      @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

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
  @Operation(
    summary = "Buscar relatório por ID",
    description = "Retorna um relatório específico baseado no seu ID único"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Relatório encontrado com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = UserReportsDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Relatório não encontrado",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Erro na consulta",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Object> findById(
    @Parameter(description = "ID único do relatório", required = true) @PathVariable String id) {
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
  @Operation(
    summary = "Buscar relatórios por área",
    description = "Retorna todos os relatórios enviados para uma área específica, com paginação e ordenação"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Relatórios da área obtidos com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Page.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Nenhum relatório encontrado para esta área",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Erro na consulta",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Object> findByAreaId(
      @Parameter(description = "ID único da área", required = true) @PathVariable String areaId,
      @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "reportedAt") String sortBy,
      @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

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
  @Operation(
    summary = "Atualizar relatório de usuário",
    description = "Atualiza os dados de um relatório existente, como descrição, status de verificação ou localização"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Relatório atualizado com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = UserReportsDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Relatório não encontrado",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Dados inválidos ou erro na atualização",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Object> update(
    @Parameter(description = "ID único do relatório", required = true) @PathVariable String id, 
    @Valid @RequestBody UpdateUserReportsDTO updateDTO) {
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
  @Operation(
    summary = "Excluir relatório de usuário",
    description = "Remove um relatório do sistema permanentemente. Esta ação não pode ser desfeita"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "Relatório excluído com sucesso",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Relatório não encontrado",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Erro na exclusão",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Object> delete(
    @Parameter(description = "ID único do relatório", required = true) @PathVariable String id) {
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
  @Operation(
    summary = "Buscar relatórios por usuário",
    description = "Retorna todos os relatórios enviados por um usuário específico, com paginação e ordenação"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Relatórios do usuário obtidos com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Page.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Nenhum relatório encontrado para este usuário",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Erro na consulta",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Object> findByUserId(
      @Parameter(description = "ID único do usuário", required = true) @PathVariable String userId,
      @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "reportedAt") String sortBy,
      @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

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