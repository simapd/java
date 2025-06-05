package br.com.simapd.simapd.modules.userReports.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserReportsDTO {

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  private String areaId;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  private String userId;

  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  @Size(max = 100, message = "Location info must not exceed 100 characters")
  private String locationInfo;

  @Size(max = 500, message = "Photo url must not exceed 500 characters")
  private String photoUrl;

  @Pattern(regexp = "^[01]$", message = "Must be 0 or 1")
  private String isVerified;
}