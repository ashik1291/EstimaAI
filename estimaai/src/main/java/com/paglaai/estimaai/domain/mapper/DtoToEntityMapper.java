package com.paglaai.estimaai.domain.mapper;

import com.paglaai.estimaai.domain.dto.UserDto;
import com.paglaai.estimaai.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoToEntityMapper {

    private final PasswordEncoder passwordEncoder;

    public User userDtoToEntity(UserDto userDto){

        var name = userDto.getLastName() != null ? userDto.getFirstName().concat(" ").concat(userDto.getLastName()) :
                userDto.getFirstName().concat("");

        return new User().setName(name).setEmail(userDto.getEmail())
                .setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
}
