package com.bookswap.platform.dto;

public record AuthResponse(
        String message,
        Long userId,
        String email,
        String fullName,
        String role
) {
}