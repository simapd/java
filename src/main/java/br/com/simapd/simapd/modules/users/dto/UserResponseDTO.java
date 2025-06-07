package br.com.simapd.simapd.modules.users.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "Dados de resposta de um usuário do sistema")
public class UserResponseDTO {
  @Schema(description = "ID único do usuário", example = "cm987654321098765432109876")
  private String id;
  
  @Schema(description = "Nome completo do usuário", example = "João Silva Santos")
  private String name;
  
  @Schema(description = "Email do usuário", example = "joao.silva@empresa.com")
  private String email;
  
  @Schema(description = "ID da área de risco associada", example = "cm123456789012345678901234")
  private String areaId;
  
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "Data e hora de criação do usuário", example = "2024-01-15 14:30")
  private LocalDateTime createdAt;
} 