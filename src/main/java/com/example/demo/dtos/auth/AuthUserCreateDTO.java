package com.example.demo.dtos.auth;

import com.example.demo.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AuthUserCreateDTO(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank
        @Email(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$") String email,
        @NotBlank Role role) implements Serializable {
}