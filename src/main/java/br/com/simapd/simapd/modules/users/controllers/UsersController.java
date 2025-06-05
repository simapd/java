package br.com.simapd.simapd.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.simapd.simapd.modules.auth.dto.LoginResponseDTO;
import br.com.simapd.simapd.modules.users.dto.CreateUserRequestDTO;
import br.com.simapd.simapd.modules.users.dto.UpdateUserRequestDTO;
import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;
import br.com.simapd.simapd.modules.users.useCases.CreateUserUseCase;
import br.com.simapd.simapd.modules.users.useCases.DeleteUserUseCase;
import br.com.simapd.simapd.modules.users.useCases.GetAllUsersUseCase;
import br.com.simapd.simapd.modules.users.useCases.GetUserByIdUseCase;
import br.com.simapd.simapd.modules.users.useCases.UpdateUserUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UsersController {

  @Autowired
  private CreateUserUseCase createUserUseCase;

  @Autowired
  private GetAllUsersUseCase getAllUsersUseCase;

  @Autowired
  private GetUserByIdUseCase getUserByIdUseCase;

  @Autowired
  private UpdateUserUseCase updateUserUseCase;

  @Autowired
  private DeleteUserUseCase deleteUserUseCase;

  @PostMapping("/register")
  public ResponseEntity<LoginResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
    try {
      LoginResponseDTO user = createUserUseCase.execute(createUserRequestDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
    Page<UserResponseDTO> users = getAllUsersUseCase.execute(pageable);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
    try {
      UserResponseDTO user = getUserByIdUseCase.execute(id);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(
      @PathVariable String id,
      @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
    try {
      UserResponseDTO user = updateUserUseCase.execute(id, updateUserRequestDTO);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    try {
      deleteUserUseCase.execute(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
} 