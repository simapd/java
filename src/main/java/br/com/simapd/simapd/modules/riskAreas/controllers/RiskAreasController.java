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
@RequestMapping("/api/risk-areas")
@Tag(name = "Áreas de Risco", description = "Endpoints para gerenciamento de áreas de risco monitoradas pelo sistema")
@SecurityRequirement(name = "bearerAuth")
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
  @Operation(
    summary = "Criar área de risco",
    description = "Cria uma nova área de risco no sistema com coordenadas geográficas"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Área de risco criada com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RiskAreasDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Dados inválidos",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<RiskAreasDTO> create(@Valid @RequestBody RiskAreasDTO riskAreasDTO) {
    try {
      RiskAreasDTO createdRiskArea = createRiskAreasUseCase.execute(riskAreasDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdRiskArea);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping
  @Operation(
    summary = "Listar todas as áreas de risco",
    description = "Retorna uma lista paginada e ordenada de todas as áreas de risco cadastradas no sistema"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Lista de áreas de risco obtida com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Page.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Page<RiskAreasDTO>> findAll(
      @Parameter(description = "Número da página (inicia em 0)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "createdAt") String sortBy,
      @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(defaultValue = "desc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<RiskAreasEntity> riskAreas = riskAreasCachingUseCase.findAll(pageable);
    Page<RiskAreasDTO> riskAreasDTO = riskAreas.map(RiskAreasMapper::toDTO);

    return ResponseEntity.ok(riskAreasDTO);
  }

  @GetMapping("/{id}")
  @Operation(
    summary = "Buscar área de risco por ID",
    description = "Retorna uma área de risco específica baseada no seu ID único"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Área de risco encontrada com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RiskAreasDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Área de risco não encontrada",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Token inválido ou expirado",
      content = @Content
    )
  })
  public ResponseEntity<Object> findById(
    @Parameter(description = "ID único da área de risco", required = true) @PathVariable String id) {
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
  @Operation(
    summary = "Atualizar área de risco",
    description = "Atualiza os dados de uma área de risco existente, como nome, descrição ou coordenadas"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "Área de risco atualizada com sucesso",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Área de risco não encontrada",
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
    @Parameter(description = "ID único da área de risco", required = true) @PathVariable String id,
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
  @Operation(
    summary = "Excluir área de risco",
    description = "Remove uma área de risco do sistema permanentemente. Esta ação não pode ser desfeita e pode afetar medições e relatórios associados"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "204",
      description = "Área de risco excluída com sucesso",
      content = @Content
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Área de risco não encontrada",
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
    @Parameter(description = "ID único da área de risco", required = true) @PathVariable String id) {
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
