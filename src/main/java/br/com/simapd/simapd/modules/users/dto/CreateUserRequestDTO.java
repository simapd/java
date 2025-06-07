package br.com.simapd.simapd.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados necessários para criar um novo usuário no sistema")
public class CreateUserRequestDTO {

  @NotBlank(message = "Name is required")
  @Size(max = 150, message = "Name must not exceed 150 characters")
  @Schema(description = "Nome completo do usuário", example = "João Silva Santos")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Size(max = 150, message = "Email must not exceed 150 characters")
  @Schema(description = "Email único do usuário para login", example = "joao.silva@empresa.com")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
  @Schema(description = "Senha segura com pelo menos 6 caracteres", example = "minhasenha123")
  private String password;

  @NotBlank(message = "Area ID is required")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for area ID")
  @Schema(description = "ID da área de risco à qual o usuário está associado", example = "cm123456789012345678901234")
  private String areaId;
} 