package br.com.simapd.simapd.modules.auth.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.auth.dto.LoginRequestDTO;
import br.com.simapd.simapd.modules.auth.dto.LoginResponseDTO;
import br.com.simapd.simapd.modules.auth.services.JwtService;
import br.com.simapd.simapd.modules.users.UsersEntity;
import br.com.simapd.simapd.modules.users.UsersRepository;
import br.com.simapd.simapd.modules.users.mapper.UsersMapper;

@Service
public class LoginUseCase {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UsersMapper usersMapper;

  public LoginResponseDTO execute(LoginRequestDTO loginRequestDTO) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequestDTO.getEmail(),
            loginRequestDTO.getPassword()
        )
    );

    UsersEntity user = usersRepository.findByEmail(loginRequestDTO.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    String token = jwtService.generateToken(user);

    return new LoginResponseDTO(token, usersMapper.toResponseDTO(user));
  }
} 