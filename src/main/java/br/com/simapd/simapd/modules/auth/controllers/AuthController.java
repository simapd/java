package br.com.simapd.simapd.modules.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.simapd.simapd.modules.auth.dto.LoginRequestDTO;
import br.com.simapd.simapd.modules.auth.dto.LoginResponseDTO;
import br.com.simapd.simapd.modules.auth.useCases.LoginUseCase;
import br.com.simapd.simapd.modules.users.UsersEntity;
import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;
import br.com.simapd.simapd.modules.users.mapper.UsersMapper;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private LoginUseCase loginUseCase;

  @Autowired
  private UsersMapper usersMapper;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
    try {
      LoginResponseDTO response = loginUseCase.execute(loginRequestDTO);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.unauthorized().build();
    }
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UsersEntity) {
      UsersEntity user = (UsersEntity) authentication.getPrincipal();
      UserResponseDTO userResponse = usersMapper.toResponseDTO(user);
      return ResponseEntity.ok(userResponse);
    }
    return ResponseEntity.unauthorized().build();
  }
} 