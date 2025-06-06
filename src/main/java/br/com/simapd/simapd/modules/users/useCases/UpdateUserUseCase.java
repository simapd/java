package br.com.simapd.simapd.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasRepository;
import br.com.simapd.simapd.modules.users.UsersEntity;
import br.com.simapd.simapd.modules.users.dto.UpdateUserRequestDTO;
import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;
import br.com.simapd.simapd.modules.users.mapper.UsersMapper;

@Service
public class UpdateUserUseCase {

  @Autowired
  private UsersCachingUseCase usersCachingUseCase;

  @Autowired
  private RiskAreasRepository riskAreasRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsersMapper usersMapper;

  public UserResponseDTO execute(String id, UpdateUserRequestDTO updateUserRequestDTO) {
    UsersEntity user = usersCachingUseCase.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (updateUserRequestDTO.getEmail() != null &&
        !updateUserRequestDTO.getEmail().equals(user.getEmail()) &&
        usersCachingUseCase.existsByEmail(updateUserRequestDTO.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    if (updateUserRequestDTO.getAreaId() != null &&
        !riskAreasRepository.existsById(updateUserRequestDTO.getAreaId())) {
      throw new RuntimeException("Risk area not found");
    }

    if (updateUserRequestDTO.getName() != null) {
      user.setName(updateUserRequestDTO.getName());
    }

    if (updateUserRequestDTO.getEmail() != null) {
      user.setEmail(updateUserRequestDTO.getEmail());
    }

    if (updateUserRequestDTO.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(updateUserRequestDTO.getPassword()));
    }

    if (updateUserRequestDTO.getAreaId() != null) {
      user.setAreaId(updateUserRequestDTO.getAreaId());
    }

    UsersEntity updatedUser = usersCachingUseCase.save(user);

    return usersMapper.toResponseDTO(updatedUser);
  }
}