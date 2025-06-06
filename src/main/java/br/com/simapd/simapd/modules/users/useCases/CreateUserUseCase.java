package br.com.simapd.simapd.modules.users.useCases;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.auth.dto.LoginResponseDTO;
import br.com.simapd.simapd.modules.auth.services.JwtService;
import br.com.simapd.simapd.modules.riskAreas.RiskAreasRepository;
import br.com.simapd.simapd.modules.users.UsersEntity;
import br.com.simapd.simapd.modules.users.dto.CreateUserRequestDTO;
import br.com.simapd.simapd.modules.users.mapper.UsersMapper;
import io.github.thibaultmeyer.cuid.CUID;

@Service
public class CreateUserUseCase {

  @Autowired
  private UsersCachingUseCase usersCachingUseCase;

  @Autowired
  private RiskAreasRepository riskAreasRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsersMapper usersMapper;

  @Autowired
  private JwtService jwtService;

  public LoginResponseDTO execute(CreateUserRequestDTO createUserRequestDTO) {
    if (usersCachingUseCase.existsByEmail(createUserRequestDTO.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

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

    UsersEntity savedUser = usersCachingUseCase.save(user);

    String token = jwtService.generateToken(savedUser);

    return new LoginResponseDTO(token, usersMapper.toResponseDTO(savedUser));
  }
}