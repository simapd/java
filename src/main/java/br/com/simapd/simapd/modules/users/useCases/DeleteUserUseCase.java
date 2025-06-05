package br.com.simapd.simapd.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.users.UsersRepository;

@Service
public class DeleteUserUseCase {

  @Autowired
  private UsersRepository usersRepository;

  public void execute(String id) {
    if (!usersRepository.existsById(id)) {
      throw new RuntimeException("User not found");
    }

    usersRepository.deleteById(id);
  }
} 