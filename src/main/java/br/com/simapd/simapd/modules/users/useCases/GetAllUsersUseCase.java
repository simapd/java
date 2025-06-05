package br.com.simapd.simapd.modules.users.useCases;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.simapd.simapd.modules.users.UsersRepository;
import br.com.simapd.simapd.modules.users.dto.UserResponseDTO;
import br.com.simapd.simapd.modules.users.mapper.UsersMapper;

@Service
public class GetAllUsersUseCase {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private UsersMapper usersMapper;

  public Page<UserResponseDTO> execute(Pageable pageable) {
    Page<br.com.simapd.simapd.modules.users.UsersEntity> users = usersRepository.findAll(pageable);
    
    List<UserResponseDTO> userDTOs = users.getContent().stream()
        .map(usersMapper::toResponseDTO)
        .collect(Collectors.toList());

    return new PageImpl<>(userDTOs, pageable, users.getTotalElements());
  }
} 