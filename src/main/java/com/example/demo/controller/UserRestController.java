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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
