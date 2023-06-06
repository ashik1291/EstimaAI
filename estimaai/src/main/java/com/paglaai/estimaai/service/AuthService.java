package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.dto.AuthenticationRequest;
import com.paglaai.estimaai.domain.dto.AuthenticationResponse;
import com.paglaai.estimaai.domain.dto.UserDto;
import com.paglaai.estimaai.domain.mapper.DtoToEntityMapper;
import com.paglaai.estimaai.repository.entity.RoleEntity;
import com.paglaai.estimaai.repository.RoleRepository;
import com.paglaai.estimaai.repository.UserRepository;
import com.paglaai.estimaai.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DtoToEntityMapper dtoToEntityMapper;
    
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);
        return new AuthenticationResponse(jwt);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto createUser(UserDto userDto){

        var userEntity = dtoToEntityMapper.userDtoToEntity(userDto);

        var role = roleRepository.findByName("ADMIN");
        if(role == null){
            var newRoles = new RoleEntity();
            newRoles.setName("ADMIN");
            role = newRoles;
        }

        userEntity.setRoles(List.of(role));
        userRepository.save(userEntity);

        return userDto;
    }
}
