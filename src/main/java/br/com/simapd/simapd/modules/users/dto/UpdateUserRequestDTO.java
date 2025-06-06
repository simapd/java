package br.com.simapd.simapd.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequestDTO {

  @Size(max = 150, message = "Name must not exceed 150 characters")
  private String name;

  @Email(message = "Invalid email format")
  @Size(max = 150, message = "Email must not exceed 150 characters")
  private String email;

  @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
  private String password;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format for area ID")
  private String areaId;
} 