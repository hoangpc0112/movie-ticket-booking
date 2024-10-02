package com.example.theater.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RegisterDTO {

    @NotEmpty(message = "Username cannot be empty.")
    private String username;

    @Size(min = 6, message = "Minimum password length is 6 characters")
    @NotEmpty(message = "Password cannot be empty.")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty.")
    private String confirmPassword;
}
