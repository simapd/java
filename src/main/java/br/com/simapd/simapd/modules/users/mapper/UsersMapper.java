package br.com.simapd.simapd.modules.users.mapper;

import org.springframework.stereotype.Component;

import br.com.simapd.simapd.modules.users.UsersEntity;
import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;

@Component
public class UsersMapper {

  public UserResponseDTO toResponseDTO(UsersEntity entity) {
    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setEmail(entity.getEmail());
    dto.setAreaId(entity.getAreaId());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }
} 