package com.paglaai.estimaai.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {

    @NotEmpty(message = "user name is required")
    @Size(max = 100, message = "username length limit is exceeded")
    private String username;

    @NotEmpty(message = "password is required")
    @Size(max = 50, message = "password length limit is exceeded")
    private String password;

}
