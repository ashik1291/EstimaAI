package com.paglaai.estimaai.controller;

import com.paglaai.estimaai.domain.dto.UserDto;
import com.paglaai.estimaai.domain.dto.AuthenticationRequest;
import com.paglaai.estimaai.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authService.login(authenticationRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.createUser(userDto));
    }
}
