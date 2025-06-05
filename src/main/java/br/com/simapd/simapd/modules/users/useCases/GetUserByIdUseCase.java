package br.com.simapd.simapd.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.users.UsersEntity;
import br.com.simapd.simapd.modules.users.UsersRepository;
import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;
import br.com.simapd.simapd.modules.users.mapper.UsersMapper;

@Service
public class GetUserByIdUseCase {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private UsersMapper usersMapper;

  public UserResponseDTO execute(String id) {
    UsersEntity user = usersRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return usersMapper.toResponseDTO(user);
  }
} 