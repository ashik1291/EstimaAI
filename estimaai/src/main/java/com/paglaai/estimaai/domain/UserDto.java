package com.paglaai.estimaai.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDto {

    @NotEmpty(message = "First name is required")
    @Size(max = 50, message = "First name length limit is exceeded")
    private String firstName;

    @Size(max = 50, message = "Last name length limit is exceeded")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(max = 50, message = "Password length limit is exceeded")
    private String password;

}
