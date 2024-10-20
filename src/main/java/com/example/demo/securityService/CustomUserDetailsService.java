package com.example.demo.securityService;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userByEmailOpt = userRepository.findUserByEmail(email);

        if (userByEmailOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new CustomUserDetails(userByEmailOpt.get()); // Return the custom user details
    }
}
