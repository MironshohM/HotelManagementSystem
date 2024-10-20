package com.example.demo.config;

import com.example.demo.dtos.AppErrorDTO;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.securityService.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final CustomUserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public SecurityConfig(CustomUserDetailsService userDetailsService, ObjectMapper objectMapper, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    private static final String[] WHITE_LIST = {
//            "/hotel/",
            //below 3 endpoints are used auth process
            "/api/auth/**",
//            "/hotel/css/**",

            // Swagger-related paths
            "/v3/api-docs/**",
            "/hotel/swagger-ui/**",
            "/hotel/swagger-ui.html"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().fullyAuthenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenUtil, userDetailsService()), UsernamePasswordAuthenticationFilter.class)
                .build();
                /*.formLogin(form -> form
                        .loginPage("/hotel/login")
                        .permitAll()
                        .defaultSuccessUrl("/home", true)
                )
                .logout(LogoutConfigurer::permitAll);*/
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration source = new CorsConfiguration();
        source.setAllowedOriginPatterns(List.of("*"));
        source.setAllowedHeaders(List.of("*"));
        source.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        UrlBasedCorsConfigurationSource source1 = new UrlBasedCorsConfigurationSource();
        source1.registerCorsConfiguration("/**", source);
        return source1;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            authException.printStackTrace();
            String errorPath = request.getRequestURI();
            String errorMessage = authException.getMessage();
            int errorCode = 401;
            AppErrorDTO appErrorDTO = new AppErrorDTO(errorMessage, errorPath, errorCode);
            response.setStatus(errorCode);
            ServletOutputStream outputStream = response.getOutputStream();
            objectMapper.writeValue(outputStream, appErrorDTO);
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User authUser = userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new UserDetails(authUser);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            accessDeniedException.printStackTrace();
            String errorPath = request.getRequestURI();
            String errorMessage = accessDeniedException.getMessage();
            int errorCode = 403;
            AppErrorDTO appErrorDTO = new AppErrorDTO(errorMessage, errorPath, errorCode);
            response.setStatus(errorCode);
            ServletOutputStream outputStream = response.getOutputStream();
            objectMapper.writeValue(outputStream, appErrorDTO);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
}
