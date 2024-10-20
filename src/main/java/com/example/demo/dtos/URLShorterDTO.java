package com.example.demo.dtos;

import jakarta.validation.constraints.Future;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public record URLShorterDTO(
        @URL(message = "Invalid url")
        String originalURL,
        String description,
        @Future(message = "Expiration date must be in the future")
        LocalDateTime expireDate) {
}