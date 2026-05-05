package com.bookswap.platform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequestDto(
        @NotNull Long userId,
        @NotNull @Size(max = 500) String message
) {
}