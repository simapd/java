package br.com.simapd.simapd.modules.auth.dto;

import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
  private String token;
  private UserResponseDTO user;
} 