package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.dto.UserDto;
import com.paglaai.estimaai.domain.dto.UserProfileWithHistories;
import com.paglaai.estimaai.exception.UserNotFoundException;
import com.paglaai.estimaai.repository.UserRepository;
import com.paglaai.estimaai.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserProfileWithHistories getProfileWithReportHistory(){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var userEntity = userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UserNotFoundException("No user profile found.");
        }

        var userProfileWithHistories = new UserProfileWithHistories();
        userProfileWithHistories.setName(userEntity.getName());
        userProfileWithHistories.setEmail(email);
        userProfileWithHistories.setReportHistories(userEntity.getReportHistoryEntities());

        return userProfileWithHistories;
    }

    private UserDto mapToUserDto(UserEntity user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
