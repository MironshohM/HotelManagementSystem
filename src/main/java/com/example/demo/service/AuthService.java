package com.example.demo.service;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.dtos.RefreshTokenRequest;
import com.example.demo.dtos.auth.AuthUserCreateDTO;
import com.example.demo.dtos.auth.TokenRequest;
import com.example.demo.dtos.auth.TokenResponse;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.enums.TokenType;
import com.example.demo.exceptions.DuplicateUsernameException;
import com.example.demo.mappers.AuthUserMapper;
import com.example.demo.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository authUserRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    private final AuthUserMapper authUserMapper;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse generateToken(@NonNull TokenRequest tokenRequest) {
        String username = tokenRequest.email();
        String password = tokenRequest.password();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(username);
    }

    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        if (!jwtTokenUtil.isValid(refreshToken, TokenType.REFRESH)) {
            throw new CredentialsExpiredException("Token is invalid");
        }
        String username = jwtTokenUtil.getUsername(refreshToken, TokenType.REFRESH);
        User authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with \"%d\" username."));
        TokenResponse tokenResponse = TokenResponse.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpiry(jwtTokenUtil.getExpiry(refreshToken, TokenType.REFRESH))
                .build();
        return jwtTokenUtil.generateAccessToken(username, tokenResponse);
    }

    public User create(AuthUserCreateDTO dto) throws DuplicateUsernameException {
        User authUser = authUserMapper.toEntity(dto);
        authUser.setPassword(passwordEncoder.encode(dto.password()));
        authUser.setRole(dto.role() == null ? Role.USER : dto.role());
        try {
            return authUserRepository.save(authUser);
        } catch (Exception e) {
            throw new DuplicateUsernameException("Username or email is already used");
        }
    }
}
