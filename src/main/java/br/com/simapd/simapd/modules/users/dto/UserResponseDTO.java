package br.com.simapd.simapd.modules.users.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserResponseDTO {
  private String id;
  private String name;
  private String email;
  private String areaId;
  
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;
} 