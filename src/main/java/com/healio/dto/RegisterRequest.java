package com.healio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Email private String email;
    @NotBlank private String password;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotNull private UserRole role;
}
