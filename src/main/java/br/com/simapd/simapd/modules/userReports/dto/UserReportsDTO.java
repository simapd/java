package br.com.simapd.simapd.modules.userReports.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserReportsDTO {

  private String id;

  @NotNull(message = "Area ID is required")
  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  private String areaId;

  @NotNull(message = "Description is required")
  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  @Size(max = 100, message = "Location info must not exceed 100 characters")
  private String locationInfo;

  @Size(max = 500, message = "Photo url must not exceed 500 characters")
  private String photoUrl;

  @Pattern(regexp = "^[01]$", message = "Must be 0 or 1")
  private String isVerified = "0";

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime reportedAt;
}