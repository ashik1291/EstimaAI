package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.AuthenticationRequest;
import com.paglaai.estimaai.domain.AuthenticationResponse;
import com.paglaai.estimaai.domain.UserDto;
import com.paglaai.estimaai.exception.ResourceAlreadyExistsException;
import com.paglaai.estimaai.mapper.DtoToEntityMapper;
import com.paglaai.estimaai.repository.RoleRepository;
import com.paglaai.estimaai.repository.UserRepository;
import com.paglaai.estimaai.repository.entity.RoleEntity;
import com.paglaai.estimaai.security.JwtTokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final DtoToEntityMapper dtoToEntityMapper;

  public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtTokenProvider.createToken(authentication);
    return new AuthenticationResponse(jwt);
  }

  @Transactional(rollbackFor = Exception.class)
  public UserDto createUser(UserDto userDto) {

    var existsByEmail = userRepository.findByEmail(userDto.getEmail());
    if(existsByEmail != null){
      throw new ResourceAlreadyExistsException("user already exists by email");
    }

    var userEntity = dtoToEntityMapper.userDtoToEntity(userDto);

    var role = roleRepository.findByName("ADMIN");
    if (role == null) {
      var newRoles = new RoleEntity();
      newRoles.setName("ADMIN");
      role = newRoles;
    }

    userEntity.setRoles(List.of(role));
    userRepository.save(userEntity);

    return userDto;
  }
}
