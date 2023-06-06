package com.paglaai.estimaai.controller;

import com.paglaai.estimaai.domain.dto.UserDto;
import com.paglaai.estimaai.domain.dto.UserProfileWithHistories;
import com.paglaai.estimaai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile/{email}")
    public ResponseEntity<UserDto> getUserProfileByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping("/profile-report-histories")
    public ResponseEntity<UserProfileWithHistories> getUserProfileWithReportHistories(){
        return ResponseEntity.ok(userService.getProfileWithReportHistory());
    }

}
