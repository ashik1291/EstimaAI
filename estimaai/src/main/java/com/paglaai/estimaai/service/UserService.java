package com.paglaai.estimaai.service;

import com.paglaai.estimaai.domain.dto.UserDto;
import com.paglaai.estimaai.repository.entity.Role;
import com.paglaai.estimaai.repository.entity.User;
import com.paglaai.estimaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserDto userDto){

        User user = new User();
        var lastName = userDto.getLastName() == null ? "": userDto.getLastName();
        user.setName(userDto.getFirstName().concat(" ").concat(lastName))
                .setEmail(userDto.getEmail())
                .setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleService.getRoleByName("ROLE_ADMIN");
        if(role == null){
            role = roleService.checkRoleExist();
        }
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
