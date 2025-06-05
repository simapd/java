package br.com.simapd.simapd.modules.users.useCases;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasRepository;
import br.com.simapd.simapd.modules.users.UsersEntity;
import br.com.simapd.simapd.modules.users.UsersRepository;
import br.com.simapd.simapd.modules.users.dto.CreateUserRequestDTO;
import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;
import br.com.simapd.simapd.modules.users.mapper.UsersMapper;
import io.github.thibaultmeyer.cuid.CUID;

@Service
public class CreateUserUseCase {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private RiskAreasRepository riskAreasRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsersMapper usersMapper;

  public UserResponseDTO execute(CreateUserRequestDTO createUserRequestDTO) {
    // Verificar se email já existe
    if (usersRepository.existsByEmail(createUserRequestDTO.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    // Verificar se a área de risco existe
    if (!riskAreasRepository.existsById(createUserRequestDTO.getAreaId())) {
      throw new RuntimeException("Risk area not found");
    }

    UsersEntity user = new UsersEntity();
    user.setId(CUID.randomCUID2(24).toString());
    user.setName(createUserRequestDTO.getName());
    user.setEmail(createUserRequestDTO.getEmail());
    user.setPassword(passwordEncoder.encode(createUserRequestDTO.getPassword()));
    user.setAreaId(createUserRequestDTO.getAreaId());
    user.setCreatedAt(LocalDateTime.now());

    UsersEntity savedUser = usersRepository.save(user);
    
    return usersMapper.toResponseDTO(savedUser);
  }
} 