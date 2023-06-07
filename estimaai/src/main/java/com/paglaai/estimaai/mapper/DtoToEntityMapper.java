package com.paglaai.estimaai.mapper;

import com.paglaai.estimaai.domain.UserDto;
import com.paglaai.estimaai.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoToEntityMapper {

    private final PasswordEncoder passwordEncoder;

    public UserEntity userDtoToEntity(UserDto userDto){

        var name = userDto.getLastName() != null ? userDto.getFirstName().concat(" ").concat(userDto.getLastName()) :
                userDto.getFirstName().concat("");

        return new UserEntity().setName(name).setEmail(userDto.getEmail())
                .setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
}
