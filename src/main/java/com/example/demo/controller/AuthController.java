package com.example.demo.controller;

import com.example.demo.dtos.RefreshTokenRequest;
import com.example.demo.dtos.auth.AuthUserCreateDTO;
import com.example.demo.dtos.auth.TokenRequest;
import com.example.demo.dtos.auth.TokenResponse;
import com.example.demo.entity.User;
import com.example.demo.exceptions.DuplicateUsernameException;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/access/token")
    public ResponseEntity<TokenResponse> generateToken(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.generateToken(tokenRequest));
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Nonnull @Valid @RequestBody AuthUserCreateDTO authUserCreateDTO) throws DuplicateUsernameException, DuplicateUsernameException {
        return ResponseEntity.ok(authService.create(authUserCreateDTO));
    }
}
