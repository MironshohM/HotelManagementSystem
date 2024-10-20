package com.example.demo.config;

import com.example.demo.dtos.auth.AuthUserCreateDTO;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repo.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUserLoader {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostConstruct
    public void createDefaultUsers() {
        if (!userRepository.findAll().isEmpty()) {
            return;
        }
        try {
            User user = authService.create(new AuthUserCreateDTO("user", "abcd1234@A", "testuser@gmail.com", Role.USER));
            User admin = authService.create(new AuthUserCreateDTO("admin", "abcd1234@A", "testadmin@gmail.com", Role.ADMIN));
        }catch (Exception e){
            System.out.println("Exception while creating default users");
        }
    }
}
