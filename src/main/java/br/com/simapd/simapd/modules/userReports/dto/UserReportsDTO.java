package br.com.simapd.simapd.modules.userReports.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados de um relatório enviado por usuário sobre situações na área")
public class UserReportsDTO {

  @Schema(description = "ID único do relatório", example = "cm345678901234567890123456")
  private String id;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Schema(description = "ID da área onde o problema foi reportado", example = "cm123456789012345678901234")
  private String areaId;

  @NotNull(message = "User ID is required")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Schema(description = "ID do usuário que enviou o relatório", example = "cm987654321098765432109876")
  private String userId;

  @NotNull(message = "Description is required")
  @Size(max = 500, message = "Description must not exceed 500 characters")
  @Schema(description = "Descrição detalhada do problema ou situação reportada", example = "Vazamento de óleo detectado próximo ao rio")
  private String description;

  @Size(max = 100, message = "Location info must not exceed 100 characters")
  @Schema(description = "Informações adicionais sobre a localização", example = "Próximo ao galpão 5, setor industrial")
  private String locationInfo;

  @Size(max = 500, message = "Photo url must not exceed 500 characters")
  @Schema(description = "URL da foto anexada ao relatório", example = "https://storage.simapd.com/reports/photo123.jpg")
  private String photoUrl;

  @Pattern(regexp = "^[01]$", message = "Must be 0 or 1")
  @Schema(description = "Status de verificação do relatório (0=Não verificado, 1=Verificado)", example = "0")
  private String isVerified = "0";

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @Schema(description = "Data e hora do envio do relatório", example = "2024-01-15 14:30")
  private LocalDateTime reportedAt;
}