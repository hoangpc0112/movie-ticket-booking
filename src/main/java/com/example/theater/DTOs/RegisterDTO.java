package com.example.theater.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RegisterDTO {

    @NotEmpty (message = "Email không được để trống.")
    @Email (message = "Email không đúng định dạng.")
    private String email;

    @NotEmpty (message = "Username không được để trống.")
    private String username;

    @Size (min = 6, message = "Mật khẩu phải có tổi thiếu 6 ký tự.")
    @NotEmpty (message = "Mật khẩu không thể để trống.")
    private String password;

    @NotEmpty (message = "Không thể để trống.")
    private String confirmPassword;
}
