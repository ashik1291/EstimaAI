package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.dto.UserDto;
import com.paglaai.estimaai.exception.UserNotFoundException;
import com.paglaai.estimaai.repository.UserRepository;
import com.paglaai.estimaai.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto findUserByEmail(String email) {

        var userEntity =  userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UserNotFoundException("No user was found.");
        }
        return mapToUserDto(userEntity);
    }


    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
